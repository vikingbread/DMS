package com.tarena.dms.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tarena.dms.util.Utils;

/** 
 * @author  Viking
 * @version 创建时间：2013-10-19 下午2:52:36 
 * 
 */
public class ReadLogTest2 {
	public static void main(String[] args) throws IOException {
		File file = new File("wtmpx");
		InputStream is = new FileInputStream(file);
		byte[] buf = new byte[372];
		is.read(buf);
		is.close();
		String user = Utils.toString(buf, 0, 32);
		int pid = Utils.toInt(buf, 68);
		short type = Utils.toShort(buf, 72);
		int time = Utils.toInt(buf, 80);
		String host = Utils.toString(buf, 114, 257);
		System.out.print(user+","+pid+","+type+","+time+","+host);
	}
}
