package com.dreamteam.lookme;

import android.os.Bundle;

public class ConversationsActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversations);
		initDrawerMenu(savedInstanceState, this.getClass());
	}

}
