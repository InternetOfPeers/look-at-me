package com.dreamteam.lookme;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.MessageItem;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.ImageUtil;
import com.dreamteam.util.Nav;

public class ChatListAdapter extends BaseAdapter {
	List<MessageItem> chatHistoryList;
	private Activity activity;

	public ChatListAdapter(Activity activity) {
		this.activity = activity;
		chatHistoryList = Services.currentState.getMessagesHistoryMap().get(Nav.getStringParameter(activity));
		// Poiché sulla variabile fanno affidamento altri metodi, mi assicuro
		// che non sia null
		if (chatHistoryList == null)
			chatHistoryList = new ArrayList<MessageItem>();
	}

	@Override
	public int getCount() {
		return chatHistoryList.size();
	}

	@Override
	public Object getItem(int position) {
		return chatHistoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// LayoutInflater class is used to instantiate layout XML file
			// into its corresponding View objects.
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.fragment_chat_list_single_row, null);
		}
		boolean leftSide = true;

		MessageItem message = (MessageItem) getItem(position);
		Node node;
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

		TextView nickNameText = (TextView) convertView.findViewById(R.id.nickNameText);
		nickNameText.setText(node.getProfile().getNickname());

		TextView lastMessageText = (TextView) convertView.findViewById(R.id.lastMessageText);
		lastMessageText.setText(message.getMessage());

		TextView lastMessageDate = (TextView) convertView.findViewById(R.id.lastMessageDate);

		String timeElapsed = CommonUtils.timeElapsed(message.getCreationTime(), new Date(System.currentTimeMillis()));
		lastMessageDate.setText(timeElapsed);
		// lastMessageDate.setText(DateFormat.getDateTimeInstance().format(message.getCreationTime()));

		// Imposto l'immagine del profilo
		ImageView photoImage = (ImageView) convertView.findViewById(R.id.profilePhotoImage);
		BasicProfile profile = (BasicProfile) node.getProfile();
		photoImage.setImageBitmap(ImageUtil.getBitmapProfileImage(activity.getResources(), profile));
		return convertView;
	}
}
