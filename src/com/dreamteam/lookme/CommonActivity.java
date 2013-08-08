package com.dreamteam.lookme;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;

public abstract class CommonActivity extends Activity {

	// public static Profile myProfile;
	protected DrawerLayout mDrawerLayout;
	protected ListView mDrawerList;
	protected ActionBarDrawerToggle mDrawerToggle;

	protected CharSequence mDrawerTitle;
	protected CharSequence mTitle;
	protected String[] mPlanetTitles;
	protected boolean menuEnabled;
	protected Bundle extras;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d();
		// Preparo gli eventuali extra passati da chi ha chiamato l'activity
		extras = getIntent().getExtras() != null ? getIntent().getExtras() : new Bundle();
		// Cancella le notifiche appese se l'utente proviene da fuori l'app ed a
		// premuto su un banner di notifica
		Services.notification.clearActivityNotifications(this);
	}

	protected void initDrawerMenu(Bundle savedInstanceState, Class<? extends Activity> activityClass) {
		menuEnabled = true;
		mTitle = mDrawerTitle = getTitle();
		mPlanetTitles = getResources().getStringArray(R.array.menu_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
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
		// Imposta la selezione del menu item corrente in base all'activity
		if (savedInstanceState == null) {
			int position = Nav.getMenuPositionFromActivityClass(activityClass);
			if (position >= 0)
				setMenuItem(position);
		}
	}

	protected void setMenuItem(int position) {
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.common, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mDrawerLayout != null) {
			// If the nav drawer is open, hide action items related to the
			// content view
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
			menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Verifica se è stata premuta l'icona del drawer o il drawer in
		// generale, e gestisce l'azione di conseguenza
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Verifica quale elemento è stato premunto
		switch (item.getItemId()) {
		case R.id.action_settings:
			Nav.startActivity(this, SettingsActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	protected class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
		if (mDrawerToggle != null)
			mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggle
		if (mDrawerToggle != null)
			mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		// Verifico se presente un parent
		Intent upIntent = NavUtils.getParentActivityIntent(this);
		if (upIntent != null) {
			// Verifico se viene da fuori dall'app (esempio da una notifica di
			// sistema) oppure dall'interno
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				// This activity is NOT part of this app's task, so create a new
				// task when navigating up, with a synthesized back stack.
				TaskStackBuilder.create(this)
				// Add all of this activity's parents to the back stack
						.addNextIntentWithParentStack(upIntent)
						// Navigate up to the closest parent
						.startActivities();
			} else {
				// This activity is part of this app's task, so simply
				// navigate up to the logical parent activity.
				NavUtils.navigateUpTo(this, upIntent);
			}
		}
	};

	protected void showErrorDialog(String message) {
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
			}
		});
		dialog.show();
	}
}
