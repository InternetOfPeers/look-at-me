package com.dreamteam.lookme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.ImageUtil;

public class NearbyListAdapter extends BaseAdapter {

	private Activity activity;

	public NearbyListAdapter(Activity activity) {
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return Services.currentState.getSocialNodeMap().size();
	}

	@Override
	public Node getItem(int position) {
		List<Node> nodeList = new ArrayList<Node>(Services.currentState.getSocialNodeMap().values());
		return nodeList.get(position);
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
			convertView = layoutInflater.inflate(R.layout.fragment_nearby_list_single_row, null);
		}
		// Imposto l'immagine del profilo
		ImageView photoImage = (ImageView) convertView.findViewById(R.id.profilePhotoImage);
		BasicProfile profile = (BasicProfile) getItem(position).getProfile();
		photoImage.setImageBitmap(ImageUtil.getBitmapProfileImage(activity.getResources(), profile));
		// Imposto i liked
		if (Services.currentState.getLikedSet().contains(getItem(position).getId())) {
			ImageView likedImage = (ImageView) convertView.findViewById(R.id.imageLiked);
			likedImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.love_icon));
		}
		return convertView;
	}

}
