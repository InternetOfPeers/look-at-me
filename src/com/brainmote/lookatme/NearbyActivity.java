package com.brainmote.lookatme;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.brainmote.lookatme.constants.AppSettings;
import com.brainmote.lookatme.service.NotificationType;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.CommonUtils;
import com.brainmote.lookatme.util.Log;
import com.samsung.android.sdk.groupplay.SgpGroupPlay;

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
	protected void onResume() {
		super.onResume();
		try {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.fragment_nearby_list, null);
			ImageButton button = (ImageButton) v.findViewById(R.id.buttonGroupPlay);
			if (Services.groupPlayManager.isReady()) {
				Log.d("gone");
				button.setVisibility(View.GONE);

			} else {
				Log.d("visibile");
				button.setVisibility(View.VISIBLE);
			}
			button.refreshDrawableState();
			button.invalidate();
		} catch (InflateException e) {
			Log.d("Layout non ancora inizializzato");
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Se l'utente è online, visualizzo il pulsante di refresh
		if (Services.businessLogic.isRunning()) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.nearby, menu);
		}
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

	public void onGroupPlayButtonClick(View view) {
		try {
			new SgpGroupPlay().runGroupPlay();
			Log.d("Group Play installato correttamente.");
		} catch (Exception e) {
			Log.d("Group Play non installato correttamente.");
			// Se non ha installato l'app, viene presentato un dialogo
			// che porta l'utente all'app store
			showDialog("Group Play", "Installa Group Play!", "Installa", null, true, true);
		}
	}

}