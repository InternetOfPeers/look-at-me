package com.dreamteam.lookme;

import android.app.Activity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.dreamteam.util.Log;

public class SettingsActivity extends CommonActivity {

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		initMenu();

		// Verifica lo stato del servizio

		Switch button = (Switch) findViewById(R.id.btn_toggle_communication_service);
		button.setChecked(Services.communication.isRunning());
		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean enable) {
				if (enable) {
					// Start del servizio di comunicazione
					Services.communication.start(getInstance());
				} else {
					// Stop del servizio di comunicazione
					Services.communication.stop(getInstance());
				}
			}
		});
	};

	private Activity getInstance() {
		return this;
	}

}
