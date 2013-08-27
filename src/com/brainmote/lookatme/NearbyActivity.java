package com.brainmote.lookatme;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.brainmote.lookatme.constants.AppSettings;
import com.brainmote.lookatme.util.CommonUtils;

public class NearbyActivity extends CommonActivity {

	private boolean doubleBackToExitPressedOnce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
		if (checkIfProfileIsCompleted()) {
			// Verifico lo stato della connessione
			if (!isConnectionAvailable() && !CommonUtils.isEmulator())
				showDialog(getString(R.string.message_warning), getString(R.string.message_wifi_required));
		}
	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, getString(R.string.click_back_again_to_exit), Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, AppSettings.DOUBLE_BACK_TO_REALLY_EXIT_DELAY);
	}
}