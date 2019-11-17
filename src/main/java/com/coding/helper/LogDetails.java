package com.coding.helper;

import org.apache.log4j.Logger;

public class LogDetails {

	public static Logger Log = Logger.getLogger(LogDetails.class.getName());


	public static void startTestCase(String sTestCaseName) {

		Log.info("Execution for : " + sTestCaseName + " started.");

	}

	public static void info(String message) {

		Log.info(message);

	}

	public static void warn(String message) {

		Log.warn(message);

	}

	public static void error(String message) {

		Log.error(message);

	}

	public static void fatal(String message) {

		Log.fatal(message);

	}

	public static void debug(String message) {

		Log.debug(message);

	}

}
