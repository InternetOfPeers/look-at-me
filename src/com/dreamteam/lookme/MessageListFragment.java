package com.dreamteam.lookme;

import java.util.Collection;
import java.util.Iterator;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.FileItem;
import com.dreamteam.lookme.bean.MessageItem;
import com.dreamteam.lookme.bean.ViewHolder;
import com.dreamteam.lookme.communication.LookAtMeCommunicationRepository;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.service.CommunicationService;
import com.dreamteam.util.Log;

public class MessageListFragment extends Fragment implements OnClickListener, OnItemClickListener {

	//private Map<String, List<MessageItem>> messagesHistoryMap = new HashMap<String, List<MessageItem>>();

	//public static final int CHAT_LIST_FRAGMENT = 1002;

	private Activity activity;

	private ListView messageListView;
	private MessagesListAdapter messageListAdapter;
	private MessageListAdapter chatListAdapter;
	private ProgressDialog loadingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private CommunicationService getCommunicationService() {
		MessagesActivity socialActivity = (MessagesActivity) this.getActivity();
		return socialActivity.getCommunicationService();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message_list, null);
		messageListAdapter = new MessagesListAdapter();
		messageListView = (ListView) view.findViewById(R.id.messageListView);
		messageListView.setAdapter(messageListAdapter);
		messageListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		android.util.Log.e("ONATTACH", "ONATTACH");
		super.onAttach(activity);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		android.util.Log.e("onActivityCreated", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
		//messagesHistoryMap = communicationRepository.getMessagesHistoryMap();
		messageListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View arg0) {
		Log.d();
		getCommunicationService().refreshSocialList();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int clickedItemPosition, long clickedItemID) {
		MessageItem messageItem = (MessageItem) messageListAdapter.getItem(clickedItemPosition);
		// List<MessageItem>
		// chatListHistory=getCommunicationService().getChat("");
		// chatListAdapter.setChatListHistory(chatListHistory);
	}

//	public void putMessageNode(LookAtMeNode node) {
//		messagesHistoryMap.put("", new ArrayList<MessageItem>());
//	}
//
//	public void removeMessageNode(String nodeName) {
//		messagesHistoryMap.remove(nodeName);
//	}
//
//	public List<MessageItem> getMessageNode(String nodeName) {
//		return messagesHistoryMap.get(nodeName);
//	}

	public void refreshFragment() {
		this.messageListAdapter.notifyDataSetChanged();
	}

	public void dismissLoadingDialog() {
		loadingDialog.dismiss();
	}

	// public void setSocialNodeMap(Map<String, LookAtMeNode> socialNodeMap) {
	// this.socialNodeMap = socialNodeMap;
	// }

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	// public LookAtMeNode getSocialNode(String id) {
	// return chatHistoryMapMap.get(id);
	// }

	public class MessagesListAdapter extends BaseAdapter {
		
		Map<String, List<MessageItem>> messagesHistoryMap = LookAtMeCommunicationRepository.getInstance().getMessagesHistoryMap();

		@Override
		public int getCount() {
			return messagesHistoryMap.size();
		}

		@Override
		public Object getItem(int arg0) {
			Collection<List<MessageItem>> nodeList = messagesHistoryMap.values();
			Iterator<List<MessageItem>> iter = nodeList.iterator();
			MessageItem tempMessage = null;
			int i = 0;
			while (iter.hasNext()) {
				List<MessageItem> tempList = iter.next();
				tempMessage = tempList.get(0);
				if (i == arg0)
					break;
			}
			return tempMessage;
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
				LayoutInflater layoutInflater = (LayoutInflater) MessageListFragment.this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

	public class MessageListAdapter extends BaseAdapter {
		private static final String TAG = "[Chord][ApiTest]";

		private static final String TAGClass = "ChatListAdapter : ";

		private List<MessageItem> mMessageItemList = null;

		private LayoutInflater mInflater = null;

		private ViewHolder mViewHolder = null;

		// private ICancelFileButtonListener mCancelFileButtonListener = null;

		@Override
		public int getCount() {
			return mMessageItemList.size();
		}

		@Override
		public MessageItem getItem(int position) {
			// TODO Auto-generated method stub
			return mMessageItemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public void addChat(LookAtMeNode node, String message, boolean isMine) {
			MessageItem item = new MessageItem(node.getProfile().getNickname(), message, isMine);
			mMessageItemList.add(item);
			notifyDataSetChanged();
		}

		private MessageItem getItemByExchangeId(String exchangeId) {
			if (mMessageItemList.isEmpty()) {
				Log.d(TAGClass + "getItemByExchangeId : List is Empty >" + exchangeId);
				return null;
			}

			for (MessageItem i : mMessageItemList) {
				FileItem file = i.getFileItem();
				if (null != file && file.getExchangeId().equals(exchangeId)) {
					return i;
				}
			}

			return null;
		}

		public void addFileLog(boolean bMine, String nodeName, int progress, String exchangeId, String message) {
			MessageItem item = getItemByExchangeId(exchangeId);
			if (null == item) {
				item = null;// new ChatItem( nodeName, message,false);
				item.setFileItem(new FileItem(exchangeId));
				mMessageItemList.add(item);
			}

			item.getFileItem().setProgress(progress);
			notifyDataSetChanged();
		}

		public void addFileCompleteLog(boolean bMine, String nodeName, String message, String exchangeId) {
			MessageItem item = getItemByExchangeId(exchangeId);
			if (null == item) {
				Log.d(TAGClass + "addFileCompleteLog : new > " + exchangeId);
				item = null;// new ChatItem( nodeName, message,bMine);
				mMessageItemList.add(item);
			} else {
				Log.d(TAGClass + "addFileCompleteLog : update > " + exchangeId);
				item.setFileItem(null);
				item.setMessage(message);
			}

			notifyDataSetChanged();
		}

		public void clearAll() {
			mMessageItemList.clear();
			notifyDataSetChanged();
		}

		public void setChatListHistory(List<MessageItem> chatListHistory) {
			this.mMessageItemList = chatListHistory;
		}

		public List<MessageItem> getChatListHistory() {
			return mMessageItemList;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;
			if (v == null) {
				mViewHolder = new ViewHolder();
				// v = mInflater.inflate(R.layout.chat_listitem, parent, false);
				// mViewHolder.yourNodeName =
				// (TextView)v.findViewById(R.id.yourNodeName);
				// mViewHolder.chatMessage =
				// (TextView)v.findViewById(R.id.chatMessage);
				// mViewHolder.myNodeName =
				// (TextView)v.findViewById(R.id.myNodeName);
				// mViewHolder.fileLayout =
				// (LinearLayout)v.findViewById(R.id.fileLayout);
				// mViewHolder.progressLayout =
				// (LinearLayout)v.findViewById(R.id.progressLayout);
				// mViewHolder.progressBar =
				// (ProgressBar)v.findViewById(R.id.progressBar);
				// mViewHolder.fileCancelBtn =
				// (Button)v.findViewById(R.id.fileCancelBtn);
				// mViewHolder.chatLayout =
				// (LinearLayout)v.findViewById(R.id.chatLayout);
				v.setTag(mViewHolder);
			} else {
				mViewHolder = (ViewHolder) v.getTag();
			}

			MessageItem messageItem = mMessageItemList.get(position);

			if (messageItem.isMine()) {
				mViewHolder.getYourNodeName().setVisibility(View.GONE);

				mViewHolder.getMyNodeName().setVisibility(View.VISIBLE);
				mViewHolder.getMyNodeName().setText(messageItem.getNickName());
				// mViewHolder.chatLayout.setBackgroundResource(R.drawable.sentmessage);
			} else {
				mViewHolder.getMyNodeName().setVisibility(View.GONE);

				mViewHolder.getYourNodeName().setVisibility(View.VISIBLE);
				mViewHolder.getYourNodeName().setText(messageItem.getNickName());
				// mViewHolder.chatLayout.setBackgroundResource(R.drawable.receivedmessage);
			}

			mViewHolder.getChatMessage().setText(messageItem.getMessage());

			FileItem fileItem = messageItem.getFileItem();
			if (null == fileItem) {
				mViewHolder.getFileLayout().setVisibility(View.GONE);
			} else {
				final String exchangeId = fileItem.getExchangeId();
				mViewHolder.getFileLayout().setVisibility(View.VISIBLE);
				mViewHolder.getProgressBar().setProgress(fileItem.getProgress());
				mViewHolder.getFileCancelBtn().setTag(exchangeId);
				mViewHolder.getFileCancelBtn().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// mCancelFileButtonListener.onCancelFileButtonClick(exchangeId);
					}
				});
			}

			return v;
		}

	}

}
