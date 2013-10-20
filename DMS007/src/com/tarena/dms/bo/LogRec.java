package com.tarena.dms.bo;

/**
 * logname �û���¼�� 
 * logintime ����ʱ�� 
 * logouttime �ǳ�ʱ��
 * durations ��¼ʱ�䳤�� ��λ�� 
 * logip ��¼�ն˵�ip��ַ
 * 
 * "��¼�ǳ���" ����
 */
public class LogRec {

	private LogData login;
	private LogData  logout;
	
	private String serverHost;

	public LogRec() {
	}
 
	public LogRec(LogData login, LogData logout, String serverHost) {
		if(login.getType()!=LogData.USER_PROCESS){
			throw new RuntimeException("���ǵ�¼��¼!");
		}
		if(logout.getType()!=LogData.DEAD_PROCESS){
			throw new RuntimeException("���ǵǳ���¼");
		}
		if (!login.getUser().equals(logout.getUser())) {
			throw new RuntimeException("��¼�ǳ�������ͬһ���û�!");
		}
		if (login.getPid() != logout.getPid()) {
			throw new RuntimeException("��¼�ǳ�������ͬһ������!");
		}
		if (!login.getHost().equals(logout.getHost())) {
			throw new RuntimeException("��¼�ǳ�������ͬһ������!");
		}
		this.login = login;
		this.logout = logout;
		this.serverHost = serverHost;
	}
	/**
	 * ���ı��ļ��е�һ�н���Ϊһ�� "��¼��¼��" ����
	 * @param fileRow 
	 */
	public LogRec(String fileRow) {
		String[] data = fileRow.split("\\|");//ƥ������
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
