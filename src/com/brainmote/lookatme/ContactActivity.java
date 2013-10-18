package com.brainmote.lookatme;

import android.os.Bundle;

public class ContactActivity extends CommonActivity {

	private ContactFragment contactFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
		contactFragment = (ContactFragment) getFragmentManager().findFragmentById(R.id.fragment_contact);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.nearby, menu);
	// return super.onCreateOptionsMenu(menu);
	// }
}
