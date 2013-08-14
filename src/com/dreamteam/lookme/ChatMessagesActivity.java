package com.dreamteam.lookme;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.dreamteam.lookme.service.NotificationService;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.Nav;

public class ChatMessagesActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_messages);
		initDrawerMenu(savedInstanceState, this.getClass(), false);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.chat_conversations, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Verifica quale elemento è stato premunto
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.action_view_profile:
			Bundle parameters = new Bundle();
			parameters.putString(Nav.PROFILE_ID_KEY, getConversation().getNodeId());
			Nav.startActivityWithParameters(this, ProfileActivity.class, parameters);
			break;
		}
		return true;
	}

	public ChatConversation getConversation() {
		String conversationId = Nav.getParameters(this).getString(NotificationService.CONVERSATION_KEY_ID);
		if (conversationId.isEmpty())
			return null;
		return Services.currentState.getConversationsStore().get(conversationId);
	}

}
