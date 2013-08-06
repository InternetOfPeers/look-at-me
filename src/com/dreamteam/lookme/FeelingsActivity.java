package com.dreamteam.lookme;

import com.dreamteam.util.Log;

public class FeelingsActivity extends CommonActivity {

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feeling);
		initDrawerMenu(savedInstanceState, this.getClass());
	};
}
