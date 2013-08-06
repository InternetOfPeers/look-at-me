package com.dreamteam.lookme;

import android.os.Bundle;

import com.dreamteam.util.Log;

public class MessagesActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages_conversations);
		// Inizializzazione del menu
		initMenu(savedInstanceState, this.getClass());
	}

}
