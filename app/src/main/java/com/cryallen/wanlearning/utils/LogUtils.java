package com.cryallen.wanlearning.utils;

import android.util.Log;

/***
 * 日志工具类
 @author Allen
 @DATE 2019-09-02
 ***/
public final class LogUtils {
	/**
	 * 日志输出级别NONE
	 */
	public static final int LEVEL_NONE = 0;
	/**
	 * 日志输出级别E
	 */
	public static final int LEVEL_ERROR = 1;
	/**
	 * 日志输出级别W
	 */
	public static final int LEVEL_WARN = 2;
	/**
	 * 日志输出级别I
	 */
	public static final int LEVEL_INFO = 3;
	/**
	 * 日志输出级别D
	 */
	public static final int LEVEL_DEBUG = 4;
	/**
	 * 日志输出级别V
	 */
	public static final int LEVEL_VERBOSE = 5;

	/**
	 * 日志输出时的TAG
	 */
	private static String mTag = "LogUtils";

	/**
	 * 是否允许输出log
	 */
	private static int mDebuggable = LEVEL_NONE ;

	private LogUtils() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	/**
	 * 设置调试Log开关
	 */
	public static void setDebuggable(boolean isEnable) {
		mDebuggable = isEnable ? LEVEL_VERBOSE : LEVEL_NONE;
	}

	/**
	 * 设置调试Log TAG名称
	 */
	public static void setTagName(String tagName) {
		mTag = tagName;
	}

	/**
	 * 以级别为 v 的形式输出LOG
	 */
	public static void v(String msg) {
		if (mDebuggable >= LEVEL_VERBOSE) {
			Log.v(mTag, msg);
		}
	}

	public static void v(String tag, String str) {
		if (mDebuggable >= LEVEL_VERBOSE) {
			Log.v(mTag, "[ " + tag + " ] : " + str);
		}
	}

	/**
	 * 以级别为 d 的形式输出LOG
	 */
	public static void d(String msg) {
		if (mDebuggable >= LEVEL_DEBUG) {
			Log.d(mTag, msg);
		}
	}

	public static void d(String tag, String str) {
		if (mDebuggable >= LEVEL_DEBUG) {
			Log.d(mTag, "[ " + tag + " ] : " + str);
		}
	}

	/**
	 * 以级别为 i 的形式输出LOG
	 */
	public static void i(String msg) {
		if (mDebuggable >= LEVEL_INFO) {
			Log.i(mTag, msg);
		}
	}

	public static void i(String tag, String str) {
		if (mDebuggable >= LEVEL_INFO) {
			Log.i(mTag, "[ " + tag + " ] : " + str);
		}
	}

	/**
	 * 以级别为 w 的形式输出LOG
	 */
	public static void w(String msg) {
		if (mDebuggable >= LEVEL_WARN) {
			Log.w(mTag, msg);
		}
	}

	/**
	 * 以级别为 e 的形式输出LOG
	 */
	public static void e(String msg) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.e(mTag, msg);
		}
	}

	public static void w(String tag, String str) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.w(mTag, "[ " + tag + " ] : " + str);
		}
	}

	/**
	 * 以级别为 w 的形式输出Throwable
	 */
	public static void w(Throwable tr) {
		w("", tr);
	}

	/**
	 * 以级别为 w 的形式输出LOG信息和Throwable
	 */
	public static void w(String msg, Throwable tr) {
		Log.w(mTag, msg, tr);
	}

	public static void w(String tag, String str, Exception e) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.w(mTag, "[ " + tag + " ] : " + str, e);
		}
	}

	/**
	 * 以级别为 e 的形式输出Throwable
	 */
	public static void e(Throwable tr) {
		e("", tr);
	}

	/**
	 * 以级别为 e 的形式输出LOG信息和Throwable
	 */
	public static void e(String msg, Throwable tr) {
		if (mDebuggable >= LEVEL_ERROR && null != msg) {
			Log.e(mTag, msg, tr);
		}
	}

	public static void e(String tag, String str) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.e(mTag, "[ " + tag + " ] : " + str);
		}
	}

	public static void e(String tag, String str,Throwable tr) {
		if (mDebuggable >= LEVEL_ERROR) {
			Log.e(mTag, "[ " + tag + " ] : " + str, tr);
		}
	}

	private static int originStackIndex = 2;

	/**
	 * 获取当前方法所在的文件名
	 *
	 * @return 当前方法所在的文件名
	 */
	public static String getFileName() {
		return Thread.currentThread().getStackTrace()[originStackIndex].getFileName();
	}

	/**
	 * 获取当前方法所在的Class名
	 *
	 * @return 当前方法所在的Class名
	 */
	public static String getClassName() {
		return Thread.currentThread().getStackTrace()[originStackIndex].getClassName();
	}

	/**
	 * 获取当前方法名
	 *
	 * @return 当前方法名
	 */
	public static String getMethodName() {
		return Thread.currentThread().getStackTrace()[originStackIndex].getMethodName();
	}

	/**
	 * 获取当前代码执行处行数
	 *
	 * @return 当前代码执行处行数
	 */
	public static int getLineNumber() {
		return Thread.currentThread().getStackTrace()[originStackIndex].getLineNumber();
	}
}
