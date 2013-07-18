package com.dreamteam.lookme;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.util.Log;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartMenuActivity extends Activity {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_menu);
		

		FullProfile myProfile = null; // get my profile
		DBOpenHelper dbOpenHelper = DBOpenHelperImpl.getInstance(this);
//		try {
//			myProfile = dbOpenHelper.getMyProfile();
//		} catch (Exception e) {
//			Log.e("dbOpenHelper, failed to retrieve my profile");
//			e.printStackTrace();
//		}
		if (myProfile == null) {
			Log.d("It's the first time this app run!");
			showDialog();
		}

	}

	public void showLogin(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);

	}

	public void showListActivity(View view) {
		Intent intent = new Intent(this, SocialListActivity.class);
		startActivity(intent);

	}

	public void exit() {

	}
	
	private void showDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.first_time_dialog);
		dialog.setTitle("Dialog popup");

		Button dialogButton = (Button) dialog.findViewById(R.id.buttonBegin);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent registerIntent = new Intent(StartMenuActivity.this, RegisterActivity.class);
				StartMenuActivity.this.startActivity(registerIntent);
				StartMenuActivity.this.finish();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

}