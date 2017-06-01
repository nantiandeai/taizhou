package com.creatoo.hn.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

/**
 * @version 1.0
 * @auther ly
 * Date: 15-4-23
 * Time: 下午2:03
 */
public class MD5Util {
	/**
	 * 将字符串转换成MD5码
	 *
	 * @param inStr
	 * @return
	 */
	public static String toMd5(String inStr) {
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f'};
		try {
			byte[] strTemp = inStr.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 将任意的二进制字节转换成MD5码
	 *
	 * @param source
	 * @return
	 */
	public static String toMd5(byte[] source) {
		String s = null;
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(source);
			byte tmp[] = md5.digest();          // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char str[] = new char[16 * 2];   // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0;                                // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) {    // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i];  // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];  // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf];   // 取字节中低 4 位的数字转换
			}
			s = new String(str);  // 换后的结果转换为字符串

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * base64编码字符串
	 * @param s 被编码的字符串
	 * @return 经过base64编码后的字符串
	 */
	public static String encode4Base64(String s){
		try{
			if(s != null && !s.isEmpty()) {
				s = (new sun.misc.BASE64Encoder()).encode(s.getBytes());
			}
		}catch(Exception e){
		}
		return s;
	}

	/**
	 * base64q解码字符串
	 * @param s 被解码的字符串
	 * @return 经过base64解码后的字符串
	 */
	public static String decode4Base64(String s){
		try{
			if(s != null && !s.isEmpty()) {
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] b = decoder.decodeBuffer(s);
				s = new String(b);
			}
		}catch(Exception e){

		}
		return s;
	}
	public static void main(String[] args)throws Exception {
		String pwd = "123456";
		//String encodeS = (new sun.misc.BASE64Encoder()).encode( pwd.getBytes() );
		System.out.println( MD5Util.encode4Base64(pwd));
/*		BASE64Decoder decoder = new BASE64Decoder();
		byte[] b = decoder.decodeBuffer(encodeS);
		String decodeS = new String(b);*/
		System.out.println(MD5Util.decode4Base64(MD5Util.encode4Base64(pwd)));
	}
}
