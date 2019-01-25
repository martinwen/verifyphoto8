package com.shcy.yyzzj.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class MD5Helper {

	private static byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}

	// see this How-to for a faster way to convert
	// a byte array to a HEX string
	public static String getMD5Checksum(String filename) throws Exception {
		byte[] b = createChecksum(filename);
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			result.append(Integer.toString((b[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return result.toString();
	}

	public static String getMD5Checksum(byte[] data) throws Exception {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			result.append(Integer.toString((data[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return result.toString();
	}

	public static String EncoderByMd5(String str) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("md5");// 返回实现指定摘要算法的
																// MessageDigest
																// 对象。
		md5.update(str.getBytes());// 先将字符串转换成byte数组，再用byte 数组更新摘要
		byte[] nStr = md5.digest();// 哈希计算，即加密
		return bytes2Hex(nStr);// 加密的结果是byte数组，将byte数组转换成字符串
	}

	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;

		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
}
