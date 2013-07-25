package com.dreamteam.lookme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.service.CommunicationService;
import com.dreamteam.util.Log;

public class SocialListFragment extends Fragment implements OnClickListener, OnItemClickListener {

	private CommunicationService communicationService;

	private Map<String, LookAtMeNode> socialNodeMap;
	
	private Set<String> iLike;
	private Set<String> liked;

	private ListView socialListView;
	private SocialListAdapter socialListAdapter;
	private Button refreshListButton;
	private ProgressDialog loadingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_social_list, null);

		socialNodeMap = new HashMap<String, LookAtMeNode>();
		// init like structures
		iLike = new TreeSet<String>();
		liked = new TreeSet<String>();

		refreshListButton = (Button) view.findViewById(R.id.buttonRefreshList);
		refreshListButton.setOnClickListener(this);

		socialListAdapter = new SocialListAdapter();
		socialListView = (ListView) view.findViewById(R.id.socialListView);
		socialListView.setAdapter(socialListAdapter);
		socialListView.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onClick(View arg0) {
		Log.d();
		communicationService.refreshSocialList();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int clickedItemPosition, long clickedItemID) {
		Log.d();
		LookAtMeNode node = (LookAtMeNode) socialListAdapter.getItem((int) clickedItemID);
		communicationService.sendProfileRequest(node.getId());
		loadingDialog = ProgressDialog.show(this.getActivity(), "Loading", "Please wait...", true);
	}

	public void putSocialNode(LookAtMeNode node) {
		socialNodeMap.put(node.getId(), node);
	}

	public void removeSocialNode(String nodeName) {
		socialNodeMap.remove(nodeName);
	}

	public void refreshFragment() {
		Log.d("socialNodeMap is not null? " + (socialNodeMap != null));
		Log.d("socialNodeMap size is " + socialNodeMap.size());
		Log.d("iLike is not null? " + (iLike != null));
		Log.d("iLike size is " + iLike.size());
		Log.d("liked is not null? " + (liked != null));
		Log.d("liked size is " + liked.size());
		this.socialListAdapter.notifyDataSetChanged();
	}

	public void dismissLoadingDialog() {
		loadingDialog.dismiss();
	}

	public void setCommunicationService(CommunicationService communicationService) {
		this.communicationService = communicationService;
	}

	public void setSocialNodeMap(Map<String, LookAtMeNode> socialNodeMap) {
		this.socialNodeMap = socialNodeMap;
	}
	
	public void addILike(String nodeId) {
		iLike.add(nodeId);
	}
	
	public void addLiked(String nodeId) {
		liked.add(nodeId);
	}
	
	public String getNicknameOf(String nodeId) {
		LookAtMeNode node = (LookAtMeNode) socialNodeMap.get(nodeId);
		if (node != null) {
			return node.getProfile().getNickname();
		}
		return null;
	}

	public class SocialListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return socialNodeMap.size();
		}

		@Override
		public Object getItem(int arg0) {
			List<LookAtMeNode> nodeList = new ArrayList<LookAtMeNode>(socialNodeMap.values());
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
				LayoutInflater layoutInflater = (LayoutInflater) SocialListFragment.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.one_row_social_list, null);
			}

			LookAtMeNode node = (LookAtMeNode) this.getItem(position);
			BasicProfile profile = (BasicProfile) node.getProfile();

			TextView nickNameText = (TextView) convertView.findViewById(R.id.nickNameText);
			nickNameText.setText(node.getProfile().getNickname());

			// Problemi con il recupero dell'immagine del profilo
			ImageView photoImage = (ImageView) convertView.findViewById(R.id.profilePhotoImage);
			if (profile.getMainProfileImage() == null || profile.getMainProfileImage().getImage() == null) {
				Drawable noPhoto = getResources().getDrawable(R.drawable.no_profile_image);
				photoImage.setImageDrawable(noPhoto);
			} else {
				Bitmap bMap = BitmapFactory.decodeByteArray(profile.getMainProfileImage().getImage(), 0, profile.getMainProfileImage().getImage().length - 1);
				photoImage.setImageBitmap(bMap);
			}

			return convertView;
		}
	}

}