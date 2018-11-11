package com.zyd.wlwsdk.utils;

import android.util.Log;

/**
 * Log统一管理类
 * 
 * @author way
 * 
 */
public class L
{

	private L()
	{
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 测试 true
	 * 正式 false
	 */
	public static boolean isDebug = true;// false  true 是否需要打印bug，可以在application的onCreate函数里面初始化
	private static final String TAG = "----Hygo-Log----";

	// 下面四个是默认tag的函数
	public static void i(String msg)
	{
		mLog("i", TAG, msg);
	}

	public static void d(String msg)
	{
		mLog("d", TAG, msg);
	}

	public static void e(String msg)
	{
		mLog("e", TAG, msg);
	}

	public static void v(String msg)
	{
		mLog("v", TAG, msg);
	}

	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg)
	{
		mLog("i", tag, msg);
	}

	public static void d(String tag, String msg)
	{
		mLog("d", tag, msg);
	}

	public static void e(String tag, String msg)
	{
		mLog("e", tag, msg);
	}

	public static void v(String tag, String msg)
	{
		mLog("v", tag, msg);
	}

	private static void mLog(String type, String tag, String msg){
		if (tag == null) {
			tag = TAG;
		}
		if (isDebug){
			int segmentSize = 3 * 1024;
			long length = msg.length();
			if (length <= segmentSize ) {// 长度小于等于限制直接打印
				switch (type) {
					case "i":
						Log.i(tag, msg);
						break;
					case "d":
						Log.d(tag, msg);
						break;
					case "e":
						Log.e(tag, msg);
						break;
					case "v":
						Log.v(tag, msg);
						break;
					default:
				}
			}else {
				while (msg.length() > segmentSize ) {// 循环分段打印日志
					String logContent = msg.substring(0, segmentSize );
					msg = msg.replace(logContent, "");
					switch (type) {
						case "i":
							Log.i(tag, logContent);
							break;
						case "d":
							Log.d(tag, logContent);
							break;
						case "e":
							Log.e(tag, logContent);
							break;
						case "v":
							Log.v(tag, logContent);
							break;
						default:
					}
				}
				// 打印剩余日志
				switch (type) {
					case "i":
						Log.i(tag, msg);
						break;
					case "d":
						Log.d(tag, msg);
						break;
					case "e":
						Log.e(tag, msg);
						break;
					case "v":
						Log.v(tag, msg);
						break;
					default:
				}
			}
		}
	}
}