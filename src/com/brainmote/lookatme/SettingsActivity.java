package com.brainmote.lookatme;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.brainmote.lookatme.constants.AppSettings;
import com.brainmote.lookatme.service.Services;

public class SettingsActivity extends CommonActivity {

	private Menu menu;

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
		checkIfProfileIsCompleted();
		// Verifica lo stato del servizio
		Switch offlineModeSwitch = (Switch) findViewById(R.id.switch_toggle_communication_service);
		offlineModeSwitch.setChecked(!Services.businessLogic.isRunning());
		offlineModeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean enable) {
				if (enable) {
					// Stop del servizio di comunicazione
					Services.businessLogic.stop(getApplicationContext());
					Services.currentState.reset();
					// Mostro il warning sulla barra di menu
					MenuItem menuItem = menu.findItem(R.id.action_offline_mode);
					if (menuItem == null) {
						MenuInflater inflater = getMenuInflater();
						inflater.inflate(R.menu.offline_mode, menu);
						menuItem = menu.findItem(R.id.action_offline_mode);
					}
					menuItem.setVisible(true);
					// Poiché l'utente è già nella schermata dei settings,
					// disabilito l'azione sul pulsante
					menuItem.setEnabled(false);
				} else {
					// Start del servizio di comunicazione
					Services.businessLogic.start(getApplicationContext());
					// Nascondo il warning sulla barra di menu
					MenuItem menuItem = menu.findItem(R.id.action_offline_mode);
					if (menuItem != null) {
						menuItem.setVisible(false);
						// Poiché l'utente è già nella schermata dei settings,
						// disabilito l'azione sul pulsante
						menuItem.setEnabled(false);
					}
				}
			}
		});
		// Imposta il pulsante invoke developers
		Switch invokeDevelopersSwitch = (Switch) findViewById(R.id.switch_toggle_credits);
		invokeDevelopersSwitch.setChecked(getSharedPreferences(AppSettings.USER_PREFERENCES, MODE_PRIVATE).getBoolean(AppSettings.IN_APP_CREDITS, false));
		invokeDevelopersSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				getSharedPreferences(AppSettings.USER_PREFERENCES, MODE_PRIVATE).edit().putBoolean(AppSettings.IN_APP_CREDITS, isChecked).commit();
			}
		});
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Aggiorno la reference al menu dell'activity
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Aggiorno la reference al menu dell'activity
		this.menu = menu;
		return super.onPrepareOptionsMenu(menu);
	}

}
