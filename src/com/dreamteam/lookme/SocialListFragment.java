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
import android.widget.Toast;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.service.Event;
import com.dreamteam.lookme.service.Services;
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
		super.onStart();
		Services.eventBus.register(this);
	}

	@Override
	public void onStop() {
		Log.d();
		super.onStop();
		Services.eventBus.unregister(this);
	}

	@Subscribe
	public void onNodeMovment(Event event) {
		Log.d(event.getEventType().toString());
		switch (event.getEventType()) {
		case NODE_JOINED:
		case NODE_LEFT:
			socialListAdapter.notifyDataSetChanged();
			break;
		case PROFILE_RECEIVED:
			dismissLoadingDialog();
			SocialActivity activity = (SocialActivity) this.getActivity();
			activity.setFragment(SocialActivity.SOCIAL_PROFILE_FRAGMENT);
			break;
		case LIKE_RECEIVED:
			Toast likeToast = Toast.makeText(Services.currentState.getContext(), (String) event.getEventObject() + " send you a LIKE", Toast.LENGTH_LONG);
			likeToast.show();
			break;
		case CHAT_MESSAGE_RECEIVED:
			Toast chatToast = Toast.makeText(Services.currentState.getContext(), (String) event.getEventObject() + " sent you a message", Toast.LENGTH_LONG);
			chatToast.show();
			break;
		case LIKE_MATCH:
			Toast likeMatchToast = Toast.makeText(Services.currentState.getContext(), (String) event.getEventObject() + " exchanged a LIKE", Toast.LENGTH_LONG);
			likeMatchToast.show();
		default:
			break;
		}
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
		final Node node = (Node) socialListAdapter.getItem((int) clickedItemID);
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
				Services.businessLogic.sendStartChatMessage(node.getId());
				// TODO: entrare nella chat privata
				// a scopo di test invio un messaggio dopo 3 secondi
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Services.businessLogic.sendChatMessage(node.getId(), "Ciao! come stai?");
			}

		});

		Button viewFullProfileButton = (Button) dialog.findViewById(R.id.viewFullProfile_btn);
		viewFullProfileButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Services.businessLogic.sendFullProfileRequest(node.getId());
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

		Map<String, Node> socialNodeMap = Services.currentState.getSocialNodeMap();

		@Override
		public int getCount() {
			return socialNodeMap.size();
		}

		@Override
		public Object getItem(int arg0) {
			List<Node> nodeList = new ArrayList<Node>(socialNodeMap.values());
			Node node = (Node) nodeList.get(arg0);
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

			Node node = (Node) this.getItem(position);
			BasicProfile profile = (BasicProfile) node.getProfile();

			TextView nickNameText = (TextView) convertView.findViewById(R.id.nickNameText);
			nickNameText.setText(node.getProfile().getNickname());

			// Problemi con il recupero dell'immagine del profilo
			ImageView photoImage = (ImageView) convertView.findViewById(R.id.profilePhotoImage);
			if (profile.getMainProfileImage() == null || profile.getMainProfileImage().getImage() == null) {
				Drawable noPhoto = getResources().getDrawable(R.drawable.ic_profile_image);
				photoImage.setImageDrawable(noPhoto);
			} else {
				Bitmap bMap = BitmapFactory.decodeByteArray(profile.getMainProfileImage().getImage(), 0, profile.getMainProfileImage().getImage().length);
				photoImage.setImageBitmap(bMap);
			}

			return convertView;
		}
	}

}