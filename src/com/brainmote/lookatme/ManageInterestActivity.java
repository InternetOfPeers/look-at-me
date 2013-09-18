package com.brainmote.lookatme;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;

public class ManageInterestActivity extends CommonActivity {

	private ExpandableListView interestList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_interest);
		initDrawerMenu(savedInstanceState, this.getClass(), false);
		interestList = (ExpandableListView) findViewById(R.id.expandableListInterests);
		interestList.setAdapter(new ManageInterestListAdapter(this));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Verifica quale elemento è stato premuto
		switch (item.getItemId()) {
		case android.R.id.home:
			// Le activity non di root devono gestire l'indietro autonomamente
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
