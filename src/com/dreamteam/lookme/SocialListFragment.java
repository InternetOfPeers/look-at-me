package com.dreamteam.lookme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
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

public class SocialListFragment extends Fragment implements OnClickListener,
		OnItemClickListener {

	public static final int SOCIAL_LIST_FRAGMENT = 1001;

	private CommunicationService communicationService;
	private Activity activity;

	private Map<String, LookAtMeNode> socialNodeMap;

	private ListView socialListView;
	private SocialListAdapter socialListAdapter;
	private Button refreshListButton;
	private ProgressDialog loadingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_social_list, null);

		socialNodeMap = new HashMap<String, LookAtMeNode>();

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
	public void onItemClick(AdapterView<?> arg0, View arg1,
			int clickedItemPosition, long clickedItemID) {
		Log.d();
		LookAtMeNode node = (LookAtMeNode) socialListAdapter
				.getItem((int) clickedItemID);
		communicationService.sendProfileRequest(node.getId());
		loadingDialog = ProgressDialog.show(activity, "Loading",
				"Please wait...", true);
	}

	public void putSocialNode(LookAtMeNode node) {
		socialNodeMap.put(node.getId(), node);
	}

	public void removeSocialNode(String nodeName) {
		socialNodeMap.remove(nodeName);
	}

	public void refreshFragment() {
		this.socialListAdapter.notifyDataSetChanged();
	}

	public void dismissLoadingDialog() {
		loadingDialog.dismiss();
	}

	public void setCommunicationService(
			CommunicationService communicationService) {
		this.communicationService = communicationService;
	}

	public void setSocialNodeMap(Map<String, LookAtMeNode> socialNodeMap) {
		this.socialNodeMap = socialNodeMap;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
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
				LayoutInflater layoutInflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(
						R.layout.one_row_social_list, null);
			}

			LookAtMeNode node = (LookAtMeNode) this.getItem(position);
			BasicProfile profile = (BasicProfile) node.getProfile();

			TextView nickNameText = (TextView) convertView
					.findViewById(R.id.nickNameText);
			nickNameText.setText(node.getProfile().getNickname());

			// Problemi con il recupero dell'immagine del profilo
			ImageView photoImage = (ImageView) convertView
					.findViewById(R.id.profilePhotoImage);
			if (profile.getMainProfileImage() == null
					|| profile.getMainProfileImage().getImage() == null) {
				Drawable noPhoto = getResources().getDrawable(
						R.drawable.no_profile_image);
				photoImage.setImageDrawable(noPhoto);
			} else {
				Bitmap bMap = BitmapFactory.decodeByteArray(profile
						.getMainProfileImage().getImage(), 0, profile
						.getMainProfileImage().getImage().length - 1);
				photoImage.setImageBitmap(bMap);
			}

			return convertView;
		}
	}

}
