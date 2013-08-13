package com.dreamteam.lookme;

import android.os.Bundle;

public class ChatConversationsActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_conversations);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
	}

}
