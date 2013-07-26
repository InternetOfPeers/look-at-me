package com.dreamteam.lookme;

import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.util.Log;

public class SettingsActivity extends CommonActivity {

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		initMenu();
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

}
