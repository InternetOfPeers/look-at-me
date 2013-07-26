package com.dreamteam.lookme;

import android.app.Activity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.dreamteam.lookme.communication.LookAtMeNode;
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

	@Override
	public void onCommunicationStarted() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCommunicationStopped() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSocialNodeJoined(LookAtMeNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSocialNodeLeft(String nodeName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSocialNodeProfileReceived(LookAtMeNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLikeReceived(String nodeFrom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChatMessageReceived(String nodeFrom, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSocialNodeUpdated(LookAtMeNode node) {
		// TODO Auto-generated method stub

	}

	private Activity getInstance() {
		return this;
	}

}
