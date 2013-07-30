package com.dreamteam.lookme;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.util.Log;

public class MessagesActivity extends CommonActivity {

	public static final int SOCIAL_PROFILE_FRAGMENT = 1002;
	public static final int MESSAGE_LIST_FRAGMENT = 1001;

	private MessageListFragment messageListFragment;	
	private int currentFragment;
	private FragmentTransaction fragmentTransaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages_conversations);
		// set fragment
		messageListFragment = (MessageListFragment) getFragmentManager().findFragmentById(R.id.fragment_message_list);
		setFragment(MESSAGE_LIST_FRAGMENT);
		// Inizializzazione del menu
		initMenu();
		if (savedInstanceState == null) {
			setMenuItem(1);
		}
		// Controllo che l'utente abbia compilato almeno i campi obbilgatori del
		// profilo
		if (!DBOpenHelperImpl.getInstance(this).isProfileCompiled()) {
			// L'utente deve compilare il profilo prima di iniziare
			Log.d("It's the first time this app run!");
		}
	}

	@Override
	public void onBackPressed() {
		Log.d();
		if (currentFragment == MESSAGE_LIST_FRAGMENT) {
			// TODO: come si fa a simulare il pulsante HOME?
		} else if (currentFragment == SOCIAL_PROFILE_FRAGMENT) {
			setFragment(MESSAGE_LIST_FRAGMENT);
		}
	}

	// START ILookAtMeCommunicationListener implementation
	@Override
	public void onSocialNodeLeft(String nodeName) {
		Log.d();
	}

	@Override
	public void onSocialNodeJoined(LookAtMeNode node) {
		Log.d();
		// add node to socialNodeMap
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
		Log.d();
		// stop loading
	}

	@Override
	public void onLikeReceived(String nodeFrom) {
		Log.d();		
	}

	@Override
	public void onChatMessageReceived(String nodeFrom, String message) {
		Log.d("NOT IMPLEMENTED");
		Toast.makeText(getApplicationContext(), nodeFrom + " Want to talk!", Toast.LENGTH_LONG).show();
		// TODO Auto-generated method stub

	}

	@Override
	public void onSocialNodeUpdated(LookAtMeNode node) {
		Log.d();
	}

	// END ILookAtMeCommunicationListener implementation

	private void setFragment(int fragment) {
		Log.d("" + fragment);
		if (currentFragment != fragment) {
			Log.d("changing fragment from " + currentFragment + " to " + fragment);
			currentFragment = fragment;
			fragmentTransaction = getFragmentManager().beginTransaction();
			switch (fragment) {
			case MESSAGE_LIST_FRAGMENT:
				fragmentTransaction.show(messageListFragment);				
				break;
			default:
				fragmentTransaction.show(messageListFragment);
				break;
			}
			this.fragmentTransaction.commit();
		}
	}

	@Override
	public void onChatMessageReceived(LookAtMeNode nodeFrom, String message) {		
		Log.d("NOT IMPLEMENTED");
	}
}
