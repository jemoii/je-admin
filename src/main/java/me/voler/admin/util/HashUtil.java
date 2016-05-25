package me.voler.admin.util;

import java.nio.charset.Charset;

public class HashUtil {

	public static String hash64Byte(String string) {
		MurmurHash3.LongPair pair = new MurmurHash3.LongPair();
		byte[] key = string.getBytes(Charset.forName("UTF-8"));
		MurmurHash3.murmurhash3_x64_128(key, 0, key.length, 0, pair);
		return toBinaryString(pair.val1);
	}

	public static String hash32Byte(String string) {
		byte[] key = string.getBytes(Charset.forName("UTF-8"));
		return toBinaryString(MurmurHash3.murmurhash3_x86_32(key, 0, key.length, 0));
	}

	/**
	 * 当前系统时间的16进制表示的逆序字符串
	 * 
	 * @return
	 * 
	 * @see java.lang.Long#toHexString
	 */
	public static String randomKey() {
		long i = System.currentTimeMillis();
		char[] buf = new char[1 << 4];
		int charPos = 0;
		for (; i != 0; ++charPos, i >>>= 4) {
			long index = i & (buf.length - 1);
			if (index > 9) {
				index = index + 'a' - ':';
			}
			buf[charPos] = (char) (index + '0');
		}

		return new String(buf, 0, charPos);
	}

	/**
	 * 
	 * @param i
	 * @return
	 * 
	 * @see java.lang.Long#toBinaryString
	 */
	private static String toBinaryString(long i) {
		char[] buf = new char[64];
		int charPos = buf.length - 1;
		for (; i != 0; --charPos, i >>>= 1) {
			buf[charPos] = (char) ((i & 1) + '0');
		}
		for (; charPos >= 0; --charPos) {
			buf[charPos] = (char) ('0');
		}
		return new String(buf);
	}

	/**
	 * 
	 * @param i
	 * @return
	 * 
	 * @see java.lang.Integer#toBinaryString
	 */
	private static String toBinaryString(int i) {
		char[] buf = new char[32];
		int charPos = buf.length - 1;
		for (; i != 0; --charPos, i >>>= 1) {
			buf[charPos] = (char) ((i & 1) + '0');
		}
		for (; charPos >= 0; --charPos) {
			buf[charPos] = (char) ('0');
		}
		return new String(buf);
	}

}
