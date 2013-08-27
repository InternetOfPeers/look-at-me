package com.brainmote.lookatme;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class LikeButton extends ImageButton {

	public LikeButton(Context context) {
		super(context);
	}

	public LikeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LikeButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) {
			setImageDrawable(getResources().getDrawable(R.drawable.love_icon));
		} else {
			setImageDrawable(getResources().getDrawable(R.drawable.love_icon_grey));
		}
	}

}
