package com.brainmote.lookatme;

import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainmote.lookatme.enumattribute.Interest;
import com.brainmote.lookatme.enumattribute.Interest.InterestCategory;

public class EditProfileInterestGridAdapter extends BaseAdapter {

	private Integer[] interestValues;
	private Activity activity;
	private boolean empty;

	public EditProfileInterestGridAdapter(Activity activity, Set<Integer> interestSet, boolean setNotAnInterest) {
		this.activity = activity;
		if (interestSet == null || interestSet.size() == 0) {
			if (setNotAnInterest) {
				interestValues = new Integer[1];
				interestValues[0] = Interest.Touch_here_to_manage_interests.getValue();
			} else {
				interestValues = new Integer[0];
			}
			empty = true;
		} else {
			Object[] tempArray = interestSet.toArray();
			interestValues = new Integer[tempArray.length];
			for (int i = 0; i < tempArray.length; i++) {
				Object o = tempArray[i];
				Integer value = (Integer) o;
				interestValues[i] = value;
			}
			empty = false;
		}
	}

	public boolean isEmpty() {
		return empty;
	}

	@Override
	public int getCount() {
		return interestValues.length;
	}

	@Override
	public Object getItem(int position) {
		return Interest.getInterestWithValue(interestValues[position]).toString();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String description = (String) getItem(position);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.interest_item, null);
		}
		TextView text = (TextView) convertView.findViewById(R.id.textViewInterest);
		text.setText(description);
		ImageView icon = (ImageView) convertView.findViewById(R.id.imageInterestIcon);
		Interest interest = Interest.getInterestWithName(description);
		icon.setImageResource(InterestCategory.getIconOfCategory(interest.getInterestCategory()));
		if (isEmpty()) {
			icon.setVisibility(ImageView.GONE);
			text.setGravity(Gravity.CENTER_HORIZONTAL);
		}
		return convertView;
	}

}
