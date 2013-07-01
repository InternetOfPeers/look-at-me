package com.dreamteam.lookme.test;

import android.test.ActivityInstrumentationTestCase2;

import com.dreamteam.lookme.MainActivity;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public MainActivityTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testActivity() {
		MainActivity activity = getActivity();
		assertNotNull(activity);
	}

}
