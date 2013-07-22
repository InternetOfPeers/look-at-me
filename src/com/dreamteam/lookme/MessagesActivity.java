package com.dreamteam.lookme;

import android.os.Bundle;

public class MessagesActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages_conversations);
		initMenu();
		if (savedInstanceState == null) {
			setMenuItem(2);
		}
	}
}
