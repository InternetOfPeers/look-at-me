package com.brainmote.lookatme.constants;

public interface AppSettings {
	// 2000 è la durata di default di un Toast.LENGTH_SHORT
	// 3500 è la durata di default di un Toast.LENGTH_LONG

	static final long SPLASH_DISPLAY_LENGHT = 3000; // Millisecondi
	static final long LOADING_PROFILE_TIMEOUT = 20000; // Millisecondi
	static final long WAIT_BEFORE_SHOWING_LOADING_PROFILE_DIALOG = 500; // Millisecondi
	static final long DOUBLE_BACK_TO_REALLY_EXIT_DELAY = 2000; // Millisecondi

	static final String MAIN_CHANNEL = "com.brainmote.lookatme.mainChannel";
	static final String INVOKE_DEVELOPERS = "com.brainmote.lookatme.settings.invokeDevelopers";
	static final String USER_PREFERENCES = "com.brainmote.lookatme.userPreferences";

	static final int fakeUsers = 0;
	static final boolean fakeUsersEnabled = fakeUsers > 0;

}
