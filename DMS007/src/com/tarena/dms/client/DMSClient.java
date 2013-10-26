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
 * ��ȡ������־���ݲ��������������
 */
public class DMSClient {
	/**
	 * ��������
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
	 * ��ȡ��һ����־
	 */
	private void readNextLogs() {
	
		if (config.getTempLogFile().exists()) {
			return;
		}
		long postion = 0;
	
		if (config.getLastPositionFile().exists()) {
			postion = readPostion(config.getLastPositionFile());
		}
		//�õ���ȡ���ֽ���
		int count = config.getBatch() * LogData.LOG_LENGTH;
		
		byte[] log = loadLog(postion, count);
		if (log != null && saveTempLog(log)) {
			//��ȡ����ɹ��� �ű���λ��
			savePostion(postion + count);
		}
	
	}

	/**
	 * ������־
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
			//������ȡ temp.log ��������Ϊlog.txt 
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
	 * ƥ����־
	 */
	private void matchLogs() {
		
		if (config.getLogRecFile().exists()
				|| !config.getTextLogFile().exists()) {
			return;
		}
		//����log.txt logins.txt
		List<LogData> logs = loadLog(config.getTextLogFile());
		List<LogData> logins = loadLog(config.getLoginLogFile());
	
		logs.addAll(logins);
		List<LogRec> logRecList = new ArrayList<LogRec>(LogData.LOG_LENGTH);
		int j = logins.size();//
		LogData temp;
		//�������� ����ƥ�����־
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
		//���� ƥ���벻ƥ��� log
		saveToTxtFile(logRecList, config.getLogRecFile());
		saveToTxtFile(logs, config.getLoginLogFile());
		
		config.getTextLogFile().delete();
	}

	/**
	 * ������־
	 */
	private void sendLogs() {
		if (!config.getLogRecFile().exists()) {
			return;
		}
		List<LogRec> list = loadLogRec();
		System.out.println();
	}

	/**
	 * �����ȡ���Ĳ�����־
	 * @param log ��Ҫ�������־
	 * @return �Ƿ�ɹ�
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
	 * �����ȡ��־�ļ�
	 * @param postion ��ʼλ��
	 * @param count   ��ȡ�ֽ���
	 * @return ��ȡ�����ֽ���־
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
	 * ���浱ǰ��־λ��
	 * @param λ��
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
	 * ��ȡλ��
	 * @param lastPostionFile �洢�ļ�
	 * @return λ��
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
	 * ����־���ֽ���Ϣת������־����
	 * @param data  �ֽ���Ϣ
	 * @return	��־����
	 */
	private LogData conertByteToLogData(byte[] data) {
		LogData log = new LogData();
		log.setUser(Utils.toString(data, 0, 32));// �����û�
		log.setPid(Utils.toInt(data, 68));// �������̺�
		log.setType(Utils.toShort(data, 72));// ������¼״̬
		log.setTime(Utils.toInt(data, 80));// ������¼ʱ��
		log.setHost(Utils.toString(data, 114, 257));// ������IP
		return log;
	}

	/**
	 * ����logrec.txt�ļ� ������
	 * @return LogRec �����б�
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
	 * ����log��logins �б� 
	 * @param list ��Ҫ������б�
	 * @param file �洢�����ļ�
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
	 * ����log.txt logins.txt
	 * @param �ļ���
	 * @return LogData���� �б�
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
	//��ʼ������
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
