package com.dreamteam.lookme;

import uk.co.senab.photoview.PhotoView;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.ProfileImage;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.Log;

public class ProfileActivity extends Fragment implements OnClickListener {

	private ViewPager profilePhoto;
	private TextView textNickname;
	private TextView textName;
	private TextView textSurname;
	private Button buttonLike;
	private Bitmap[] gallery_images;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d();
		View view = inflater.inflate(R.layout.activity_profile, null);
		textNickname = (TextView) view.findViewById(R.id.textNickname);
		textName = (TextView) view.findViewById(R.id.textName);
		textSurname = (TextView) view.findViewById(R.id.textSurname);
		buttonLike = (Button) view.findViewById(R.id.buttonLike);
		buttonLike.setOnClickListener(this);
		profilePhoto = (HackyViewPager) view.findViewById(R.id.hackyViewPager);
		return view;
	}

	public void prepareProfileAttributes(FullProfile profile) {
		if (profile != null) {
			textNickname.setText(profile.getNickname());
			textName.setText(profile.getName());
			textSurname.setText(profile.getSurname());
			gallery_images = new Bitmap[1];
			gallery_images[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile_image);
			if (profile.getProfileImages() != null) {
				gallery_images = new Bitmap[profile.getProfileImages().size()];
				int i = 0;
				for (ProfileImage image : profile.getProfileImages()) {
					Bitmap bMap = BitmapFactory.decodeByteArray(image.getImage(), 0, image.getImage().length);
					gallery_images[i] = bMap;
					i++;
				}
			}
			profilePhoto.setAdapter(new SamplePagerAdapter());
			buttonLike.setEnabled(likeButtonIsEnabledFor(Services.currentState.getProfileViewed().getId()));
		}
	}

	@Override
	public void onClick(View v) {
		Node profileNode = Services.currentState.getProfileViewed();
		Log.d("LIKE clicked on node " + profileNode.getId());
		Services.businessLogic.sendLike(profileNode.getId());
		buttonLike.setEnabled(likeButtonIsEnabledFor(Services.currentState.getProfileViewed().getId()));
		Toast.makeText(this.getActivity(), "You like " + profileNode.getProfile().getNickname(), Toast.LENGTH_LONG).show();
	}

	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return gallery_images.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			photoView.setImageBitmap(gallery_images[position]);
			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	private boolean likeButtonIsEnabledFor(String nodeId) {
		return !Services.currentState.getILikeSet().contains(nodeId);
	}
}