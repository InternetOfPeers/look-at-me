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
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.lookme.bean.Profile;
import com.dreamteam.lookme.communication.ILookAtMeCommunicationListener;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.lookme.error.LookAtMeException;
import com.dreamteam.lookme.service.CommunicationService;
import com.dreamteam.lookme.service.CommunicationService.CommunicationServiceBinder;

public class SocialListActivity extends Activity implements OnItemClickListener, OnClickListener {
	
	private static final String TAG = "LOOKATME_ACTIVITY";
	private static final String TAGClass = "[SocialListActivity]";
	
	private static final String SERVICE_PREFIX = "com.dreamteam.lookme.service.CommunicationService.";
	
	private CommunicationService communicationService;
	
	private ListView socialListView;
	private TextView myNickText;
	private SocialListAdapter socialListAdapter;
	private Button refreshListButton;
	
	private Map<String, LookAtMeNode> socialNodeMap;
	// the BaseAdapter class uses as items id a long type, so while
	// lookAtMe app identify nodes with String it's necessary a map to match
	// profileId - nodeId and operate on socialNodeMap.
	private Map<Long, String> socialNodeIdMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, TAGClass + " : " + "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social_list);
		
		// init data structures
		socialNodeMap = new HashMap<String, LookAtMeNode>();
		socialNodeIdMap = new HashMap<Long, String>();
		
		// Start service
		Intent intentStart = new Intent(SERVICE_PREFIX + "SERVICE_START");
        startService(intentStart);
        if (communicationService == null) {
            Intent intentBind = new Intent(SERVICE_PREFIX + "SERVICE_BIND");
            bindService(intentBind, serviceConnection, Context.BIND_AUTO_CREATE);
        }
		
		// set a profile to skip registration step (will be deleted)
		Profile myProfile = new Profile();
		long currentMillis = System.currentTimeMillis();
		//myProfile.setId(currentMillis);
		String currentMillisStr = currentMillis+"";
		String name = "nome"+currentMillisStr.substring(currentMillisStr.length()-3);
		String surname = "cognome"+currentMillisStr.substring(currentMillisStr.length()-3);
		String nickName = "nick"+currentMillisStr.substring(currentMillisStr.length()-3);
		myProfile.setName(name);
		myProfile.setSurname(surname);
		myProfile.setNickname(nickName);
		DBOpenHelper dbOpenHelper = DBOpenHelperImpl.getInstance(this);
		try {
			dbOpenHelper.saveOrUpdateProfile(myProfile);
		} catch (Exception e) {
			Log.d("DBOpenHelper", "failed to save myProfile");
			e.printStackTrace();
		}
		
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
		Log.d(TAG, TAGClass + " : " + "onResume");
        super.onResume();
        socialListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
    	Log.d(TAG, TAGClass + " : " + "onDestroy");
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
			Log.d(TAG, TAGClass + "[ServiceConnection] : onServiceDisconnected");
			communicationService.stop();
			communicationService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, TAGClass + "[ServiceConnection] : onServiceConnected");
			CommunicationServiceBinder binder = (CommunicationServiceBinder) service;
			communicationService = binder.getService();
			communicationService.initialize(SocialListActivity.this, new ILookAtMeCommunicationListener() {
				
				@Override
				public void onSocialNodeLeft(String nodeName) {
					Log.d(TAG, TAGClass + "[ServiceConnection][ILookAtMeCommunicationListener] : onSocialNodeLeft");
					// get profile id corresponding to nodeName
					LookAtMeNode node = socialNodeMap.get(nodeName);
					long profileId = node.getProfile().getId();
					// remove node from socialNodeMap 
					socialNodeMap.remove(nodeName);
					// remove entry from profileNodeMap
					socialNodeIdMap.remove(Long.valueOf(profileId));
					// update GUI calling a GUI listener
					socialListAdapter.notifyDataSetChanged();
					
				}
				
				@Override
				public void onSocialNodeJoined(LookAtMeNode node) {
					Log.d(TAG, TAGClass + "[ServiceConnection][ILookAtMeCommunicationListener] : onSocialNodeJoined");
					if (node == null) {
						Toast.makeText(getApplicationContext(), "NULL Node OBJECT ARRIVED!", Toast.LENGTH_LONG).show();
						return;
					}
					// add node to socialNodeMap
					socialNodeMap.put(node.getId(), node);
					// add entry in profileNodeMap
					socialNodeIdMap.put(node.getProfile().getId(), node.getId());
					// update GUI calling a GUI listener
					socialListAdapter.notifyDataSetChanged();
					
				}
				
				@Override
				public void onCommunicationStopped() {
					Log.d(TAG, TAGClass + "[ServiceConnection][ILookAtMeCommunicationListener] : onCommunicationStopped NOT IMPLEMENTED");
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onCommunicationStarted() {
					Log.d(TAG, TAGClass + "[ServiceConnection][ILookAtMeCommunicationListener] : onCommunicationStarted NOT IMPLEMENTED");
					// TODO Auto-generated method stub
					
				}
			});
			try {
				communicationService.start();
			} catch (LookAtMeException e) {
				Log.d(TAG, TAGClass + "[ServiceConnection] : communicationService start() throws LookAtMeException");
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onClick(View arg0) {
		Log.d(TAG, TAGClass + " : " + "onClick");
		communicationService.refreshSocialList();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.d(TAG, TAGClass + " : " + "onItemClick");
		// arg2 = the position of the item in our view (List/Grid) that we clicked
		// arg3 = the id of the item that we have clicked
		String nodeId = socialNodeIdMap.get(arg3);
		LookAtMeNode node = socialNodeMap.get(nodeId);
		String message = "DELETING NODE \"" + node.getId() + "\". ITS NAME IS \"" + node.getProfile().getName() + " " + node.getProfile().getSurname() + "\"";
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		socialNodeIdMap.remove(arg3);
		socialNodeMap.remove(nodeId);
		socialListAdapter.notifyDataSetChanged();
	}
	
	public class SocialListAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return socialNodeMap.values().size();
		}

		@Override
		public Object getItem(int arg0) {
			List<LookAtMeNode> nodeList = new ArrayList<LookAtMeNode>(socialNodeMap.values());
			LookAtMeNode node = (LookAtMeNode) nodeList.get(arg0);
			return node.getProfile();
		}

		@Override
		public long getItemId(int arg0) {
			Profile profile = (Profile) this.getItem(arg0);
			return profile.getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				// LayoutInflater class is used to instantiate layout XML file into its corresponding View objects.
				LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.one_row_social_list, null);
			}

			TextView nickNameText = (TextView) convertView.findViewById(R.id.nickNameText);
			nickNameText.setText(((Profile)this.getItem(position)).getNickname());

			return convertView;
		}
	}
}