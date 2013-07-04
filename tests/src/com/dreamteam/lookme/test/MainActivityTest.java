package com.dreamteam.lookme.test;

import android.test.ActivityInstrumentationTestCase2;

import com.dreamteam.lookme.MainActivity;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	MainActivity activity;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	public void testActivity() {
		activity = getActivity();
		assertNotNull(activity);
	}

}
