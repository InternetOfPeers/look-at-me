package com.dreamteam.lookme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.service.Event;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.Nav;
import com.squareup.otto.Subscribe;

public class ChatListFragment extends Fragment {

	private ListView messageListView;
	private ChatListAdapter chatListAdapter;
	private EditText mInputEditText;
	private Button sendButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat_list, null);
		chatListAdapter = new ChatListAdapter(getActivity());
		messageListView = (ListView) view.findViewById(R.id.chatListView);
		messageListView.setAdapter(chatListAdapter);
		mInputEditText = (EditText) view.findViewById(R.id.messageEdit);
		sendButton = (Button) view.findViewById(R.id.sendButton);
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = mInputEditText.getText().toString();
				if (text != null && !text.isEmpty()) {
					Node node = CommonUtils.getNodeFromChannelId(Nav.getStringParameter(ChatListFragment.this.getActivity()));
					if (node != null && node.getId() != null && !node.getId().isEmpty() && node.getProfile() != null && node.getProfile().getId() != null
							&& !node.getProfile().getId().isEmpty() && !mInputEditText.getText().toString().isEmpty()) {
						Services.businessLogic.sendChatMessage(CommonUtils.getNodeFromChannelId(Nav.getStringParameter(ChatListFragment.this.getActivity())).getId(), text);
						mInputEditText.getText().clear();
						ChatListFragment.this.refreshFragment();
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
		super.onActivityCreated(savedInstanceState);
		refreshFragment();
	}

	private void refreshFragment() {
		chatListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onStart() {
		super.onStart();
		Services.event.register(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		Services.event.unregister(this);
	}

	@Subscribe
	public void onEventReceived(Event event) {
		switch (event.getEventType()) {
		case CHAT_MESSAGE_RECEIVED:
			refreshFragment();
			scrollMyListViewToBottom();
			break;
		default:
			break;
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

}
