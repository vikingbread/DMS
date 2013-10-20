package com.tarena.dms.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.tarena.dms.bo.ClientConfig;

/** 
 * 读取解析日志数据并且向服务器发送 
 */
public class DMSClient {
	private static ClientConfig config = new ClientConfig();
	
	public DMSClient() {
		System.out.println(config);
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
		config.setLogFile(prop.getProperty("log.file", "wtmpx"));
		config.setTempLogFile(prop.getProperty("temp.log.file", "temp.log"));
		config.setTextLogFile(prop.getProperty("text.log.file", "log.txt"));
		config.setLogRecFile(prop.getProperty("log.rec.file", "logrec.txt"));
		config.setLoginLogFile(prop.getProperty("login.log.file", "login.txt"));
		config.setLogFile(prop.getProperty("last.position.file", "last-position.txt"));
		config.setStepIndexFile(prop.getProperty("step.index.file", "step-index.txt"));
		config.setBatch(Integer.parseInt(prop.getProperty("batch", "10")));
		config.setServerHost(prop.getProperty("dms.server.host", "127.0.0.1"));
		config.setDmsServerPort(Integer.parseInt(prop.getProperty("dms.server.port", "8899")));
		config.setServerHost(prop.getProperty("server.host", "192.168.0.20"));
		
	}

	private void initConfig() {
		
	}

	private void sendLogs() {
		
	}

	private void matchLogs() {
		
	}

	private void parseLogs() {
		
	}

	private void readNextLogs() {
		
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



