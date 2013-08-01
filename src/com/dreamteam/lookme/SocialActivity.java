package com.dreamteam.lookme;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.util.Log;

public class SocialActivity extends CommonActivity {

	public static final int SOCIAL_PROFILE_FRAGMENT = 1002;
	public static final int SOCIAL_LIST_FRAGMENT = 1001;

	private SocialListFragment socialListFragment;
	private SocialProfileFragment socialProfileFragment;
	private int currentFragment;
	private FragmentTransaction fragmentTransaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social);
		// set fragment
		socialListFragment = (SocialListFragment) getFragmentManager().findFragmentById(R.id.fragment_list);
		socialProfileFragment = (SocialProfileFragment) getFragmentManager().findFragmentById(R.id.fragment_profile);
		setFragment(SOCIAL_LIST_FRAGMENT);
		// Inizializzazione del menu
		initMenu(savedInstanceState, this.getClass());

		// Controllo che l'utente abbia compilato almeno i campi obbilgatori del
		// profilo
		if (!DBOpenHelperImpl.getInstance(this).isProfileCompiled()) {
			// L'utente deve compilare il profilo prima di iniziare
			Log.d("It's the first time this app run!");
			showFirstTimeDialog();
		}
	}

	@Override
	public void onBackPressed() {
		Log.d();
		if (currentFragment == SOCIAL_LIST_FRAGMENT) {
			// TODO: come si fa a simulare il pulsante HOME?
		} else if (currentFragment == SOCIAL_PROFILE_FRAGMENT) {
			setFragment(SOCIAL_LIST_FRAGMENT);
		}
	}

	protected void setFragment(int fragment) {
		Log.d("" + fragment);
		if (currentFragment != fragment) {
			Log.d("changing fragment from " + currentFragment + " to " + fragment);
			currentFragment = fragment;
			fragmentTransaction = getFragmentManager().beginTransaction();
			switch (fragment) {
			case SOCIAL_LIST_FRAGMENT:
				fragmentTransaction.show(socialListFragment);
				fragmentTransaction.hide(socialProfileFragment);
				break;
			case SOCIAL_PROFILE_FRAGMENT:
				socialProfileFragment.setProfileData();
				fragmentTransaction.hide(socialListFragment);
				fragmentTransaction.show(socialProfileFragment);
				break;
			default:
				fragmentTransaction.show(socialListFragment);
				fragmentTransaction.hide(socialProfileFragment);
				break;
			}
			this.fragmentTransaction.commit();
		}
	}

	private void showFirstTimeDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.first_time_dialog);
		dialog.setTitle("Dialog popup");

		Button dialogButton = (Button) dialog.findViewById(R.id.buttonBegin);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent registerIntent = new Intent(SocialActivity.this, ProfileActivity.class);
				SocialActivity.this.startActivity(registerIntent);
				SocialActivity.this.finish();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

}