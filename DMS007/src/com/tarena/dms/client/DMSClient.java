package com.tarena.dms.client;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Properties;
import java.util.Scanner;

import com.tarena.dms.bo.ClientConfig;
import com.tarena.dms.bo.LogData;

/**
 * 读取解析日志数据并且向服务器发送
 */
public class DMSClient {
	private static ClientConfig config = new ClientConfig();

	public DMSClient() {
	}

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
		config.setTempLogFile(new File(prop.getProperty("temp.log.file", "temp.log").trim()));
		config.setTextLogFile(new File(prop.getProperty("text.log.file", "log.txt").trim()));
		config.setLogRecFile(new File(prop.getProperty("log.rec.file", "logrec.txt").trim()));
		config.setLoginLogFile(new File(prop.getProperty("login.log.file", "login.txt").trim()));
		config.setLastPositionFile(new File(prop.getProperty("last.position.file",
				"last-position.txt").trim())); 
		config.setStepIndexFile(new File(prop.getProperty("step.index.file",
				"step-index.txt").trim()));
		config.setBatch(Integer.parseInt(prop.getProperty("batch", "10")));
		config.setServerHost(prop.getProperty("dms.server.host", "127.0.0.1").trim());
		config.setDmsServerPort(Integer.parseInt(prop.getProperty(
				"dms.server.port", "8899")));
		config.setServerHost(prop.getProperty("server.host", "192.168.0.20").trim());

	}

	private void sendLogs() {

	}

	private void matchLogs() {

	}

	private void parseLogs() {

	}

	private void readNextLogs() {

		if (config.getTempLogFile().exists()) {
			System.out.println(config.getTempLogFile().exists());
			return;
		}
		long postion = 0;

		if (config.getLastPositionFile().exists()) {
			postion = readPostion(config.getLastPositionFile());
		}
		
		System.out.println(postion);
		int count = config.getBatch()*LogData.LOG_LENGTH;
		byte[] log = loadLog(postion,count);
		if(log!=null && saveTempLog(log)){
			savePostion(postion+count);
		}
		
		System.out.println();
	}

	private boolean saveTempLog(byte[] log) {
		boolean ret = false;
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(new FileOutputStream(config.getTempLogFile()));
			dos.write(log);
			ret = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			config.getTempLogFile().delete();
			e.printStackTrace();
		}finally{
			try {
				dos.close();
			} catch (IOException e) {
				ret = false;
				e.printStackTrace();
			}
		}
		return ret;
	}

	private byte[] loadLog(long postion,int count) {
		RandomAccessFile ra = null;
		long length = 0;
		byte[] log = new byte[count];
		if(config.getLogFile().exists()){
			try {
				ra = new RandomAccessFile(config.getLogFile(), "r");
				length =ra.length();
				if (postion>=length) {
					return null;
				}
				ra.seek(postion);
				ra.read(log);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					ra.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return log;
	}

	private void savePostion(long l) {
		
		PrintWriter printWriter= null;
		try {
			printWriter = new PrintWriter(config.getLastPositionFile());
			printWriter.write(Long.toString(l));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			printWriter.close();
		}
		
	}

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
		}finally{
			scanner.close();
		}
		return postion;
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

	public static void main(String[] args) {
		DMSClient client = new DMSClient();
		client.action();
	}
}
