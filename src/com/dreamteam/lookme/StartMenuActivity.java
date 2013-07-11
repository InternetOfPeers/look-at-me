package com.dreamteam.lookme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartMenuActivity extends Activity {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_menu);

	}

	public void showLogin(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);

	}

	public void showTestChord(View view) {
		Intent intent = new Intent(this, TestChordActivity.class);
		startActivity(intent);

	}

	public void exit() {

	}

}