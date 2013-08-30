package com.brainmote.lookatme;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.brainmote.lookatme.service.NotificationType;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.Nav;

public class ChatMessagesActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_messages);
		initDrawerMenu(savedInstanceState, this.getClass(), false);
		checkIfProfileIsCompleted();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.chat_conversations, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Verifica quale elemento è stato premuto
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.action_view_profile:
			String profileId = Services.businessLogic.getProfileIdFromConversationId(getConversation().getId());
			String nodeId = Services.currentState.getSocialNodeMap().getNodeIdByProfileId(profileId);
			// Verifico che il nodo sia ancora disponibile
			if (Services.businessLogic.isNodeAlive(nodeId)) {
				Bundle parameters = new Bundle();
				parameters.putString(Nav.NODE_KEY_ID, nodeId);
				Nav.startActivityWithParameters(this, ProfileActivity.class, parameters);
			} else {
				// L'utente non è più online e non posso vederne il profilo
				Toast toast = Toast.makeText(this, R.string.user_is_not_nearby_anymore, Toast.LENGTH_SHORT);
				toast.show();
			}
			break;
		}
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Cancello le notifiche di chat
		Services.notification.clearLocalNotifications(NotificationType.CHAT_MESSAGE_RECEIVED);
		return super.onPrepareOptionsMenu(menu);
	}

	public ChatConversation getConversation() {
		String conversationId = Nav.getParameters(this).getString(Nav.CONVERSATION_KEY_ID);
		if (conversationId.isEmpty())
			return null;
		return Services.currentState.getConversationsStore().get(conversationId);
	}

}
