package com.dreamteam.lookme;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.dreamteam.util.Log;

public class MessagesActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages_conversations);
		// Imposto il fragment da utilizzare
		// TODO Secondo me qui un fragment non serve, ma per ora teniamolo dato
		// che è già stato fatto così
//		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//		fragmentTransaction.add(R.id.fragment_message_list, new MessagesListFragment());
//		fragmentTransaction.commit();
		// Inizializzazione del menu
		initMenu(savedInstanceState, this.getClass());
	}

}
