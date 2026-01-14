package com.cryallen.wanlearning.utils;

import android.content.Context;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/***
 * 转换相关工具类
 * @author Allen
 * @DATE 2019-09-10
 ***/
public final class ConvertUtils {

	private ConvertUtils() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	/**
	 * byteArr转hexString
	 * <p>例如：</p>
	 * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
	 *
	 * @param bytes 字节数组
	 * @return 16进制大写字符串
	 */
	public static String bytes2HexString(final byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		int len = bytes.length;
		if (len <= 0) {
			return null;
		}
		char[] ret = new char[len << 1];
		for (int i = 0, j = 0; i < len; i++) {
			ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
			ret[j++] = hexDigits[bytes[i] & 0x0f];
		}
		return new String(ret);
	}

	/**
	 * hexString转byteArr
	 * <p>例如：</p>
	 * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
	 *
	 * @param hexString 十六进制字符串
	 * @return 字节数组
	 */
	public static byte[] hexString2Bytes(String hexString) {
		if (isSpace(hexString)) {
			return null;
		}
		int len = hexString.length();
		if (len % 2 != 0) {
			hexString = "0" + hexString;
			len = len + 1;
		}
		char[] hexBytes = hexString.toUpperCase().toCharArray();
		byte[] ret = new byte[len >> 1];
		for (int i = 0; i < len; i += 2) {
			ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
		}
		return ret;
	}

	/**
	 * Convert byte[] to hex string.将byte转换成int，
	 * 然后利用Integer.toHexString(int)来转换成16进制字符串。
	 *
	 * @param src byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * hexChar转int
	 *
	 * @param hexChar hex单个字节
	 * @return 0..15
	 */
	private static int hex2Dec(final char hexChar) {
		if (hexChar >= '0' && hexChar <= '9') {
			return hexChar - '0';
		} else if (hexChar >= 'A' && hexChar <= 'F') {
			return hexChar - 'A' + 10;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * charArr转byteArr
	 *
	 * @param chars 字符数组
	 * @return 字节数组
	 */
	public static byte[] chars2Bytes(final char[] chars) {
		if (chars == null || chars.length <= 0) {
			return null;
		}
		int len = chars.length;
		byte[] bytes = new byte[len];
		for (int i = 0; i < len; i++) {
			bytes[i] = (byte) (chars[i]);
		}
		return bytes;
	}

	/**
	 * byteArr转charArr
	 *
	 * @param bytes 字节数组
	 * @return 字符数组
	 */
	public static char[] bytes2Chars(final byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		int len = bytes.length;
		if (len <= 0) {
			return null;
		}
		char[] chars = new char[len];
		for (int i = 0; i < len; i++) {
			chars[i] = (char) (bytes[i] & 0xff);
		}
		return chars;
	}

	/**
	 * bytes转bits
	 *
	 * @param bytes 字节数组
	 * @return bits
	 */
	public static String bytes2Bits(final byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte aByte : bytes) {
			for (int j = 7; j >= 0; --j) {
				sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
			}
		}
		return sb.toString();
	}

	/**
	 * bits转bytes
	 *
	 * @param bits 二进制
	 * @return bytes
	 */
	public static byte[] bits2Bytes(String bits) {
		int lenMod = bits.length() % 8;
		int byteLen = bits.length() / 8;
		// 不是8的倍数前面补0
		if (lenMod != 0) {
			for (int i = lenMod; i < 8; i++) {
				bits = "0" + bits;
			}
			byteLen++;
		}
		byte[] bytes = new byte[byteLen];
		for (int i = 0; i < byteLen; ++i) {
			for (int j = 0; j < 8; ++j) {
				bytes[i] <<= 1;
				bytes[i] |= bits.charAt(i * 8 + j) - '0';
			}
		}
		return bytes;
	}

	/**
	 * inputStream转outputStream
	 *
	 * @param is 输入流
	 * @return outputStream子类
	 */
	public static ByteArrayOutputStream input2OutputStream(final InputStream is) {
		if (is == null) {
			return null;
		}
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int len;
			while ((len = is.read(b, 0, 1024)) != -1) {
				os.write(b, 0, len);
			}
			return os;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			CloseUtils.closeIO(is);
		}
	}

	/**
	 * outputStream转inputStream
	 *
	 * @param out 输出流
	 * @return inputStream子类
	 */
	public ByteArrayInputStream output2InputStream(final OutputStream out) {
		if (out == null) {
			return null;
		}
		return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
	}

	/**
	 * inputStream转byteArr
	 *
	 * @param is 输入流
	 * @return 字节数组
	 */
	public static byte[] inputStream2Bytes(final InputStream is) {
		if (is == null) {
			return null;
		}
		return input2OutputStream(is).toByteArray();
	}

	/**
	 * byteArr转inputStream
	 *
	 * @param bytes 字节数组
	 * @return 输入流
	 */
	public static InputStream bytes2InputStream(final byte[] bytes) {
		if (bytes == null || bytes.length <= 0) {
			return null;
		}
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * outputStream转byteArr
	 *
	 * @param out 输出流
	 * @return 字节数组
	 */
	public static byte[] outputStream2Bytes(final OutputStream out) {
		if (out == null) {
			return null;
		}
		return ((ByteArrayOutputStream) out).toByteArray();
	}

	/**
	 * outputStream转byteArr
	 *
	 * @param bytes 字节数组
	 * @return 字节数组
	 */
	public static OutputStream bytes2OutputStream(final byte[] bytes) {
		if (bytes == null || bytes.length <= 0) {
			return null;
		}
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
			os.write(bytes);
			return os;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			CloseUtils.closeIO(os);
		}
	}

	/**
	 * inputStream转string按编码
	 *
	 * @param is 输入流
	 * @param charsetName 编码格式
	 * @return 字符串
	 */
	public static String inputStream2String(final InputStream is, final String charsetName) {
		if (is == null || isSpace(charsetName)) {
			return null;
		}
		try {
			return new String(inputStream2Bytes(is), charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * string转inputStream按编码
	 *
	 * @param string 字符串
	 * @param charsetName 编码格式
	 * @return 输入流
	 */
	public static InputStream string2InputStream(final String string, final String charsetName) {
		if (string == null || isSpace(charsetName)) {
			return null;
		}
		try {
			return new ByteArrayInputStream(string.getBytes(charsetName));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * outputStream转string按编码
	 *
	 * @param out 输出流
	 * @param charsetName 编码格式
	 * @return 字符串
	 */
	public static String outputStream2String(final OutputStream out, final String charsetName) {
		if (out == null || isSpace(charsetName)) {
			return null;
		}
		try {
			return new String(outputStream2Bytes(out), charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * string转outputStream按编码
	 *
	 * @param string 字符串
	 * @param charsetName 编码格式
	 * @return 输入流
	 */
	public static OutputStream string2OutputStream(final String string, final String charsetName) {
		if (string == null || isSpace(charsetName)) {
			return null;
		}
		try {
			return bytes2OutputStream(string.getBytes(charsetName));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * dp转px
	 *
	 * @param dpValue dp值
	 * @return px值
	 */
	public static int dp2px(Context context, final float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转dp
	 *
	 * @param pxValue px值
	 * @return dp值
	 */
	public static int px2dp(Context context,final float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * sp转px
	 *
	 * @param spValue sp值
	 * @return px值
	 */
	public static int sp2px(Context context,final float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * px转sp
	 *
	 * @param pxValue px值
	 * @return sp值
	 */
	public static int px2sp(Context context,final float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 判断字符串是否为null或全为空白字符
	 *
	 * @param s 待校验字符串
	 * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
	 */
	private static boolean isSpace(final String s) {
		if (s == null) {
			return true;
		}
		for (int i = 0, len = s.length(); i < len; ++i) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
