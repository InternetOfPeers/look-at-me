package com.brainmote.lookatme;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brainmote.lookatme.enumattribute.Interest;
import com.brainmote.lookatme.service.Services;

public class InterestGridAdapter extends BaseAdapter {

	private Integer[] interestValues;
	private Activity activity;

	public InterestGridAdapter(Activity activity) {
		this.activity = activity;
		if (Services.currentState.getInterestSet() == null || Services.currentState.getInterestSet().size() == 0) {
			interestValues = new Integer[1];
			interestValues[0] = Interest.Manage_interest.getValue();
		} else {
			Object[] tempArray = Services.currentState.getInterestSet().toArray();
			interestValues = new Integer[tempArray.length];
			for (int i = 0; i < tempArray.length; i++) {
				Object o = tempArray[i];
				Integer value = (Integer) o;
				interestValues[i] = value;
			}
		}
	}

	@Override
	public int getCount() {
		return interestValues.length;
	}

	@Override
	public Object getItem(int arg0) {
		return Interest.getInterestWithValue(interestValues[arg0]).toString();
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
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
		return convertView;
	}

}
