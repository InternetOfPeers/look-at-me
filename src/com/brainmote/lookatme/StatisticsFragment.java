package com.brainmote.lookatme;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.brainmote.lookatme.service.Event;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.ImageUtil;
import com.squareup.otto.Subscribe;

public class StatisticsFragment extends Fragment {

	private ImageView statisticsThumbnail;
	private RatingBar ratingBar;
	private TextView textScore;
	private TextView textVisitCount;
	private TextView textLikeCount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_statistics, null);
		if (((CommonActivity) getActivity()).checkIfProfileIsCompleted()) {
			statisticsThumbnail = (ImageView) view.findViewById(R.id.statisticsThumbnail);
			Bitmap bitmapThumbnail = ImageUtil.bitmapForCustomThumbnail(Services.currentState.getMyBasicProfile().getMainProfileImage().getImageBitmap(), getActivity()
					.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.edit_profile_thumbnail_size));
			statisticsThumbnail.setImageBitmap(bitmapThumbnail);

			ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
			textScore = (TextView) view.findViewById(R.id.textScore);
			textVisitCount = (TextView) view.findViewById(R.id.textVisitCount);
			textLikeCount = (TextView) view.findViewById(R.id.textLikeCount);

			refreshFragment();
		}
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		Services.event.register(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		Services.event.unregister(this);
	}

	@Subscribe
	public void onEventReceived(Event event) {
		switch (event.getEventType()) {
		case VISIT_RECEIVED:
		case LIKE_RECEIVED:
		case LIKE_RECEIVED_AND_MATCH:
			refreshFragment();
			break;
		default:
			break;
		}
	}

	private void refreshFragment() {
		ratingBar.setRating(Services.currentState.getStatistics().getRating());
		String score = "Score: " + Services.currentState.getStatistics().getScore();
		textScore.setText(score);
		String visitCount = "Your profile has been visited " + Services.currentState.getStatistics().getVisitCount() + " times";
		textVisitCount.setText(visitCount);
		String likeCount = "You received " + Services.currentState.getStatistics().getLikeCount() + " likes";
		textLikeCount.setText(likeCount);
	}

}
