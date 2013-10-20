package com.tarena.dms.bo;
/** 
 * @author  Viking
 * @version 创建时间：2013-10-19 下午10:57:08 
 * 
 */
public class ClientConfig {
	
	
	
	private String logFile;
	private String tempLogFile;
	private String textLogFile;
	private String logRecFile;
	private String loginLogFile;
	private String lastPositionFile;
	private String stepIndexFile;
	private int batch;
	private String dmsServerHost;
	private int port;
	private String serverHost;
	
	
	public String getLogFile() {
		return logFile;
	}
	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}
	public String getTempLogFile() {
		return tempLogFile;
	}
	public void setTempLogFile(String tempLogFile) {
		this.tempLogFile = tempLogFile;
	}
	public String getTextLogFile() {
		return textLogFile;
	}
	public void setTextLogFile(String textLogFile) {
		this.textLogFile = textLogFile;
	}
	public String getLogRecFile() {
		return logRecFile;
	}
	public void setLogRecFile(String logRecFile) {
		this.logRecFile = logRecFile;
	}
	public String getLoginLogFile() {
		return loginLogFile;
	}
	public void setLoginLogFile(String loginLogFile) {
		this.loginLogFile = loginLogFile;
	}
	public String getLastPositionFile() {
		return lastPositionFile;
	}
	public void setLastPositionFile(String lastPositionFile) {
		this.lastPositionFile = lastPositionFile;
	}
	public String getStepIndexFile() {
		return stepIndexFile;
	}
	public void setStepIndexFile(String stepIndexFile) {
		this.stepIndexFile = stepIndexFile;
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
