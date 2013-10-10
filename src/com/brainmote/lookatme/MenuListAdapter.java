package com.brainmote.lookatme;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainmote.lookatme.service.Services;

public class MenuListAdapter extends BaseAdapter {

	private Activity activity;
	private String[] menuLabels;

	public MenuListAdapter(Activity activity, String[] menuLabels) {
		this.activity = activity;
		this.menuLabels = menuLabels;
	}

	@Override
	public int getCount() {
		return menuLabels.length;
	}

	@Override
	public String getItem(int position) {
		return menuLabels[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// LayoutInflater class is used to instantiate layout XML file
			// into its corresponding View objects.
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.drawer_list_item, null);
		}
		TextView title = (TextView) convertView.findViewById(R.id.textLabel);
		title.setText(getItem(position));
		TextView notificationNumber = (TextView) convertView.findViewById(R.id.drawer_item_notification_number);
		ImageView icon = (ImageView) convertView.findViewById(R.id.imageView1);
		int pendingMessages = 0;
		switch (position) {
		case 0:
			icon.setImageResource(R.drawable.ic_edit_profile);
			break;
		case 1:
			icon.setImageResource(R.drawable.ic_nearby);
			pendingMessages = Services.notification.getLikePendingNotifications() + Services.notification.getPerfectMatchPendingNotifications();
			if (pendingMessages > 0) {
				notificationNumber.setText(String.valueOf(pendingMessages));
				if (Build.VERSION.SDK_INT >= 16) {
					notificationNumber.setBackground(activity.getResources().getDrawable(R.drawable.menu_notification_number_background));
				} else {
					notificationNumber.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.menu_notification_number_background));
				}
			} else {
				notificationNumber.setText("");
				if (Build.VERSION.SDK_INT >= 16) {
					notificationNumber.setBackground(activity.getResources().getDrawable(R.drawable.ic_void));
				} else {
					notificationNumber.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_void));
				}
			}
			break;
		case 2:
			icon.setImageResource(R.drawable.ic_conversations);
			pendingMessages = Services.notification.getChatMessagePendingNotifications();
			if (pendingMessages > 0) {
				notificationNumber.setText(String.valueOf(pendingMessages));
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					notificationNumber.setBackground(activity.getResources().getDrawable(R.drawable.menu_notification_number_background));
				} else {
					notificationNumber.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.menu_notification_number_background));
				}
			} else {
				notificationNumber.setText("");
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					notificationNumber.setBackground(activity.getResources().getDrawable(R.drawable.ic_void));
				} else {
					notificationNumber.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_void));
				}
			}
			break;
		case 3:
			icon.setImageResource(R.drawable.ic_star);
			break;
		case 4:
			icon.setImageResource(R.drawable.ic_statistics);
			break;
		case 5:
			icon.setImageResource(R.drawable.ic_settings);
			break;
		case 6:
			icon.setImageResource(R.drawable.ic_help);
			break;
		}
		return convertView;
	}

}
