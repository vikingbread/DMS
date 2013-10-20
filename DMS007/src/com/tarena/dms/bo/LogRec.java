package com.tarena.dms.bo;

/**
 * logname 用户登录名 
 * logintime 登入时刻 
 * logouttime 登出时刻
 * durations 登录时间长度 单位秒 
 * logip 登录终端的ip地址
 * 
 * "登录登出对" 类型
 */
public class LogRec {

	private LogData login;
	private LogData  logout;
	
	private String serverHost;

	public LogRec() {
	}
 
	public LogRec(LogData login, LogData logout, String serverHost) {
		if(login.getType()!=LogData.USER_PROCESS){
			throw new RuntimeException("不是登录记录!");
		}
		if(logout.getType()!=LogData.DEAD_PROCESS){
			throw new RuntimeException("不是登出记录");
		}
		if (!login.getUser().equals(logout.getUser())) {
			throw new RuntimeException("登录登出必须是同一个用户!");
		}
		if (login.getPid() != logout.getPid()) {
			throw new RuntimeException("登录登出必须是同一个进程!");
		}
		if (!login.getHost().equals(logout.getHost())) {
			throw new RuntimeException("登录登出必须是同一个主机!");
		}
		this.login = login;
		this.logout = logout;
		this.serverHost = serverHost;
	}
	/**
	 * 将文本文件中的一行解析为一个 "登录记录对" 对象
	 * @param fileRow 
	 */
	public LogRec(String fileRow) {
		String[] data = fileRow.split("\\|");//匹配竖线
		this.login = new LogData(data[0]);
		this.logout = new LogData(data[1]);
		this.serverHost = data[2];
	}
	public String getUser() {
		return login.getUser();
	}

	public String getHost() {
		return login.getHost();
	}

	public int getPid() {
		return login.getPid();
	}

	public int getLoginTime() {
		return login.getTime();
	}

	public int getLogoutTime() {
		return logout.getTime();
	}

	public LogData getLogin() {
		return login;
	}

	public LogData getLogout() {
		return logout;
	}
	
	public int getDurations() {
		return getLogoutTime() - getLoginTime();
	}
	
	public String getServerHost() {
		return serverHost;
	}
	
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public String toString() {
		return login.toString() + "|" + logout.toString()+"|"+serverHost;
	}
}
