package com.brainmote.lookatme;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.brainmote.lookatme.util.Log;

public class ProfileActivity extends CommonActivity {

	private ProfileFragment profileFragment;
	private boolean favourite = false;
	private MenuItem toggleFavouriteItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		initDrawerMenu(savedInstanceState, this.getClass(), false);
		profileFragment = (ProfileFragment) getFragmentManager().findFragmentById(R.id.fragment_profile);
		checkIfProfileIsCompleted();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Imposta il menù di favorito o meno a seconda dei casi
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_profile, menu);
		toggleFavouriteItem = (MenuItem) menu.findItem(R.id.action_toggle_favourites);
		setFavouriteAction();
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.action_toggle_favourites:
			if (favourite) {
				profileFragment.removeContact();
				setFavourite(false);
				Toast.makeText(getApplicationContext(), R.string.favourite_removed_message, Toast.LENGTH_SHORT).show();
			}
			else {
				profileFragment.saveContact();
				setFavourite(true);
				Toast.makeText(getApplicationContext(), R.string.favourite_added_message, Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
		try {
			setFavouriteAction();
		}
		catch (Exception e) {
			Log.d("Errore nel setting della action bar! Probabilmente il menù ancora non è stato inizializzato");
		}
	}
	
	private void setFavouriteAction() {
		if (favourite) {
			toggleFavouriteItem.setIcon(R.drawable.ic_favourite);
			toggleFavouriteItem.setTitle(R.string.action_remove_favourites);
		}
		else {
			toggleFavouriteItem.setIcon(R.drawable.ic_not_favourite);
			toggleFavouriteItem.setTitle(R.string.action_add_favourites);
		}
	}

	public void onInterestsButtonClick(View view) {
		profileFragment.toggleInterests();
	}
}