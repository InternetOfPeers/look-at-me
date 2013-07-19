package com.dreamteam.lookme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.communication.ILookAtMeCommunicationListener;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.lookme.error.LookAtMeException;
import com.dreamteam.lookme.service.CommunicationService;
import com.dreamteam.lookme.service.CommunicationService.CommunicationServiceBinder;
import com.dreamteam.util.Log;

public class SocialListActivity extends Activity implements
		OnItemClickListener, OnClickListener {

	private static final String SERVICE_PREFIX = "com.dreamteam.lookme.service.CommunicationService.";

	private CommunicationService communicationService;

	private ListView socialListView;
	private TextView myNickText;
	private SocialListAdapter socialListAdapter;
	private Button refreshListButton;

	private Map<String, LookAtMeNode> socialNodeMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_list);

		// init data structures
		socialNodeMap = new HashMap<String, LookAtMeNode>();

		// Start service
		Intent intentStart = new Intent(SERVICE_PREFIX + "SERVICE_START");
		startService(intentStart);
		if (communicationService == null) {
			Intent intentBind = new Intent(SERVICE_PREFIX + "SERVICE_BIND");
			bindService(intentBind, serviceConnection, Context.BIND_AUTO_CREATE);
		}

		BasicProfile myProfile = null; // get my profile
		DBOpenHelper dbOpenHelper = DBOpenHelperImpl.getInstance(this);
		try {
			myProfile = dbOpenHelper.getMyBasicProfile();
		} catch (Exception e) {
			Log.e("dbOpenHelper, failed to retrieve my profile");
			e.printStackTrace();
		}
		Log.d("My profile retrieved");

		myNickText = (TextView) findViewById(R.id.myNickText);
		myNickText.setText("You are: " + myProfile.getNickname());

		refreshListButton = (Button) findViewById(R.id.buttonRefreshList);
		refreshListButton.setOnClickListener(this);

		socialListAdapter = new SocialListAdapter();
		socialListView = (ListView) findViewById(R.id.socialListView);
		socialListView.setAdapter(socialListAdapter);
		socialListView.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		Log.d();
		super.onResume();
		socialListAdapter.notifyDataSetChanged();
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
			communicationService.initialize(SocialListActivity.this,
					new ILookAtMeCommunicationListener() {

						@Override
						public void onSocialNodeLeft(String nodeName) {
							Log.d();
							// remove node from socialNodeMap
							socialNodeMap.remove(nodeName);
							// update GUI calling a GUI listener
							socialListAdapter.notifyDataSetChanged();

						}

						@Override
						public void onSocialNodeJoined(LookAtMeNode node) {
							Log.d();
							if (node == null) {
								Toast.makeText(getApplicationContext(),
										"NULL Node OBJECT ARRIVED!",
										Toast.LENGTH_LONG).show();
								return;
							}
							// add node to socialNodeMap
							socialNodeMap.put(node.getId(), node);
							// update GUI calling a GUI listener
							socialListAdapter.notifyDataSetChanged();

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
							Log.d("NOT IMPLEMENTED");
							// TODO Auto-generated method stub
							
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
						public void onSocialNodeUpdated(
								LookAtMeNode node) {
							Log.d("NOT IMPLEMENTED");
							// TODO Auto-generated method stub
							
						}
					});
			try {
				communicationService.start();
			} catch (LookAtMeException e) {
				Log.d("[ServiceConnection] : communicationService start() throws LookAtMeException");
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onClick(View arg0) {
		Log.d();
		communicationService.refreshSocialList();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1,
			int clickedItemPosition, long clickedItemID) {
		Log.d();
		LookAtMeNode node = (LookAtMeNode) socialListAdapter.getItem((int)clickedItemID);
		String message = "SELECTED NODE \"" + node.getId()
				+ "\". ITS NICK IS \"" + node.getProfile().getNickname();
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
				.show();
	}

	public class SocialListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return socialNodeMap.size();
		}

		@Override
		public Object getItem(int arg0) {
			List<LookAtMeNode> nodeList = new ArrayList<LookAtMeNode>(
					socialNodeMap.values());
			LookAtMeNode node = (LookAtMeNode) nodeList.get(arg0);
			return node;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				// LayoutInflater class is used to instantiate layout XML file
				// into its corresponding View objects.
				LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(
						R.layout.one_row_social_list, null);
			}
			
			LookAtMeNode node = (LookAtMeNode) this.getItem(position);
			BasicProfile profile = (BasicProfile) node.getProfile();

			TextView nickNameText = (TextView) convertView
					.findViewById(R.id.nickNameText);
			nickNameText.setText(node.getProfile().getNickname());
			
			// Problemi con il recupero dell'immagine del profilo
			ImageView photoImage = (ImageView) convertView.findViewById(R.id.profilePhotoImage);
			if (profile.getMainProfileImage() == null || profile.getMainProfileImage().getImage() == null) {
				Drawable noPhoto = getResources().getDrawable(R.drawable.no_profile_image);
				photoImage.setImageDrawable(noPhoto);
			}
			else {
				Bitmap bMap = BitmapFactory.decodeByteArray(profile.getMainProfileImage().getImage(), 0, profile.getMainProfileImage().getImage().length - 1);
				photoImage.setImageBitmap(bMap);
			}

			return convertView;
		}
	}
	
}