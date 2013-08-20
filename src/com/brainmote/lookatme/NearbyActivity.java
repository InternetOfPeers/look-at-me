package com.brainmote.lookatme;

import android.os.Bundle;

import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.CommonUtils;

public class NearbyActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
		if (Services.businessLogic.isMyProfileComplete()) {
			// Verifico lo stato della connessione
			if (!isConnectionAvailable() && !CommonUtils.isEmulator())
				showDialog(getString(R.string.message_warning), getString(R.string.message_wifi_required), false);
		} else {
			showFirstTimeDialog();
		}

	}
}