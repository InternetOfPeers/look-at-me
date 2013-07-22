package com.dreamteam.util;

import com.google.common.base.Strings;

public class Log {

	private static final String SEPARATOR = ":";
	private static final int NESTING_LEVEL = 4;

	public static void i() {
		android.util.Log.i(getCallingClassName(), getCallingMethod());
	}

	public static void d() {
		android.util.Log.d(getCallingClassName(), getCallingMethod());
	}

	public static void e() {
		android.util.Log.e(getCallingClassName(), getCallingMethod());
	}

	public static void i(String message) {
		android.util.Log.i(getCallingClassName(), getCallingMethod() + SEPARATOR + Strings.nullToEmpty(message));
	}

	public static void d(String message) {
		android.util.Log.d(getCallingClassName(), getCallingMethod() + SEPARATOR + Strings.nullToEmpty(message));
	}

	public static void e(String message) {
		android.util.Log.e(getCallingClassName(), getCallingMethod() + SEPARATOR + Strings.nullToEmpty(message));
	}

	private static String getCallingClassName() {
		String[] classNameSplit = Thread.currentThread().getStackTrace()[NESTING_LEVEL].getClassName().split("\\.");
		return classNameSplit[classNameSplit.length - 1];
	}

	private static String getCallingMethod() {
		return Thread.currentThread().getStackTrace()[NESTING_LEVEL].getMethodName();
	}

}
