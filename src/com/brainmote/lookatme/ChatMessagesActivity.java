package com.brainmote.lookatme;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
		// Se l'utente è online, visualizzo il pulsante del profilo utente
		if (Services.businessLogic.isRunning()) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.chat_conversations, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Verifica quale elemento è stato premuto
		switch (item.getItemId()) {
		case android.R.id.home:
			// Le activity non di root devono gestire l'indietro autonomamente
			onBackPressed();
			return true;
		case R.id.action_view_profile:
			String profileId = Services.businessLogic.getProfileIdFromConversationId(getConversation().getId());
			String nodeId = Services.currentState.getSocialNodeMap().getNodeIdByProfileId(profileId);
			// Affinché l'utente possa visitare il profilo, verifico che il nodo
			// sia ancora disponibile oppure che sia il nodo di un utente fake
			if (Services.businessLogic.isNodeAlive(nodeId) || Services.businessLogic.isFakeUserNode(nodeId)) {
				Bundle parameters = new Bundle();
				parameters.putString(Nav.NODE_KEY_ID, nodeId);
				Nav.startActivityWithParameters(this, ProfileActivity.class, parameters);
			} else {
				// L'utente non è più online e non posso vederne il profilo
				showDialog(getString(R.string.message_warning), getString(R.string.user_is_not_nearby_anymore));
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
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