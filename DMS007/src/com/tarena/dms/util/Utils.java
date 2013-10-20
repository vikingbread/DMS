package com.tarena.dms.util;

import java.io.UnsupportedEncodingException;

/**
 * @author Viking
 * @version 创建时间：2013-10-19 下午2:35:21
 * 
 */
public class Utils {

	public static int toInt(byte[] arr, int offset) {
		
		int d1 = arr[offset] & 0xff;
		int d2 = arr[offset+1] & 0xff;
		int d3 = arr[offset+2] & 0xff;
		int d4 = arr[offset+3] & 0xff;
		return (d1 << 24) + (d2 << 16) + (d3 << 8) + d4;

	}
	
public static short toShort(byte[] arr, int offset) {
		
		int d1 = arr[offset] & 0xff;
		int d2 = arr[offset+1] & 0xff;
		return (short) ((d1 << 8) + d2);

	}


	public static  String toString(byte[] arr, int offset, int len) {
		String str = null;
		try {
			str = new String(arr, offset, len, "iso8859-1").trim();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return str;
	}

	public static void printHexr(byte[] buf) {
		int b;
		for (int i = 0; i < buf.length; i++) {
			b = (byte) (buf[i] & 0xff);
			b &= 0xff;
			if (b <= 0xf) {
				System.out.print("0");
			}

			System.out.print(Integer.toHexString(b) + " ");

			if (((i + 1) % 16) == 0) {
				System.out.println();
			}
		}
		System.out.println();
	}

}
