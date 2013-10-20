package com.tarena.dms.test;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tarena.dms.util.Utils;

/** 
 * @author  Viking
 * @version 创建时间：2013-10-19 上午11:14:02 
 * 
 */
public class ReadLogTest1 {
	
	public static void main(String[] args) throws IOException {
		File file = new File("wtmpx");
		InputStream is = new FileInputStream(file);
		byte[] buf = new byte[372];
		
		is.read(buf);
		printBuffer(buf);
		is.close();
		System.out.println(new String(buf,0,32,"iso8859-1").trim());
		System.out.println(new String(buf,114,257,"iso8859-1").trim());
		int pid = Utils.toInt(buf, 68);
		System.out.println(pid);
		DataInputStream dis = new DataInputStream(new FileInputStream(file) );
		dis.skip(68);
		System.out.println(dis.readInt());
		dis.close();
	}

	public static void printBuffer(byte[] buf) {
		int b;
		for(int i=0;i<buf.length;i++){
			b = (byte) (buf[i] & 0xff) ;
			b &= 0xff; 
			if(b<=0xf){
				System.out.print("0");
			}
			
			System.out.print(Integer.toHexString(b)+" ");
			
			if(((i+1)%16)==0){
				System.out.println();
			}
		}
		System.out.println();
		
	}

}
