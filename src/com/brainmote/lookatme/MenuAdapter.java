package com.brainmote.lookatme;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {

	private Activity activity;
	private String[] menuLabels;

	public MenuAdapter(Activity activity, String[] menuLabels) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// LayoutInflater class is used to instantiate layout XML file
			// into its corresponding View objects.
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.drawer_list_item, null);
		}
		TextView label = (TextView) convertView.findViewById(R.id.textLabel);
		label.setText(getItem(position));

		ImageView icon = (ImageView) convertView.findViewById(R.id.imageView1);
		switch (position) {
		case 0:
			icon.setImageResource(R.drawable.ic_edit_profile);
			break;
		case 1:
			icon.setImageResource(R.drawable.ic_nearby);
			break;
		case 2:
			icon.setImageResource(R.drawable.ic_conversations);
			break;
		case 3:
			icon.setImageResource(R.drawable.ic_statistics);
			break;
		case 4:
			icon.setImageResource(R.drawable.ic_settings);
			break;
		case 5:
			icon.setImageResource(R.drawable.ic_help);
			break;
		}
		return convertView;
	}

}
