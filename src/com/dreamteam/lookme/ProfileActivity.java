package com.dreamteam.lookme;

import android.os.Bundle;
import android.view.MenuItem;

import com.dreamteam.util.Log;

public class ProfileActivity extends CommonActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		initDrawerMenu(savedInstanceState, this.getClass(), false);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return true;
	}
}