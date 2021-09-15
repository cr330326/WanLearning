package com.cryallen.wanlearning.utils;

import androidx.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/***
 * Logger封装工具类
 * @author Allen
 * @DATE 2019-09-20
 ***/
public final class LoggerUtils {
	/**
	 * 日志输出时的TAG
	 */
	private static final String TAG = "LoggerUtils";

	/**
	 * 初始化log工具，在app入口处调用
	 *
	 * @param isLogEnable 是否打印log
	 */
	public static void init(boolean isLogEnable) {
		FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
				.showThreadInfo(true)  // (Optional) Whether to show thread info or not. Default true
				.methodCount(2)         // (Optional) How many method line to show. Default 2
				.methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
				.tag(TAG)               // (Optional) Global tag for every log. Default PRETTY_LOGGER
				.build();

		AndroidLogAdapter logAdapter = new AndroidLogAdapter(formatStrategy){
			@Override
			public boolean isLoggable(int priority, @Nullable String tag) {
				return isLogEnable ? true : false;
			}
		};

		Logger.addLogAdapter(logAdapter);
	}

	public static void d(String message) {
		Logger.d(message);
	}

	public static void i(String message) {
		Logger.i(message);
	}

	public static void w(String message, Throwable e) {
		String info = e != null ? e.toString() : "null";
		Logger.w(message + "：" + info);
	}

	public static void e(String message){
		Logger.e(message);
	}

	public static void e(String message, Throwable e) {
		Logger.e(e, message);
	}

	public static void json(String json) {
		Logger.json(json);
	}
}
