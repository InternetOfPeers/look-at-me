package com.dreamteam.lookme;

import android.os.Bundle;

import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.util.Log;

public class MessagesActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages_conversations);
		initMenu();
		if (savedInstanceState == null) {
			setMenuItem(2);
		}
	}

	// START ILookAtMeCommunicationListener implementation
	@Override
	public void onSocialNodeLeft(String nodeName) {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub
	}

	@Override
	public void onSocialNodeJoined(LookAtMeNode node) {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub
	}

	@Override
	public void onCommunicationStopped() {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub

	}

	@Override
	public void onCommunicationStarted() {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub

	}

	@Override
	public void onSocialNodeProfileReceived(LookAtMeNode node) {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub
	}

	@Override
	public void onLikeReceived(String nodeFrom) {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub
	}

	@Override
	public void onChatMessageReceived(String nodeFrom, String message) {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub

	}

	@Override
	public void onSocialNodeUpdated(LookAtMeNode node) {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub
	}
	// END ILookAtMeCommunicationListener implementation
}
