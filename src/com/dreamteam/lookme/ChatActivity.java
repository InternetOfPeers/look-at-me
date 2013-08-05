package com.dreamteam.lookme;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.dreamteam.util.Log;

public class ChatActivity extends CommonActivity {

	// public static final int SOCIAL_PROFILE_FRAGMENT = 2002;
	public static final int CHAT_LIST_FRAGMENT = 2004;

	private ChatListFragment messageListFragment;
	private int currentFragment;
	private FragmentTransaction chatListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages_chat);
		// set fragment
		messageListFragment = (ChatListFragment) getFragmentManager().findFragmentById(R.id.fragment_chat_list);
		setFragment(CHAT_LIST_FRAGMENT);
		// Inizializzazione del menu
		initMenu(savedInstanceState, this.getClass());
	}

	@Override
	public void onBackPressed() {
		Log.d();
		// if (currentFragment == MESSAGE_LIST_FRAGMENT) {
		Intent registerIntent = new Intent(this, ChatActivity.class);
		this.startActivity(registerIntent);
		this.finish();
		// }
	}

	private void setFragment(int fragment) {
		Log.d("" + fragment);
		if (currentFragment != fragment) {
			Log.d("changing fragment from " + currentFragment + " to " + fragment);
			currentFragment = fragment;
			chatListFragment = getFragmentManager().beginTransaction();
			switch (fragment) {
			case CHAT_LIST_FRAGMENT:
				chatListFragment.show(messageListFragment);
				break;
			default:
				chatListFragment.show(messageListFragment);
				break;
			}
			this.chatListFragment.commit();
		}
	}

}
