package com.brainmote.lookatme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.brainmote.lookatme.enumattribute.Interest;
import com.brainmote.lookatme.enumattribute.Interest.InterestCategory;
import com.brainmote.lookatme.service.Services;

public class InterestAdapter extends BaseExpandableListAdapter {
	
	private Context context;
	private List<String> categoryList;
	private Map<String, List<String>> interestMap;
	
	public InterestAdapter(Context context) {
		this.context = context;
		categoryList = InterestCategory.getInterestCategoryList();
		interestMap = new HashMap<String, List<String>>();
		for (String category : categoryList) {
			interestMap.put(category, InterestCategory.getInterestOfCategory(Interest.getInterestWithName(category).getValue()));
		}
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		return interestMap.get(categoryList.get(arg0)).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return Interest.getInterestWithName((String) getChild(arg0, arg1)).getValue();
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		String description = (String) getChild(groupPosition, childPosition);
		final int childId = (int) getChildId(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.interest_child_row, null);
		}
		final TextView label = (TextView) convertView.findViewById(R.id.textInterestDescription);
		label.setText(description);
		if (Services.currentState.getInterestSet().contains(childId)) {
			label.setTextColor(R.color.lightgrey);
			label.setBackgroundColor(R.color.gray);
		}
		else {
			label.setTextColor(R.color.black);
			label.setBackgroundColor(R.color.lightgrey);
		}
		label.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Services.currentState.getInterestSet().contains(childId)) {
					Services.currentState.removeInterestFromSet(childId);
					label.setTextColor(R.color.black);
					label.setBackgroundColor(R.color.lightgrey);
				}
				else {
					Services.currentState.addInterestToSet(childId);
					label.setTextColor(R.color.lightgrey);
					label.setBackgroundColor(R.color.gray);
				}
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		return interestMap.get(categoryList.get(arg0)).size();
	}

	@Override
	public Object getGroup(int arg0) {
		return categoryList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return categoryList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		return Interest.getInterestWithName((String) getGroup(arg0)).getValue();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		String description = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.interest_group_row, null);
		}
		TextView text = (TextView) convertView.findViewById(R.id.textGroupInterestDescription);
		text.setText(description);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

}
