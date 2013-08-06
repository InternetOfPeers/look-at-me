package com.dreamteam.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dreamteam.lookme.EditProfileActivity;
import com.dreamteam.lookme.FeelingsActivity;
import com.dreamteam.lookme.ConversationsActivity;
import com.dreamteam.lookme.NearbyActivity;
import com.google.common.collect.HashBiMap;

/**
 * Classe di utilità per la gestione della navigazione tra le varie activity
 * dell'applicazione
 */
public class Nav {

	private static final String STRING_VOID = "";
	private static final String STRING_KEY = "string_key";
	private static final HashBiMap<Class<? extends Activity>, Integer> map;
	static {
		map = HashBiMap.create();
		map.put(EditProfileActivity.class, 0);
		map.put(NearbyActivity.class, 1);
		map.put(ConversationsActivity.class, 2);
		map.put(FeelingsActivity.class, 3);
	}

	/**
	 * Avvia un'activity
	 * 
	 * @param currentActivity
	 * @param destinationActivity
	 */
	public static void startActivity(Activity currentActivity, Class<? extends Activity> destinationActivity) {
		startActivityWithParameters(currentActivity, destinationActivity, new Bundle());
	}

	/**
	 * Avvia un'activity passando un parametro di tipo String
	 * 
	 * @param currentActivity
	 * @param destinationActivity
	 * @param stringParameter
	 */
	public static void startActivityWithString(Activity currentActivity, Class<? extends Activity> destinationActivity, String stringParameter) {
		Bundle bundle = new Bundle();
		bundle.putString(STRING_KEY, stringParameter);
		startActivityWithParameters(currentActivity, destinationActivity, bundle);
	}

	/**
	 * Avvia un'activity passando un gruppo di parametri
	 * 
	 * @param currentActivity
	 * @param destinationActivity
	 * @param bundle
	 */
	public static void startActivityWithParameters(Activity currentActivity, Class<? extends Activity> destinationActivity, Bundle bundle) {
		if (destinationActivity != null) {
			Intent intent = new Intent(currentActivity, destinationActivity).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.putExtras(bundle);
			currentActivity.startActivity(intent);
		}
	}

	/**
	 * Recupera l'eventuale parametro di tipo String passato all'activity
	 * 
	 * @param currentActivity
	 * @return
	 */
	public static String getStringParameter(Activity currentActivity) {
		if (currentActivity != null && currentActivity.getIntent() != null && currentActivity.getIntent().getExtras() != null
				&& currentActivity.getIntent().getExtras().getString(STRING_KEY) != null) {
			return currentActivity.getIntent().getExtras().getString(STRING_KEY);
		} else {
			return STRING_VOID;
		}
	}

	/**
	 * Recupera gli eventuali parametri passati all'activity. In caso non ve ne
	 * siano, viene ritornato un bundle vuoto.
	 * 
	 * @param currentActivity
	 * @return
	 */
	public static Bundle getParameters(Activity currentActivity) {
		if (currentActivity != null && currentActivity.getIntent() != null && currentActivity.getIntent().getExtras() != null) {
			return currentActivity.getIntent().getExtras();
		} else {
			return new Bundle();
		}
	}

	/**
	 * Recupera l'activity corrispondente alla posizione della voce di menu
	 * indicata
	 * 
	 * @param position
	 * @return
	 */
	public static Class<? extends Activity> getActivityFromMenuPosition(int position) {
		return map.inverse().get(position);
	}

	/**
	 * Recupera la posizione dell'activity all'interno delle voci di menu
	 * 
	 * @param activity
	 * @return
	 */
	public static int getMenuPositionFromActivityClass(Class<? extends Activity> activity) {
		return map.containsKey(activity) ? map.get(activity) : -1;
	}
}
