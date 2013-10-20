package com.tarena.dms.test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.tarena.dms.util.Utils;

/**
 * @author Viking
 * @version 创建时间：2013-10-19 下午4:17:17
 * 
 */
public class Demo {

	public static void main(String[] args) throws IOException {
		File file = new File("temp.log".trim());//temp.log wtmpx
	//	File file = new File("wtmpx");//temp.log wtmpx
		System.out.println(file.getAbsolutePath());
		RandomAccessFile in = new RandomAccessFile(file, "r");
		int count = 2;
		int logLenth = 372;
		byte[] log = new byte[logLenth * count];
//		in.seek(372 * 6);
		in.read(log);
		in.close();
		int offset = 0;
		for (int i = 0; i < count; i++) {
			offset = i*logLenth;
			String user = Utils.toString(log, 0 + offset, 32);
			int pid = Utils.toInt(log, 68 + offset);
			short type = Utils.toShort(log, 72 + offset);
			int time = Utils.toInt(log, 80 + offset);
			String host = Utils.toString(log, 114 + offset, 257);
			System.out.println(user + "," + pid + "," + type + "," + time + ","
					+ host);
		}
	}

}
