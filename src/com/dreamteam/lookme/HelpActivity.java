package com.dreamteam.lookme;

import com.dreamteam.util.Log;

public class HelpActivity extends CommonActivity {

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
	};

}
