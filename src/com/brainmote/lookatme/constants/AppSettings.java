package com.brainmote.lookatme.constants;

public interface AppSettings {
	// 2000 è la durata di default di un Toast.LENGTH_SHORT
	// 3500 è la durata di default di un Toast.LENGTH_LONG

	static final long SPLASH_DISPLAY_LENGHT = 3000; // Millisecondi
	static final long LOADING_PROFILE_TIMEOUT = 20000; // Millisecondi
	static final long WAIT_BEFORE_SHOWING_LOADING_PROFILE_DIALOG = 500; // Millisecondi
	static final long DOUBLE_BACK_TO_REALLY_EXIT_DELAY = 2000; // Millisecondi
	static final long FAKE_USER_CHAT_RESPONSE_TIME = 2500; // Millisecondi

	/**
	 * Massimo di millisecondi di scostamento rispetto al tempo impostato da
	 * FAKE_USER_CHAT_RESPONSE_TIME
	 */
	static final int FAKE_USER_CHAT_RESPONSE_TIME_OFFSET = 8000;

	static final String MAIN_CHANNEL = "com.brainmote.lookatme.mainChannel";
	static final String IN_APP_CREDITS = "com.brainmote.lookatme.settings.invokeDevelopers";
	static final String USER_PREFERENCES = "com.brainmote.lookatme.userPreferences";

	static final String URL_PREFIX_LINKEDIN = "http://www.linkedin.com/in/";
	static final String URL_PREFIX_FACEBOOK = "https://www.facebook.com/";

	static final int MORE_FAKE_USERS = 0; // In aggiunta ai profili fake dei
											// credits, è possibile aggiungere
											// ulteriori profili finti
	static final boolean NEED_MORE_FAKE_USERS = MORE_FAKE_USERS > 0;

}
