package com.brainmote.lookatme.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.bean.ChatMessage;

public class FormatUtils {

	private static final long A_SECOND = 1000; // Millisecondi
	private static final long A_MINUTE = A_SECOND * 60;
	private static final long AN_HOUR = A_MINUTE * 60;
	private static final long A_DAY = AN_HOUR * 24;
	private static final long A_WEEK = A_DAY * 7;

	/**
	 * 
	 * @param message
	 * @return
	 */
	public static CharSequence formatMessageTimestamp(ChatMessage message) {
		return getFormattedTimestamp(message.getTimestamp());
	}

	/**
	 * 
	 * @param conversation
	 * @return
	 */
	public static CharSequence formatConversationTimestamp(ChatConversation conversation) {
		return getFormattedTimestamp(conversation.getLastMessageTimestamp());
	}

	/**
	 * 
	 * @param timestamp
	 * @return
	 */
	private static CharSequence getFormattedTimestamp(Date timestamp) {
		Long milliseconds = getDateDiffInMilliseconds(timestamp, new Date());
		if (milliseconds < A_MINUTE)
			return "now";
		if (milliseconds < AN_HOUR)
			return TimeUnit.MINUTES.convert(milliseconds, TimeUnit.MILLISECONDS) + " minutes ago";
		if (milliseconds < A_DAY)
			return TimeUnit.HOURS.convert(milliseconds, TimeUnit.MILLISECONDS) + " hours ago";
		if (milliseconds < A_WEEK)
			return TimeUnit.DAYS.convert(milliseconds, TimeUnit.MILLISECONDS) + " days ago";
		return SimpleDateFormat.getDateInstance().format(timestamp);
	}

	/**
	 * 
	 * @param aDate
	 * @param anotherDate
	 * @return
	 */
	private static long getDateDiffInMilliseconds(Date aDate, Date anotherDate) {
		long milliseconds = aDate.getTime() - anotherDate.getTime();
		return milliseconds > 0 ? milliseconds : -milliseconds;
	}

}
