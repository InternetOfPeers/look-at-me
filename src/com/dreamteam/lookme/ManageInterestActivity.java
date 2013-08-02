package com.dreamteam.lookme;

import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;

import android.os.Bundle;

public class ManageInterestActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_manage_interest);
		initMenu(savedInstanceState, this.getClass());

	}
	
	@Override
	public void onBackPressed() {
		Log.d();
		Nav.startActivity(this, EditProfileActivity.class);
	}

}
