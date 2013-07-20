package com.dreamteam.lookme;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.dreamteam.lookme.communication.ILookAtMeCommunicationListener;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.lookme.error.LookAtMeException;
import com.dreamteam.lookme.service.CommunicationService;
import com.dreamteam.lookme.service.CommunicationService.CommunicationServiceBinder;
import com.dreamteam.util.Log;

public class SocialActivity extends Activity {

	private static final String SERVICE_PREFIX = "com.dreamteam.lookme.service.CommunicationService.";

	private CommunicationService communicationService;

	private SocialListFragment socialListFragment;
	// private int currentFragment;
	private FragmentTransaction fragmentTransaction;

	// private ProgressDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social);
		// set list fragment
		socialListFragment = (SocialListFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_list);
		socialListFragment.setActivity(this);
		setFragment(SocialListFragment.SOCIAL_LIST_FRAGMENT);

		// Controllo che l'utente abbia compilato almeno i campi obbilgatori del
		// profilo
		if (DBOpenHelperImpl.getInstance(this).isProfileCompiled()) {
			// Start service
			Intent intentStart = new Intent(SERVICE_PREFIX + "SERVICE_START");
			startService(intentStart);
			if (communicationService == null) {
				Intent intentBind = new Intent(SERVICE_PREFIX + "SERVICE_BIND");
				bindService(intentBind, serviceConnection,
						Context.BIND_AUTO_CREATE);
			}
		} else {
			// L'utente deve compilare il profilo prima di iniziare
			Log.d("It's the first time this app run!");
			showDialog();
		}
	}

	@Override
	protected void onResume() {
		Log.d();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		Log.d();
		super.onDestroy();
		if (communicationService != null) {
			communicationService.stop();
			unbindService(serviceConnection);
		}
		communicationService = null;
		Intent intent = new Intent(SERVICE_PREFIX + "SERVICE_STOP");
		stopService(intent);
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d();
			communicationService.stop();
			communicationService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d();
			CommunicationServiceBinder binder = (CommunicationServiceBinder) service;
			communicationService = binder.getService();
			communicationService.initialize(SocialActivity.this,
					new ILookAtMeCommunicationListener() {

						@Override
						public void onSocialNodeLeft(String nodeName) {
							Log.d();
							// remove node from socialNodeMap
							socialListFragment.removeSocialNode(nodeName);
							socialListFragment.refreshFragment();
						}

						@Override
						public void onSocialNodeJoined(LookAtMeNode node) {
							Log.d();
							// add node to socialNodeMap
							socialListFragment.putSocialNode(node);
							socialListFragment.refreshFragment();
						}

						@Override
						public void onCommunicationStopped() {
							Log.d("NOT IMPLEMENTED");
							// TODO Auto-generated method stub

						}

						@Override
						public void onCommunicationStarted() {
							Log.d("NOT IMPLEMENTED");
							// TODO Auto-generated method stub

						}

						@Override
						public void onSocialNodeProfileReceived(
								LookAtMeNode node) {
							Log.d("NOT IMPLEMETED");
							// stop loading
							socialListFragment.dismissLoadingDialog();
							// TODO got to profile fragment
							Toast.makeText(
									getApplicationContext(),
									node.getProfile().getNickname()
											+ " ARRIVED!", Toast.LENGTH_LONG)
									.show();

						}

						@Override
						public void onLikeReceived(String nodeFrom) {
							Log.d("NOT IMPLEMENTED");
							// TODO Auto-generated method stub

						}

						@Override
						public void onChatMessageReceived(String nodeFrom,
								String message) {
							Log.d("NOT IMPLEMENTED");
							// TODO Auto-generated method stub

						}

						@Override
						public void onSocialNodeUpdated(LookAtMeNode node) {
							Log.d();
							// update node in socialNodeMap
							socialListFragment.putSocialNode(node);
							socialListFragment.refreshFragment();
						}
					});
			try {
				socialListFragment
						.setCommunicationService(communicationService);
				communicationService.start();
			} catch (LookAtMeException e) {
				Log.d("communicationService.start() throws LookAtMeException");
				e.printStackTrace();
			}
		}
	};

	private void setFragment(int currentFragment) {
		// this.currentFragment = currentFragment;
		this.fragmentTransaction = getFragmentManager().beginTransaction();
		this.fragmentTransaction.show(socialListFragment);
		this.fragmentTransaction.commit();
	}

	private void showDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.first_time_dialog);
		dialog.setTitle("Dialog popup");

		Button dialogButton = (Button) dialog.findViewById(R.id.buttonBegin);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent registerIntent = new Intent(SocialActivity.this,
						ProfileActivity.class);
				SocialActivity.this.startActivity(registerIntent);
				SocialActivity.this.finish();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

}