package com.dreamteam.lookme;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;

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
		// Imposta l'icona per tornare al livello precedente
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// Imposta il fragment
		messageListFragment = (ChatListFragment) getFragmentManager().findFragmentById(R.id.fragment_chat_list);
		setFragment(CHAT_LIST_FRAGMENT);
		// Inizializzazione del menu
		initMenu(savedInstanceState, this.getClass());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Verifica quale elemento è stato premunto
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				// This activity is NOT part of this app's task, so create a new
				// task when navigating up, with a synthesized back stack.
				TaskStackBuilder.create(this)
				// Add all of this activity's parents to the back stack
						.addNextIntentWithParentStack(upIntent)
						// Navigate up to the closest parent
						.startActivities();
			} else {
				// This activity is part of this app's task, so simply
				// navigate up to the logical parent activity.
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
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
