package com.dreamteam.lookme;

import java.util.Date;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.MessageItem;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.service.Event;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;
import com.squareup.otto.Subscribe;

public class ChatListFragment extends Fragment implements OnClickListener, OnItemClickListener {

	private ListView messageListView;
	private ChatListAdapter chatListAdapter;
	private EditText mInputEditText;
	private ProgressDialog loadingDialog;
	private Button sendButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat_list, null);
		chatListAdapter = new ChatListAdapter();
		messageListView = (ListView) view.findViewById(R.id.chatListView);
		messageListView.setAdapter(chatListAdapter);
		mInputEditText = (EditText) view.findViewById(R.id.messageEdit);
		sendButton = (Button) view.findViewById(R.id.sendButton);

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = mInputEditText.getText().toString();
				if (text != null && !text.isEmpty()) {
					Node node = CommonUtils.getNodeFromChannelName(Nav.getStringParameter(ChatListFragment.this.getActivity()));
					if (node != null && node.getId() != null && !node.getId().isEmpty() && node.getProfile() != null && node.getProfile().getId() != null
							&& !node.getProfile().getId().isEmpty() && !mInputEditText.getText().toString().isEmpty()) {
						Services.businessLogic.sendChatMessage(CommonUtils.getNodeFromChannelName(Nav.getStringParameter(ChatListFragment.this.getActivity())).getId(), text);
						mInputEditText.getText().clear();
						chatListAdapter.notifyDataSetChanged();
						scrollMyListViewToBottom();
					} else {
						Toast toast = Toast.makeText(ChatListFragment.this.getActivity(), R.string.unable_to_send_chat_message, Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d();
		super.onActivityCreated(savedInstanceState);
		chatListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int clickedItemPosition, long clickedItemID) {

	}

	public void refreshFragment() {
		this.chatListAdapter.notifyDataSetChanged();
	}

	public void dismissLoadingDialog() {
		loadingDialog.dismiss();
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
			break;
		case PROFILE_RECEIVED:
			break;
		case LIKE_RECEIVED:
			break;
		case CHAT_MESSAGE_RECEIVED:
			chatListAdapter = new ChatListAdapter();
			messageListView.setAdapter(chatListAdapter);
			chatListAdapter.notifyDataSetChanged();
			scrollMyListViewToBottom();
			break;
		case LIKE_MATCH:
		default:
			break;
		}
	}

	// @Subscribe
	// public void onMessageReceived(Event event) {
	// Log.d(event.getEventType().toString());
	// switch (event.getEventType()) {
	// case NODE_JOINED:break;
	//
	// }
	// }

	public class ChatListAdapter extends BaseAdapter {
		List<MessageItem> chatHistoryList = Services.currentState.getMessagesHistoryMap().get(Nav.getStringParameter(ChatListFragment.this.getActivity()));

		@Override
		public int getCount() {
			return chatHistoryList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return chatHistoryList.get(arg0);
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
				LayoutInflater layoutInflater = (LayoutInflater) ChatListFragment.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.one_row_chat_list, null);
			}
			boolean leftSide = true;

			MessageItem message = (MessageItem) this.getItem(position);
			Node node = null;
			if (message.isMine()) {
				node = new Node();
				leftSide = false;
				// TODO: trovare il proprio chord id
				// node.setId(id);
				node.setProfile(Services.currentState.getMyBasicProfile());
			} else
				node = Services.currentState.getSocialNodeMap().get(message.getNodeId());

			int bgRes = R.drawable.right_message_bg;

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			if (!leftSide) {
				bgRes = R.drawable.left_message_bg;
				params.gravity = Gravity.RIGHT;
			}

			convertView.setBackgroundResource(bgRes);

			BasicProfile profile = (BasicProfile) node.getProfile();

			TextView nickNameText = (TextView) convertView.findViewById(R.id.nickNameText);
			nickNameText.setText(node.getProfile().getNickname());

			TextView lastMessageText = (TextView) convertView.findViewById(R.id.lastMessageText);
			lastMessageText.setText(message.getMessage());

			TextView lastMessageDate = (TextView) convertView.findViewById(R.id.lastMessageDate);

			String timeElapsed = CommonUtils.timeElapsed(message.getCreationTime(), new Date(System.currentTimeMillis()));
			lastMessageDate.setText(timeElapsed);
			// lastMessageDate.setText(DateFormat.getDateTimeInstance().format(message.getCreationTime()));

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

	private void scrollMyListViewToBottom() {
		messageListView.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				messageListView.setSelection(messageListView.getCount() - 1);
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
