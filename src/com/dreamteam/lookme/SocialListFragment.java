package com.dreamteam.lookme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.dreamteam.lookme.communication.EventBusProvider;
import com.dreamteam.lookme.communication.LookAtMeCommunicationRepository;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.event.LookAtMeEvent;
import com.dreamteam.lookme.service.CommunicationService;
import com.dreamteam.util.Log;
import com.squareup.otto.Subscribe;

public class SocialListFragment extends Fragment implements OnItemClickListener {

	private ListView socialListView;
	private SocialListAdapter socialListAdapter;
	private ProgressDialog loadingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		Log.d();
		EventBusProvider.getIntance().register(this);
		super.onStart();
	}

	@Subscribe
	public void onNodeMovment(LookAtMeEvent event) {
		Log.d(event.getEventType().toString());
		switch (event.getEventType()) {
		case NODE_JOINED:
			socialListAdapter.notifyDataSetChanged();
			break;
		case NODE_LEFT:
			socialListAdapter.notifyDataSetChanged();
			break;
		case PROFILE_RECEIVED:
			dismissLoadingDialog();
			SocialActivity activity = (SocialActivity) this.getActivity();
			activity.setFragment(SocialActivity.SOCIAL_PROFILE_FRAGMENT);
			break;
		default:
			break;
		}
	}

	private CommunicationService getCommunicationService() {
		SocialActivity socialActivity = (SocialActivity) this.getActivity();
		return socialActivity.getCommunicationService();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d();
		View view = inflater.inflate(R.layout.fragment_social_list, null);

		socialListAdapter = new SocialListAdapter();
		socialListView = (ListView) view.findViewById(R.id.socialListView);
		socialListView.setAdapter(socialListAdapter);
		socialListView.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int clickedItemPosition, long clickedItemID) {
		Log.d();
		final LookAtMeNode node = (LookAtMeNode) socialListAdapter.getItem((int) clickedItemID);
		final Dialog dialog = new Dialog(this.getActivity());
		arg1.setAlpha(1);
		// tell the Dialog to use the dialog.xml as it's layout description
		dialog.setContentView(R.layout.chosed_profile_dialog);
		dialog.setTitle("What do u wanna do?");

		TextView txt = (TextView) dialog.findViewById(R.id.nickname_txt);
		txt.setText(node.getProfile().getNickname());
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

		Button viewFullProfileButton = (Button) dialog.findViewById(R.id.viewFullProfile_btn);
		viewFullProfileButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				getCommunicationService().sendFullProfileRequest(node.getId());
				// entro in attesa
				loadingDialog = new ProgressDialog(SocialListFragment.this.getActivity());
				loadingDialog.setTitle("Loading profile");
				loadingDialog.show();
			}

		});
		dialog.show();
	}

	public void dismissLoadingDialog() {
		loadingDialog.dismiss();
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