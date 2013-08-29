package com.brainmote.lookatme;

import android.os.Bundle;

public class StatisticsActivity extends CommonActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
		checkIfProfileIsCompleted();
	}

}
