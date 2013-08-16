package com.dreamteam.lookme;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;

public class NearbyActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
		// Controllo che l'utente abbia compilato almeno i campi obbilgatori
		// del profilo
		if (Services.currentState.getMyBasicProfile() == null) {
			// L'utente deve compilare il profilo prima di iniziare
			Log.d("It's the first time this app run!");
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.first_time_dialog);
			dialog.setTitle("First launch");
			Button dialogButton = (Button) dialog.findViewById(R.id.buttonBegin);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Nav.startActivity(NearbyActivity.this, EditProfileActivity.class);
					dialog.dismiss();
				}
			});
			dialog.show();
		} else {
			// Verifico lo stato della connessione
			if (!isConnectionAvailable() && !CommonUtils.isEmulator())
				showDialog("Warning", "No WiFi connection available. Connect to a WiFi hotspot to find new friends nearby.", false);

		}
	}
}