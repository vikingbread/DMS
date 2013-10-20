package com.tarena.dms.bo;

public class LogData {

	/**
	 * 登录状态, 是type属性的值
	 */
	public static final short USER_PROCESS = 7;

	/**
	 * 登出状态, 是type属性的值
	 */
	public static final short DEAD_PROCESS = 8;

	/** Unix 标准登录类型定义 */
	private static final String[] TYPES = { "EMPTY", "RUN_LVL", "BOOT_TIME",
			"OLD_TIME", "NEW_TIME", "INIT_PROCESS", "LOGIN_PROCESS",
			"USER_PROCESS", "DEAD_PROCESS", "ACCOUNTING" };

	/**
	 * 日志文件记录长度, 是Unix日志标准长度
	 */
	public static final int LOG_LENGTH = 372;

	/**
	 * OS帐号, 也是 登录用户名
	 */
	private String user;
	/**
	 * 业务帐号,用户属性偏移位置, 采用ASCII编码存储
	 */
	public static final int USER_OFFSET = 0;
	public static final int USER_LENGTH = 32;
	/**
	 * 登录进程号码 int类型
	 */
	private int pid;
	/**
	 * 进程号偏移位置, 连续4个byte, 是一个int数据
	 */
	public static final int PID_OFFSET = 68;
	/**
	 * 登录类型: LOGIN 或 LOGOUT short类型
	 */
	private short type;
	/**
	 * 登录类型偏移位置, 连续两个byte, 是short数据
	 */
	public static final int TYPE_OFFSET = 72;
	/**
	 * 登录时间, 以秒为单位, int类型, 是1970年开始秒数
	 */
	private int time;
	/**
	 * 登录类型偏移位置, 连续4个byte, int类型
	 */
	public static final int TIME_OFFSET = 80;

	/**
	 * 登录用户主机名, 经常是IP地址
	 */
	private String host;
	/**
	 * 登录用户主机名偏移位置, 是ASCII编码字符串, 每个byte对应一个字符
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
	 * 将String 类型的log 解析为登录记录, 其中log是文本数, 如:
	 * "as080705,45123,7,1357642539,192.168.1.48" 格式为: "user,pid,time,type,host"
	 * 
	 * @param log
	 *            文本日志, 格式与toString()返回值一致
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
