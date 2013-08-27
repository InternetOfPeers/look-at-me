package com.brainmote.lookatme.util;

import com.google.common.base.Strings;

public class Log {

	private static final String SEPARATOR = ":";
	private static final int NESTING_LEVEL = 4;

	public static void v() {
		android.util.Log.v(getCallingClassName(), getCallingMethod());
	}

	public static void d() {
		android.util.Log.d(getCallingClassName(), getCallingMethod());
	}

	public static void i() {
		android.util.Log.i(getCallingClassName(), getCallingMethod());
	}

	public static void w() {
		android.util.Log.w(getCallingClassName(), getCallingMethod());
	}

	public static void e() {
		android.util.Log.e(getCallingClassName(), getCallingMethod());
	}

	public static void wft() {
		android.util.Log.wtf(getCallingClassName(), getCallingMethod());
	}

	public static void v(String message) {
		android.util.Log.v(getCallingClassName(), getCallingMethod() + SEPARATOR + Strings.nullToEmpty(message));
	}

	public static void d(String message) {
		android.util.Log.d(getCallingClassName(), getCallingMethod() + SEPARATOR + Strings.nullToEmpty(message));
	}

	public static void i(String message) {
		android.util.Log.i(getCallingClassName(), getCallingMethod() + SEPARATOR + Strings.nullToEmpty(message));
	}

	public static void w(String message) {
		android.util.Log.w(getCallingClassName(), getCallingMethod() + SEPARATOR + Strings.nullToEmpty(message));
	}

	public static void e(String message) {
		android.util.Log.e(getCallingClassName(), getCallingMethod() + SEPARATOR + Strings.nullToEmpty(message));
	}

	public static void wtf(String message) {
		android.util.Log.wtf(getCallingClassName(), getCallingMethod() + SEPARATOR + Strings.nullToEmpty(message));
	}

	private static String getCallingClassName() {
		String[] classNameSplit = Thread.currentThread().getStackTrace()[NESTING_LEVEL].getClassName().split("\\.");
		return classNameSplit[classNameSplit.length - 1];
	}

	private static String getCallingMethod() {
		return Thread.currentThread().getStackTrace()[NESTING_LEVEL].getMethodName();
	}

}
