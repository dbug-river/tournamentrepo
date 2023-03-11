package com.rivertech.services;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogFactory {

	public static SimpleFormatter getLogFormatter() {
		return new SimpleFormatter() {
			private static final String RESET = "\u001B[0m";
			private static final String SEVERE_COLOR = "\u001B[31m";
			private static final String WARNING_COLOR = "\u001B[33m";
			private static final String INFO_COLOR = "\u001B[32m";
			private static final String CONFIG_COLOR = "\u001B[35m";
			private static final String FINE_COLOR = "\u001B[34m";
			private static final String FINER_COLOR = "\u001B[36m";
			private static final String FINEST_COLOR = "\u001B[37m";

			@Override
			public synchronized String format(LogRecord record) {
				String color;
				if (record.getLevel().equals(Level.SEVERE)) {
					color = SEVERE_COLOR;
				} else if (record.getLevel().equals(Level.WARNING)) {
					color = WARNING_COLOR;
				} else if (record.getLevel().equals(Level.INFO)) {
					color = INFO_COLOR;
				} else if (record.getLevel().equals(Level.CONFIG)) {
					color = CONFIG_COLOR;
				} else if (record.getLevel().equals(Level.FINE)) {
					color = FINE_COLOR;
				} else if (record.getLevel().equals(Level.FINER)) {
					color = FINER_COLOR;
				} else if (record.getLevel().equals(Level.FINEST)) {
					color = FINEST_COLOR;
				} else {
					color = "";
				}
				String message = super.formatMessage(record);
				return color + record.getLevel() + " " + message + RESET + "\n";
			}
		};
	}

	public static Logger getConsoleHandler(String logger) {
		/* Create console handler */
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(getLogFormatter());

		/* Create logger */
		Logger rootLogger = Logger.getLogger(logger);
		rootLogger.addHandler(consoleHandler);

		return rootLogger;
	}
}
