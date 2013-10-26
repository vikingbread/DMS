package com.tarena.dms.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.tarena.dms.bo.ClientConfig;
import com.tarena.dms.bo.LogData;
import com.tarena.dms.bo.LogRec;
import com.tarena.dms.util.Utils;

/**
 * 读取解析日志数据并且向服务器发送
 */
public class DMSClient {
	/**
	 * 保存配置
	 */
	private static ClientConfig config = new ClientConfig();

	public static void main(String[] args) {
		DMSClient client = new DMSClient();
		client.action();
	}

	public DMSClient() {
	}
	
	public void action() {
		while (true) {
			readNextLogs();
			parseLogs();
			matchLogs();
			sendLogs();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取下一段日志
	 */
	private void readNextLogs() {
	
		if (config.getTempLogFile().exists()) {
			return;
		}
		long postion = 0;
	
		if (config.getLastPositionFile().exists()) {
			postion = readPostion(config.getLastPositionFile());
		}
		//得到读取的字节数
		int count = config.getBatch() * LogData.LOG_LENGTH;
		
		byte[] log = loadLog(postion, count);
		if (log != null && saveTempLog(log)) {
			//读取保存成功了 才保存位置
			savePostion(postion + count);
		}
	
	}

	/**
	 * 分析日志
	 */
	private void parseLogs() {
		if (config.getTextLogFile().exists()||!config.getTempLogFile().exists()) {// log.txt file
			return;
		}
	
		DataInputStream ds = null;
		PrintWriter pw = null;
		try {
			byte[] buf = new byte[LogData.LOG_LENGTH];
			ds = new DataInputStream(new FileInputStream(
					config.getTempLogFile()));
			pw = new PrintWriter(config.getTextLogFile());
			LogData log;
			//逐条读取 temp.log 解析保存为log.txt 
			while (ds.read(buf) != -1) {
				log = conertByteToLogData(buf);
				pw.println(log.toString());
			}
	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ds != null) {
					ds.close();
					config.getTempLogFile().delete();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			pw.close();
		}
	
	}

	/**
	 * 匹配日志
	 */
	private void matchLogs() {
		
		if (config.getLogRecFile().exists()
				|| !config.getTextLogFile().exists()) {
			return;
		}
		//载入log.txt logins.txt
		List<LogData> logs = loadLog(config.getTextLogFile());
		List<LogData> logins = loadLog(config.getLoginLogFile());
	
		logs.addAll(logins);
		List<LogRec> logRecList = new ArrayList<LogRec>(LogData.LOG_LENGTH);
		int j = logins.size();//
		LogData temp;
		//迭代集合 查找匹配的日志
		for (int i = 0; i < logs.size(); i++) {
			temp = logs.get(i);
			for (j = i + 1; j < logs.size(); j++) {
				if (temp.isMach(logs.get(j))) {
					logRecList
							.add(new LogRec(temp, logs.get(j), temp.getHost()));
					logs.remove(j);
					logs.remove(i);
					break;
				}
			}
		}
		//保存 匹配与不匹配的 log
		saveToTxtFile(logRecList, config.getLogRecFile());
		saveToTxtFile(logs, config.getLoginLogFile());
		
		config.getTextLogFile().delete();
	}

	/**
	 * 发送日志
	 */
	private void sendLogs() {
		if (!config.getLogRecFile().exists()) {
			return;
		}
		List<LogRec> list = loadLogRec();
		System.out.println();
	}

	/**
	 * 保存读取到的部分日志
	 * @param log 需要保存的日志
	 * @return 是否成功
	 */
	private boolean saveTempLog(byte[] log) {
		boolean ret = false;
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(new FileOutputStream(
					config.getTempLogFile()));
			dos.write(log);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			config.getTempLogFile().delete();
			e.printStackTrace();
		} finally {
			try {
				if (dos != null) {
					dos.close();
				}
				ret = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * 随机读取日志文件
	 * @param postion 起始位置
	 * @param count   读取字节数
	 * @return 读取到的字节日志
	 */
	private byte[] loadLog(long postion, int count) {
		RandomAccessFile ra = null;
		long length = 0;
		byte[] log = new byte[count];
		if (config.getLogFile().exists()) {
			try {
				ra = new RandomAccessFile(config.getLogFile(), "r");
				length = ra.length();
				if (postion >= length) {
					return null;
				}
				ra.seek(postion);
				ra.read(log);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					ra.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return log;
	}

	/**
	 * 保存当前日志位置
	 * @param 位置
	 */
	private void savePostion(long l) {
	
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(config.getLastPositionFile());
			printWriter.write(Long.toString(l));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
		}
	
	}

	/**
	 * 读取位置
	 * @param lastPostionFile 存储文件
	 * @return 位置
	 */
	private long readPostion(File lastPostionFile) {
		long postion = 0;
		Scanner scanner = null;
		try {
			scanner = new Scanner(lastPostionFile);
			postion = Long.parseLong(scanner.next());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return postion;
	}

	/**
	 * 将日志从字节信息转换成日志对象
	 * @param data  字节信息
	 * @return	日志对象
	 */
	private LogData conertByteToLogData(byte[] data) {
		LogData log = new LogData();
		log.setUser(Utils.toString(data, 0, 32));// 解析用户
		log.setPid(Utils.toInt(data, 68));// 解析进程号
		log.setType(Utils.toShort(data, 72));// 解析登录状态
		log.setTime(Utils.toInt(data, 80));// 解析登录时刻
		log.setHost(Utils.toString(data, 114, 257));// 主机名IP
		return log;
	}

	/**
	 * 导入logrec.txt文件 的内容
	 * @return LogRec 对象列表
	 */
	private List<LogRec> loadLogRec() {
		List<LogRec> logs = new LinkedList<LogRec>();
		Scanner sc = null;
		File file = config.getLogRecFile();
		try {
			if (!file.exists()) {
				return logs;
			}
			sc = new Scanner(file);
			while (sc.hasNext()) {
				logs.add(new LogRec(sc.nextLine()));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		return logs;
	}
	
	/**
	 * 保存log、logins 列表 
	 * @param list 需要保存的列表
	 * @param file 存储到的文件
	 */
	private void saveToTxtFile(List list, File file) {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(file);
			for (int i = 0; i < list.size(); i++) {
				pw.println(list.get(i).toString());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
	/**
	 * 载入log.txt logins.txt
	 * @param 文件夹
	 * @return LogData对象 列表
	 */
	private List<LogData> loadLog(File file) {
		List<LogData> logs = new LinkedList<LogData>();
		Scanner sc = null;
		try {
			if (!file.exists()) {
				return logs;
			}
			sc = new Scanner(file);
			while (sc.hasNext()) {
				logs.add(new LogData(sc.nextLine()));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		return logs;
	}
	//初始化配置
	static {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("config.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		config.setLogFile(new File(prop.getProperty("log.file", "wtmpx").trim()));
		config.setTempLogFile(new File(prop.getProperty("temp.log.file",
				"temp.log").trim()));
		config.setTextLogFile(new File(prop.getProperty("text.log.file",
				"log.txt").trim()));
		config.setLogRecFile(new File(prop.getProperty("log.rec.file",
				"logrec.txt").trim()));
		config.setLoginLogFile(new File(prop.getProperty("login.log.file",
				"login.txt").trim()));
		config.setLastPositionFile(new File(prop.getProperty(
				"last.position.file", "last-position.txt").trim()));
		config.setStepIndexFile(new File(prop.getProperty("step.index.file",
				"step-index.txt").trim()));
		config.setBatch(Integer.parseInt(prop.getProperty("batch", "10")));
		config.setServerHost(prop.getProperty("dms.server.host", "127.0.0.1")
				.trim());
		config.setDmsServerPort(Integer.parseInt(prop.getProperty(
				"dms.server.port", "8899")));
		config.setServerHost(prop.getProperty("server.host", "192.168.0.20")
				.trim());
	
	}
}
