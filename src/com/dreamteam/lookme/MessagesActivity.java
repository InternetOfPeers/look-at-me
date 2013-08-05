package com.dreamteam.lookme;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.dreamteam.util.Log;

public class MessagesActivity extends CommonActivity {

	// public static final int SOCIAL_PROFILE_FRAGMENT = 2002;
	public static final int MESSAGE_LIST_FRAGMENT = 2001;

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
		initMenu(savedInstanceState, this.getClass());
	}

	@Override
	public void onBackPressed() {
		Log.d();
		// if (currentFragment == MESSAGE_LIST_FRAGMENT) {
		Intent registerIntent = new Intent(this, MessagesActivity.class);
		this.startActivity(registerIntent);
		this.finish();
		// }
	}

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
}
