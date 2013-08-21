package com.brainmote.lookatme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageSpinnerAdapter extends ArrayAdapter<String> {

	private String[] textSpinnerArray = null;
	private Integer[] imageSpinnerArray = null;

	public ImageSpinnerAdapter(Context context, int textViewResourceId, String[] textsIdArray, Integer[] imagesIdArray) {
		super(context, textViewResourceId, textsIdArray);
		textSpinnerArray = textsIdArray;
		imageSpinnerArray = imagesIdArray;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public int getPosition(String value) {
		int pos = 0;
		for (int i = 0; i < textSpinnerArray.length; i++) {
			if (textSpinnerArray[i] != null && textSpinnerArray[i].equals(value)) {
				pos = i;
				break;
			}
		}
		return pos;
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(this.getContext());
		View row = inflater.inflate(R.layout.spinner_custom, parent, false);

		TextView sub = (TextView) row.findViewById(R.id.textView2);
		sub.setText(textSpinnerArray[position]);

		ImageView icon = (ImageView) row.findViewById(R.id.imageView1);
		icon.setImageResource(imageSpinnerArray[position]);

		// ImageView icon = (ImageView) row.findViewById(R.id.imageView1);
		// icon.setImageDrawable(ImageUtil.loadImageFromAsset(this.getContext(),
		// genderArray[position] + ".png"));

		return row;
	}

}
