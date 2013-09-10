package com.brainmote.lookatme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.brainmote.lookatme.util.Log;

public class ProfileActivity extends CommonActivity {
	
	private ProfileFragment profileFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		profileFragment = (ProfileFragment) getFragmentManager().findFragmentById(R.id.fragment_profile);
		initDrawerMenu(savedInstanceState, this.getClass(), false);
		setTitle("");
		checkIfProfileIsCompleted();
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
	
	public void onInterestsButtonClick(View view) {
		profileFragment.toggleInterests();
	}
}