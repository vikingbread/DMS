package com.tarena.dms.bo;

import java.io.File;

/** 
 * @author  Viking
 * @version ����ʱ�䣺2013-10-19 ����10:57:08 
 * 
 */
public class ClientConfig {
	
	
	/**
	 * ��־�ļ�
	 */
	private File logFile;
	/**
	 * ��ʱ��־�ļ�
	 */
	private File tempLogFile;
	/**
	 * 
	 */
	private File textLogFile;
	/**
	 * �ɶԵĵ��롢�ǳ���־
	 */
	private File logRecFile;
	
	private File loginLogFile;
	private File lastPositionFile;
	private File stepIndexFile;
	private int batch;
	private String dmsServerHost;
	private int port;
	private String serverHost;
	
	

	public File getLogFile() {
		return logFile;
	}
	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}
	public File getTempLogFile() {
		return tempLogFile;
	}
	public void setTempLogFile(File tempLogFile) {
		this.tempLogFile = tempLogFile;
	}
	public File getTextLogFile() {
		return textLogFile;
	}
	public void setTextLogFile(File textLogFile) {
		this.textLogFile = textLogFile;
	}
	public File getLogRecFile() {
		return logRecFile;
	}
	public void setLogRecFile(File logRecFile) {
		this.logRecFile = logRecFile;
	}
	public File getLoginLogFile() {
		return loginLogFile;
	}
	public void setLoginLogFile(File loginLogFile) {
		this.loginLogFile = loginLogFile;
	}
	public File getLastPositionFile() {
		return lastPositionFile;
	}
	public void setLastPositionFile(File lastPositionFile) {
		this.lastPositionFile = lastPositionFile;
	}
	public File getStepIndexFile() {
		return stepIndexFile;
	}
	public void setStepIndexFile(File stepIndexFile) {
		this.stepIndexFile = stepIndexFile;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getBatch() {
		return batch;
	}
	public void setBatch(int batch) {
		this.batch = batch;
	}
	public String getDmsServerHost() {
		return dmsServerHost;
	}
	public void setDmsServerHost(String dmsServerHost) {
		this.dmsServerHost = dmsServerHost;
	}
	public int getPort() {
		return port;
	}
	public void setDmsServerPort(int port) {
		this.port = port;
	}
	public String getServerHost() {
		return serverHost;
	}
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	@Override
	public String toString() {
		return "ClientConfig [logFile=" + logFile + ", tempLogFile="
				+ tempLogFile + ", textLogFile=" + textLogFile
				+ ", logRecFile=" + logRecFile + ", loginLogFile="
				+ loginLogFile + ", lastPositionFile=" + lastPositionFile
				+ ", stepIndexFile=" + stepIndexFile + ", batch=" + batch
				+ ", dmsServerHost=" + dmsServerHost + ", port=" + port
				+ ", serverHost=" + serverHost + "]";
	}
	

}
