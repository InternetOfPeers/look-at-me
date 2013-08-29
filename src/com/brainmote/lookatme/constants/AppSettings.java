package com.brainmote.lookatme.constants;

public interface AppSettings {
	// 2000 � la durata di default di un Toast.LENGTH_SHORT
	// 3500 � la durata di default di un Toast.LENGTH_LONG

	static final long SPLASH_DISPLAY_LENGHT = 3000; // Millisecondi
	static final long LOADING_PROFILE_TIMEOUT = 10000; // Millisecondi
	static final long WAIT_BEFORE_SHOWING_LOADING_PROFILE_DIALOG = 500; // Millisecondi
	static final long DOUBLE_BACK_TO_REALLY_EXIT_DELAY = 2000; // Millisecondi

	static final String SOCIAL_CHANNEL_NAME = "com.brainmote.lookatme.SOCIAL_CHANNEL";

	static final int fakeUsers = 4;
	static final boolean fakeUsersEnabled = fakeUsers > 0;

}
