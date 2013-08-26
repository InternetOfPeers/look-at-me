package com.brainmote.lookatme;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.Log;

public class SettingsActivity extends CommonActivity {

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
		checkProfileCompleted();

		// Verifica lo stato del servizio
		Switch button = (Switch) findViewById(R.id.btn_toggle_communication_service);
		button.setChecked(Services.businessLogic.isRunning());
		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean enable) {
				if (enable) {
					// Start del servizio di comunicazione
					Services.businessLogic.start(getApplicationContext());
				} else {
					// Stop del servizio di comunicazione
					Services.businessLogic.stop(getApplicationContext());
					Services.currentState.reset();
				}
			}
		});
	};

}
