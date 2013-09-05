package com.brainmote.lookatme;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.brainmote.lookatme.constants.AppSettings;
import com.brainmote.lookatme.service.NotificationType;
import com.brainmote.lookatme.service.Services;
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
			if (!CommonUtils.isWifiConnected(this) && !CommonUtils.isEmulator())
				showDialog(getString(R.string.message_warning), getString(R.string.message_wifi_required));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.nearby, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Verifica quale elemento è stato premuto
		switch (item.getItemId()) {
		case R.id.action_force_refresh:
			Services.businessLogic.refreshSocialList();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Cancello le notifiche di like
		Services.notification.clearLocalNotifications(NotificationType.SOMEONE_LIKED_YOU);
		Services.notification.clearLocalNotifications(NotificationType.PERFECT_MATCH);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}
		doubleBackToExitPressedOnce = true;
		Toast.makeText(this, getString(R.string.click_back_again_to_exit), Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, AppSettings.DOUBLE_BACK_TO_REALLY_EXIT_DELAY);
	}
}