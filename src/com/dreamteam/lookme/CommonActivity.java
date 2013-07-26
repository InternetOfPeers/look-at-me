package com.dreamteam.lookme;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamteam.lookme.communication.ILookAtMeCommunicationListener;
import com.dreamteam.lookme.navigation.Nav;
import com.dreamteam.lookme.service.CommunicationService;
import com.dreamteam.lookme.service.CommunicationService.CommunicationServiceBinder;
import com.dreamteam.util.Log;

// ILookAtMeCommunicationListener will be implemented by every Activity so this class will be abstract
public abstract class CommonActivity extends Activity implements
		ServiceConnection, ILookAtMeCommunicationListener {

	// Service was placed here because it must be reachable from all activities.
	// It will be started only in first activity onCreate() method.
	protected CommunicationService communicationService;
	// This variable can be valued as CommunicationService.SERVICE_READY_TO_RUN
	// or CommunicationService.SERVICE_RUNNING constants
	protected int serviceState;

	protected DrawerLayout mDrawerLayout;
	protected ListView mDrawerList;
	protected ActionBarDrawerToggle mDrawerToggle;

	protected CharSequence mDrawerTitle;
	protected CharSequence mTitle;
	protected String[] mPlanetTitles;
	protected boolean menuEnabled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Cancella le notifiche appese se l'utente proviene da fuori da un
		// banner di notifica
		Services.notify.clearActivityNotifications(this);
	}
	
	@Override
	protected void onStart() {
		Log.d();
		super.onStart();
		// Start service
		// Multiple requests to start the service result in multiple
		// corresponding calls to the service's onStartCommand(). However,
		// only one request to stop the service (with stopSelf() or
		// stopService()) is required to stop it.
		Intent intentStart = new Intent(CommunicationService.SERVICE_START);
		startService(intentStart);
		// Bind service
		Intent intentBind = new Intent(CommunicationService.SERVICE_BIND);
		bindService(intentBind, this, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onStop() {
		Log.d();
		super.onStop();
		try {
			Log.d("Unbind communication service");
			unbindService(this);
		}
		catch(Exception e) {
			Log.e(e.getMessage());
		}
	}
	
	@Override
	protected void onResume() {
		Log.d();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Log.d();
		super.onPause();
	}
	
	@Override
	protected void onRestart() {
		Log.d();
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		Log.d();
		super.onDestroy();
	}

	// START ServiceConnection implementation
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.d();
		CommunicationServiceBinder binder = (CommunicationServiceBinder) service;
		communicationService = binder.getService();
		serviceState = communicationService.initialize(this, this);
	}
	
	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.d();
		communicationService.stop();
		communicationService = null;
	}
	// END ServiceConnection implementation
	
	protected void stopService() {
		if (communicationService != null) {
			unbindService(this);
			Intent intent = new Intent(CommunicationService.SERVICE_STOP);
			stopService(intent);
		}
	}
	
	protected void closeApplication() {
		stopService();
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		this.startActivity(intent);
		finish();
	}

	protected void initMenu() {
		menuEnabled = true;
		mTitle = mDrawerTitle = getTitle();
		mPlanetTitles = getResources().getStringArray(R.array.menu_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	protected void setMenuItem(int position) {
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "Exiting app", Toast.LENGTH_LONG).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	protected class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	protected void selectItem(int position) {
		if (menuEnabled) {
			// update selected item and title, then close the drawer
			// mDrawerList.setItemChecked(position, true);
			// setTitle(mPlanetTitles[position]);
			// mDrawerLayout.closeDrawer(mDrawerList);
			menuEnabled = false;
			Nav.startActivity(this, Nav.getActivityFromMenuPosition(position));
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Notifica un messaggio di tipo chat all'utente
	 * 
	 * @param fromName
	 * @param messageAbstract
	 */
	protected void notifyChatMessage(String fromName, String messageAbstract) {
		Services.notify.chatMessage(this, fromName, messageAbstract);
	}

	/**
	 * Notifica l'utente quando riceve un mi piace
	 * 
	 * @param fromName
	 */
	protected void notifyLike(String fromName) {
		Services.notify.like(this, fromName);
	}

	/**
	 * Notifica l'utente quando avviene un match perfetto tra il suo profilo e
	 * quello di un'altro utente
	 * 
	 * @param fromName
	 */
	protected void notifyPerfectMatch(String fromName) {
		Services.notify.perfectMatch(this, fromName);
	}

	/**
	 * Notifica l'utente quando il suo profilo viene visualizzato
	 * 
	 * @param fromName
	 */
	protected void notifyProfileView(String fromName) {
		Services.notify.profileView(this, fromName);
	}
}
