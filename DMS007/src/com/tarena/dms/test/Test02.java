package com.tarena.dms.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tarena.dms.util.Utils;

public class Test02 {
	public static void main(String[] args)
		throws IOException {
		
		File file = new File("wtmpx");
		InputStream in = new FileInputStream(file);
		byte[] log = new byte[372];
		in.read(log);
		String user = Utils.toString(log, 0, 32);//解析用户名
		int pid = Utils.toInt(log, 68);//解析进程号
		short type= Utils.toShort(log, 72);//解析登录状态
		int time = Utils.toInt(log, 80);//解析登录时刻
		String host = Utils.toString(log, 114, 257);//主机名IP
		System.out.println(user+","+
				pid+","+type+","+time+","+host);
		in.close();
	}

}



