package com.brainmote.lookatme.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;

import com.brainmote.lookatme.R;
import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.enumattribute.Country;
import com.brainmote.lookatme.enumattribute.Gender;
import com.brainmote.lookatme.service.Services;
import com.brainmote.reflection.Reflection;
import com.google.common.base.Optional;

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
			return new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
		}
	}

	public static boolean isEmulator() {
		return "goldfish".equals(Build.HARDWARE);
	}

	public static String parseDate(Date date) {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date.getTime());
	}

	public static Date parseDate(String date) throws Exception {
		return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date);
	}

	public static void cleanMem() {
		System.gc();
		try {
			Thread.sleep(200L);
			return;
		} catch (InterruptedException localInterruptedException) {
			Log.d("Fallito sleep: " + localInterruptedException);
		}
	}

	public static boolean isApplicationInForeground(Context context) {
		return getForegroundActivityPackageName(context).equalsIgnoreCase(context.getPackageName().toString());
	}

	public static boolean isScreenOn(Context context) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

	public static String getForegroundActivityPackageName(Context context) {
		return getApplicationServices(context).get(0).topActivity.getPackageName().toString();
	}

	public static String getForegroundActivityClassName(Context context) {
		return getApplicationServices(context).get(0).topActivity.getClassName();
	}

	private static List<RunningTaskInfo> getApplicationServices(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getRunningTasks(Integer.MAX_VALUE);
	}

	public static void debugPrintCurrentNodeList() {
		Log.d("Elenco dei nodeId presenti nella SocialMap:");
		for (Node nodo : Services.currentState.getSocialNodeMap().getNodeList()) {
			Log.d(nodo.getId());
		}
		Log.d("--------------------------");
	}

	/**
	 * Verifica se è presente una connessione di tipo WiFi e se il device
	 * risulta correttamente connesso
	 * 
	 * @param context
	 *            il contesto dell'applicazione
	 * @return true se il device è connesso ad una rete WiFi, false in caso
	 *         contrario
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		// Verifico che il device sia connesso ad una WiFi
		if (networkInfo.isAvailable() && networkInfo.getDetailedState() == DetailedState.CONNECTED)
			return true;
		// Controllo a questo punto se almeno il device ha creato un access
		// point WiFi, altrimenti non considero il device connesso
		return isWifiApEnabled(context);
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isWifiApEnabled(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return (Boolean) Optional.fromNullable(Reflection.invokeMethod_NoParameters_ReturnObject(wifiManager, "isWifiApEnabled")).or(false);
	}
}
