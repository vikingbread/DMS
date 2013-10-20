package com.tarena.dms.test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;

/**
 * 读取日志文件 wtmpx 
 */
public class Test01 {
	public static void main(String[] args) 
		throws IOException {
		File file = new File("wtmpx");
		InputStream in= new BufferedInputStream(
				new FileInputStream(file));
		byte[] log = new byte[372];
		//从in(流)中尽可能多的读取数据(byte)填充到log数组中
		//返回值是, 读取的数据的个数372
		//在读取到文件末尾时候返回 -1 
		// log = [00 06 bb 90 00 07]
		int n = in.read(log);	printHex(log);
		String user=new String(log, 0, 32,"ISO8859-1").trim();
		String host=new String(log,114, 257,"ISO8859-1").trim();
		
		int d1 = log[68] & 0xff;
		int d2 = log[69] & 0xff;
		int d3 = log[70] & 0xff;
		int d4 = log[71] & 0xff;
		int pid = (d1<<24) + (d2<<16) + (d3<<8)+d4;
		System.out.println(pid);
		
		DataInputStream dis = new DataInputStream(
				new ByteArrayInputStream(log));
		dis.skipBytes(68);
		int i = dis.readInt();
		System.out.println(i);
		
		System.out.println(user+","+host); 
		System.out.println(n);//372
		n = in.read(log);	printHex(log);
		System.out.println(n);//372
		
		in.close();
	}
	public static void printHex(byte[] ary){
		//假设 ary = [00 06 bb 90 00 07 ....]
		//            0  1  2  3  4  5  ...
		// i                ^
		// b  = 11111111 11111111 11111111 10111011
		// ff = 00000000 00000000 00000000 11111111
		//  & ----------------------------------------
		//      00000000 00000000 00000000 10111011
		// b =  00000000 00000000 00000000 10111011   
		// console : 00 06 bb 
		for(int i=0; i<ary.length; i++){
			int b = ary[i];	b &= 0xff;//消去高24位
			if(b<=0xf){	System.out.print("0");}//单位数补"0"
			System.out.print(Integer.toHexString(b)+" ");
			if((i+1)%16==0){	System.out.println();	}
		}
		System.out.println();
	}
}






