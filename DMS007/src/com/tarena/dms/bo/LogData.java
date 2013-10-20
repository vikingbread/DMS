package com.tarena.dms.bo;

public class LogData {

	/**
	 * ��¼״̬, ��type���Ե�ֵ
	 */
	public static final short USER_PROCESS = 7;

	/**
	 * �ǳ�״̬, ��type���Ե�ֵ
	 */
	public static final short DEAD_PROCESS = 8;

	/** Unix ��׼��¼���Ͷ��� */
	private static final String[] TYPES = { "EMPTY", "RUN_LVL", "BOOT_TIME",
			"OLD_TIME", "NEW_TIME", "INIT_PROCESS", "LOGIN_PROCESS",
			"USER_PROCESS", "DEAD_PROCESS", "ACCOUNTING" };

	/**
	 * ��־�ļ���¼����, ��Unix��־��׼����
	 */
	public static final int LOG_LENGTH = 372;

	/**
	 * OS�ʺ�, Ҳ�� ��¼�û���
	 */
	private String user;
	/**
	 * ҵ���ʺ�,�û�����ƫ��λ��, ����ASCII����洢
	 */
	public static final int USER_OFFSET = 0;
	public static final int USER_LENGTH = 32;
	/**
	 * ��¼���̺��� int����
	 */
	private int pid;
	/**
	 * ���̺�ƫ��λ��, ����4��byte, ��һ��int����
	 */
	public static final int PID_OFFSET = 68;
	/**
	 * ��¼����: LOGIN �� LOGOUT short����
	 */
	private short type;
	/**
	 * ��¼����ƫ��λ��, ��������byte, ��short����
	 */
	public static final int TYPE_OFFSET = 72;
	/**
	 * ��¼ʱ��, ����Ϊ��λ, int����, ��1970�꿪ʼ����
	 */
	private int time;
	/**
	 * ��¼����ƫ��λ��, ����4��byte, int����
	 */
	public static final int TIME_OFFSET = 80;

	/**
	 * ��¼�û�������, ������IP��ַ
	 */
	private String host;
	/**
	 * ��¼�û�������ƫ��λ��, ��ASCII�����ַ���, ÿ��byte��Ӧһ���ַ�
	 */
	public static final int HOST_OFFSET = 114;
	public static final int HOST_LENGTH = 257;

	public LogData() {
	}

	public LogData(String user, int pid, short type, int time, String host) {
		super();
		this.user = user;
		this.pid = pid;
		this.type = type;
		this.time = time;
		this.host = host;
	}
	/**
	 * ��String ���͵�log ����Ϊ��¼��¼, ����log���ı���, ��:
	 * "as080705,45123,7,1357642539,192.168.1.48" ��ʽΪ: "user,pid,time,type,host"
	 * 
	 * @param log
	 *            �ı���־, ��ʽ��toString()����ֵһ��
	 */
	public LogData(String log) {
		String[] data = log.split(",");
		this.user = data[0];
		this.pid = Integer.parseInt(data[1]);
		this.type = Short.parseShort(data[2]);
		this.time = Integer.parseInt(data[3]);
		this.host = data[4];
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	 
	public String toString() {
		String separator = ",";
		String str = this.user + separator + pid + separator + this.type
				+ separator + time + separator + host;
		return str;
	}

}
