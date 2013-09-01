package com.brainmote.lookatme;

import android.os.Bundle;

import com.brainmote.lookatme.util.Log;

public class NewEditProfileActivity extends CommonActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_edit_profile);
		initDrawerMenu(savedInstanceState, this.getClass(), false);
		setTitle("");
	}

}
