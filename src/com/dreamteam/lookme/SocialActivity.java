package com.dreamteam.lookme;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.dreamteam.lookme.communication.ILookAtMeCommunicationListener;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.lookme.error.LookAtMeException;
import com.dreamteam.lookme.service.CommunicationService;
import com.dreamteam.lookme.service.CommunicationService.CommunicationServiceBinder;
import com.dreamteam.util.Log;

public class SocialActivity extends CommonActivity {

	private static final String SERVICE_PREFIX = "com.dreamteam.lookme.service.CommunicationService.";

	public static final int SOCIAL_PROFILE_FRAGMENT = 1002;
	public static final int SOCIAL_LIST_FRAGMENT = 1001;

	private CommunicationService communicationService;

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
		initMenu();
		if (savedInstanceState == null) {
			setMenuItem(1);
		}
		// Controllo che l'utente abbia compilato almeno i campi obbilgatori del
		// profilo
		if (DBOpenHelperImpl.getInstance(this).isProfileCompiled()) {
			// Start service
			// Multiple requests to start the service result in multiple
			// corresponding calls to the service's onStartCommand(). However,
			// only one request to stop the service (with stopSelf() or
			// stopService()) is required to stop it.
			Intent intentStart = new Intent(SERVICE_PREFIX + "SERVICE_START");
			startService(intentStart);
			if (communicationService == null) {
				Intent intentBind = new Intent(SERVICE_PREFIX + "SERVICE_BIND");
				bindService(intentBind, serviceConnection, Context.BIND_AUTO_CREATE);
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
		// if (communicationService != null) {
		// communicationService.stop();
		// unbindService(serviceConnection);
		// }
		// communicationService = null;
		// Intent intent = new Intent(SERVICE_PREFIX + "SERVICE_STOP");
		// stopService(intent);
	}

	// overridato metodo di selezione dal menù per permettere la chiusura
	// dell'app. Andrà molto probabilmente eliminato
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (communicationService != null && item.getItemId() == R.id.action_settings) {
			communicationService.stop();
			unbindService(serviceConnection);
			communicationService = null;
			Intent intent = new Intent(SERVICE_PREFIX + "SERVICE_STOP");
			stopService(intent);
		}
		return super.onOptionsItemSelected(item);
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
			int serviceState = communicationService.initialize(SocialActivity.this, new ILookAtMeCommunicationListener() {

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
				public void onSocialNodeProfileReceived(LookAtMeNode node) {
					Log.d();
					// stop loading
					socialListFragment.dismissLoadingDialog();
					socialProfileFragment.setProfileNode(node);
					setFragment(SOCIAL_PROFILE_FRAGMENT);
				}

				@Override
				public void onLikeReceived(String nodeFrom) {
					Log.d();
					socialListFragment.addLiked(nodeFrom);
					socialListFragment.refreshFragment(); // to update GUI
					String nodeNickname = socialListFragment.getNicknameOf(nodeFrom);
					if (nodeNickname != null) {
						SocialActivity.this.notifyLike(nodeNickname);
					}
				}

				@Override
				public void onChatMessageReceived(String nodeFrom, String message) {
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
				socialListFragment.setCommunicationService(communicationService);
				socialProfileFragment.setCommunicationService(communicationService);
				if (serviceState == CommunicationService.SERVICE_READY_TO_RUN) {
					Log.d("service is ready to run");
					communicationService.start();
				} else {
					Log.d("service was already started");
				}
			} catch (LookAtMeException e) {
				Log.d("communicationService.start() throws LookAtMeException: " + e.getMessage());
				e.printStackTrace();
				showErrorDialog(e.getMessage());

			}
		}
	};

	private void setFragment(int fragment) {
		Log.d("changing fragment from " + currentFragment + " to " + fragment);
		if (currentFragment != fragment) {
			currentFragment = fragment;
			fragmentTransaction = getFragmentManager().beginTransaction();
			switch (fragment) {
			case SOCIAL_LIST_FRAGMENT:
				fragmentTransaction.show(socialListFragment);
				fragmentTransaction.hide(socialProfileFragment);
				break;
			case SOCIAL_PROFILE_FRAGMENT:
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

	private void showDialog() {
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

	private void showErrorDialog(String message) {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.error_dialog);
		dialog.setTitle("Dialog popup");

		Button dialogButton = (Button) dialog.findViewById(R.id.buttonClose);
		TextView errorMsg = (TextView) dialog.findViewById(R.id.textErrorMsg);
		errorMsg.setText(message);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				SocialActivity.this.startActivity(intent);
				finish();
			}
		});

		dialog.show();
	}

}