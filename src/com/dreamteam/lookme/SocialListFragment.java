package com.dreamteam.lookme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.app.Activity;
import android.app.Dialog;
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
import com.dreamteam.lookme.communication.LookAtMeCommunicationRepository;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.service.CommunicationService;
import com.dreamteam.util.Log;

public class SocialListFragment extends Fragment implements OnClickListener, OnItemClickListener {

	private ListView socialListView;
	private SocialListAdapter socialListAdapter;
	private Button refreshListButton;
	private ProgressDialog loadingDialog;

	// @Subscribe
	// public void handleButtonPress(ButtonEvent event) {
	//
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private CommunicationService getCommunicationService() {
		SocialActivity socialActivity = (SocialActivity) this.getActivity();
		return socialActivity.getCommunicationService();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_social_list, null);

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
		getCommunicationService().refreshSocialList();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int clickedItemPosition, long clickedItemID) {
		Log.d();
		final LookAtMeNode node = (LookAtMeNode) socialListAdapter.getItem((int) clickedItemID);
		final Activity activity = this.getActivity();
		final Dialog dialog = new Dialog(this.getActivity());
		arg1.setAlpha(1);
		// tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.chosed_profile_dialog);
		dialog.setTitle("What do u wanna do?");
		TextView txt = (TextView) dialog.findViewById(R.id.nickname_txt);
		txt.setText(node.getProfile().getNickname());
		// txt = (TextView) dialog.findViewById(R.id.age_txt);
		// txt.setText(node.getProfile().getAge());

		txt = (TextView) dialog.findViewById(R.id.gender_txt);
		txt.setText(node.getProfile().getGender());
		txt = (TextView) dialog.findViewById(R.id.matching_score_txt);
		txt.setText("90%");
		Button dialogButton = (Button) dialog.findViewById(R.id.startChat_btn);
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				// BasicProfile myBasicProfile =
				// ((CommonActivity)activity).getMyBasicProfile();
				getCommunicationService().sendStartChatMessage(node.getId());
				// TODO: entrare nella chat privata

			}

		});
		dialog.show();
	}

	public void refreshFragment() {
		Log.d();
		this.socialListAdapter.notifyDataSetChanged();
	}

	public void dismissLoadingDialog() {
		loadingDialog.dismiss();
	}

	public String getNicknameOf(String nodeId) {
		LookAtMeNode node = (LookAtMeNode) LookAtMeCommunicationRepository.getInstance().getSocialNodeMap().get(nodeId);
		if (node != null) {
			return node.getProfile().getNickname();
		}
		return null;
	}

	public class SocialListAdapter extends BaseAdapter {
		
		Map<String, LookAtMeNode> socialNodeMap = LookAtMeCommunicationRepository.getInstance().getSocialNodeMap();

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
				Drawable noPhoto = getResources().getDrawable(R.drawable.ic_profile_image);
				photoImage.setImageDrawable(noPhoto);
			} else {
				Bitmap bMap = BitmapFactory.decodeByteArray(profile.getMainProfileImage().getImage(), 0, profile.getMainProfileImage().getImage().length - 1);
				photoImage.setImageBitmap(bMap);
			}

			return convertView;
		}
	}

}