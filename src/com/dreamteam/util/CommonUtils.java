package com.dreamteam.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import android.os.Build;

import com.dreamteam.lookme.R;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.enumattribute.Country;
import com.dreamteam.lookme.enumattribute.Gender;
import com.dreamteam.lookme.service.Services;

public class CommonUtils {

	public static final String[] genderArray = { Gender.M.toString(), Gender.F.toString(), Gender.TG.toString() };
	public static final Integer[] genderImages = { R.drawable.mars_symbol, R.drawable.venus_symbol, R.drawable.transgender_symbol };

	public static final String[] countryArray = { Country.CA.toString(), Country.CN.toString(), Country.DE.toString(), Country.FR.toString(), Country.IT.toString(),
			Country.KR.toString(), Country.TW.toString(), Country.UK.toString(), Country.US.toString() };
	public static final Integer[] countryImages = { R.drawable.canada, R.drawable.china, R.drawable.germany, R.drawable.france, R.drawable.italy, R.drawable.korea,
			R.drawable.taiwan, R.drawable.uk, R.drawable.us };

	public static byte[] getBytes(Object obj) {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final ObjectOutputStream os;

		try {
			os = new ObjectOutputStream(out);
			os.writeObject(obj);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return out.toByteArray();
	}

	public static String getConversationId(String myId, String otherProfileId) {
		if (myId.compareTo(otherProfileId) < 0)
			return myId + "_" + otherProfileId;
		else
			return otherProfileId + "_" + myId;
	}

	public static Node getNodeFromConversationId(String conversationId) {
		String[] nodes = conversationId.split("_");
		if (!Services.currentState.getMyBasicProfile().getId().equals(nodes[0]))
			return getNodeFromProfileId(nodes[0]);
		else
			return getNodeFromProfileId(nodes[1]);
	}

	public static Node getNodeFromProfileId(String profileId) {
		Collection<Node> nodeJoined = Services.currentState.getSocialNodeMap().values();
		Iterator<Node> iter = nodeJoined.iterator();
		while (iter.hasNext()) {
			Node tempNode = iter.next();
			if (tempNode.getProfile().getId().equalsIgnoreCase(profileId))
				return tempNode;
		}
		Log.d("getNodeFromProfileId profile not found!profileId: " + profileId);
		return null;
	}

	/**
	 * Get a diff between two dates
	 * 
	 * @param date1
	 *            the oldest date
	 * @param date2
	 *            the newest date
	 * @param timeUnit
	 *            the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	public static String timeElapsed(Date date1, Date date2) {
		Long diff = getDateDiff(date1, date2, TimeUnit.SECONDS);
		if (diff <= 60)
			return "now";
		else if (getDateDiff(date1, date2, TimeUnit.MINUTES) <= 60)
			return getDateDiff(date1, date2, TimeUnit.MINUTES) + " minutes ago";
		else if (getDateDiff(date1, date2, TimeUnit.DAYS) <= 7)
			return getDateDiff(date1, date2, TimeUnit.MINUTES) + " days ago";
		else {
			SimpleDateFormat dfDate_day = new SimpleDateFormat("dd/MM/yyyy");
			String dt = "";
			Calendar c = Calendar.getInstance();
			return dfDate_day.format(c.getTime());
		}
	}

	public static boolean isEmulator() {
		return "goldfish".equals(Build.HARDWARE);
	}

	public static String parseDate(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return (sdf.format(d.getTime()));
	}

	public static Date parseDate(String s) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return sdf.parse(s);
	}

	public static void cleanMem() {
		System.gc();
		try {
			Thread.sleep(200L);
			return;
		} catch (InterruptedException localInterruptedException) {
		}
	}
}
