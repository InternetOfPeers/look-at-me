package com.dreamteam.lookme;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.MessageItem;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.service.Event;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.ImageUtil;
import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;
import com.squareup.otto.Subscribe;

public class MessagesListFragment extends Fragment implements OnClickListener, OnItemClickListener {

	private ListView messageListView;
	private MessagesListAdapter messageListAdapter;
	private ProgressDialog loadingDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message_list, container, false);
		messageListAdapter = new MessagesListAdapter();
		messageListView = (ListView) view.findViewById(R.id.messageListView);
		messageListView.setAdapter(messageListAdapter);
		messageListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d();
		super.onActivityCreated(savedInstanceState);
		messageListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View arg0) {
		Log.d();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int clickedItemPosition, long clickedItemID) {
		Log.d();
		// NELLA LISTA DEI MESSAGGI DEVE ESSERCI SEMPRE UN MESSAGGIO CON IL
		// NODEID DEL NODO CON CUI STO CONVERSANDO
		MessageItem message = (MessageItem) messageListAdapter.getItem((int) clickedItemID);
		String profileId = Services.currentState.getSocialNodeMap().get(message.getNodeId()).getProfile().getId();
		Nav.startActivityWithString(this.getActivity(), ChatActivity.class, CommonUtils.generateChannelName(Services.currentState.getMyBasicProfile().getId(), profileId));
	}

	public void refreshFragment() {
		this.messageListAdapter.notifyDataSetChanged();
	}

	public void dismissLoadingDialog() {
		loadingDialog.dismiss();
	}

	@Subscribe
	public void onMessageReceived(Event event) {
		Log.d(event.getEventType().toString());
		switch (event.getEventType()) {
		case NODE_JOINED:
			break;
		case CHAT_MESSAGE_RECEIVED:
			messageListAdapter.notifyDataSetChanged();
			break;
		case PROFILE_RECEIVED:
			break;
		case LIKE_RECEIVED:
			break;
		default:
			break;
		}
	}

	public class MessagesListAdapter extends BaseAdapter {

		List<String> channelList = getListFromMessageMap();

		@Override
		public int getCount() {
			return channelList.size();
		}

		@Override
		public Object getItem(int arg0) {
			Node node = CommonUtils.getNodeFromChannelName(channelList.get(arg0));
			// Node node =
			// Services.currentState.getSocialNodeMap().get(CommonUtils.getNodeFromChannelName(channelList.get(arg0)));
			List<MessageItem> messageList = Services.currentState.getMessagesHistoryMap().get(channelList.get(arg0));
			MessageItem fakeMessage = new MessageItem(node.getId(), node.getProfile().getId(), "", false);
			if (messageList != null && !messageList.isEmpty()) {
				fakeMessage.setMessage(messageList.get(messageList.size() - 1).getMessage());
			}

			return fakeMessage;
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
				LayoutInflater layoutInflater = (LayoutInflater) MessagesListFragment.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.one_row_message_list, null);
			}
			MessageItem message = (MessageItem) this.getItem(position);
			Node node = Services.currentState.getSocialNodeMap().get(message.getNodeId());
			TextView nickNameText = (TextView) convertView.findViewById(R.id.nickNameText);
			nickNameText.setText("conversation to: " + node.getProfile().getNickname());
			// TextView lastMessageText = (TextView)
			// convertView.findViewById(R.id.lastMessageText);
			// lastMessageText.setText(message.getMessage());
			TextView lastMessageDate = (TextView) convertView.findViewById(R.id.lastMessageDate);
			String timeElapsed = CommonUtils.timeElapsed(message.getCreationTime(), new Date(System.currentTimeMillis()));
			lastMessageDate.setText(timeElapsed);
			// Imposto l'immagine del profilo
			ImageView photoImage = (ImageView) convertView.findViewById(R.id.profilePhotoImage);
			BasicProfile profile = (BasicProfile) node.getProfile();
			photoImage.setImageBitmap(ImageUtil.getBitmapProfileImage(getResources(), profile));
			return convertView;
		}

		private List<String> getListFromMessageMap() {
			List<String> list = new ArrayList<String>();
			list.addAll(Services.currentState.getMessagesHistoryMap().keySet());
			return list;
		}
	}

}
