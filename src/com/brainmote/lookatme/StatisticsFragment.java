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

import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.ImageUtil;

public class StatisticsFragment extends Fragment {

	private ImageView statisticsThumbnail;
	private RatingBar ratingBar;
	private TextView textScore;
	private TextView textVisitCount;
	private TextView textLikeCount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_statistics, null);

		statisticsThumbnail = (ImageView) view.findViewById(R.id.statisticsThumbnail);
		Bitmap bitmapThumbnail = ImageUtil.bitmapForCustomThumbnail(Services.currentState.getMyBasicProfile().getMainProfileImage().getImageBitmap(), getActivity()
				.getApplicationContext().getResources().getDimensionPixelSize(R.dimen.edit_profile_thumbnail_size));
		statisticsThumbnail.setImageBitmap(bitmapThumbnail);

		ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
		ratingBar.setRating(Services.currentState.getStatistics().getRating());

		textScore = (TextView) view.findViewById(R.id.textScore);
		String score = "Score: " + Services.currentState.getStatistics().getScore();
		textScore.setText(score);

		textVisitCount = (TextView) view.findViewById(R.id.textVisitCount);
		String visitCount = "Your profile has been visited " + Services.currentState.getStatistics().getVisitCount() + " times";
		textVisitCount.setText(visitCount);

		textLikeCount = (TextView) view.findViewById(R.id.textLikeCount);
		String likeCount = "You received " + Services.currentState.getStatistics().getLikeCount() + " likes";
		textLikeCount.setText(likeCount);

		return view;
	}

}
