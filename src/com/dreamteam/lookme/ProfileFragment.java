package com.dreamteam.lookme;

import uk.co.senab.photoview.PhotoView;
import android.app.Fragment;
import android.app.ProgressDialog;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.ProfileImage;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.service.Event;
import com.dreamteam.lookme.service.NotificationService;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;
import com.squareup.otto.Subscribe;

public class ProfileFragment extends Fragment implements OnClickListener {

	private ViewPager profilePhoto;
	private TextView textNickname;
	// private TextView textName;
	// private TextView textSurname;
	private ImageButton buttonLike;
	private ImageButton buttonChat;
	private Bitmap[] gallery_images;

	private ProgressDialog loadingDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d();
		View view = inflater.inflate(R.layout.fragment_profile, null);
		textNickname = (TextView) view.findViewById(R.id.textNickname);
		// textName = (TextView) view.findViewById(R.id.textName);
		// textSurname = (TextView) view.findViewById(R.id.textSurname);
		buttonLike = (ImageButton) view.findViewById(R.id.buttonLike);
		buttonChat = (ImageButton) view.findViewById(R.id.buttonChat);
		buttonLike.setOnClickListener(this);
		profilePhoto = (HackyViewPager) view.findViewById(R.id.hackyViewPager);

		// recupero il node id, entro in attesa e invio la richiesta di full
		// profile
		Bundle parameters = getActivity().getIntent().getExtras();
		final String nodeId = parameters.getString(Nav.PROFILE_ID_KEY);

		buttonChat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.businessLogic.startChat(nodeId);
				Bundle parameters = new Bundle();
				parameters.putString(
						NotificationService.CONVERSATION_KEY_ID,
						CommonUtils.getConversationId(Services.currentState.getMyBasicProfile().getId(), Services.currentState.getSocialNodeMap().get(nodeId).getProfile()
								.getId()));
				Nav.startActivityWithParameters(getActivity(), ChatMessagesActivity.class, parameters);
			}
		});
		Services.businessLogic.requestFullProfile(nodeId);
		loadingDialog = new ProgressDialog(getActivity());
		loadingDialog.setTitle("Loading profile");
		loadingDialog.show();

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
		case PROFILE_RECEIVED:
			prepareProfileAttributes();
			loadingDialog.dismiss();
			break;
		default:
			break;
		}
	}

	public void prepareProfileAttributes() {
		Node profileNode = Services.currentState.getProfileViewed();
		FullProfile profile = (FullProfile) profileNode.getProfile();
		if (profile != null) {
			textNickname.setText(profile.getNickname());
			// textName.setText(profile.getName());
			// textSurname.setText(profile.getSurname());
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
			if (!likeButtonIsEnabledFor(Services.currentState.getProfileViewed().getId())) {
				// change image to disabled button
				buttonLike.setImageDrawable(getResources().getDrawable(R.drawable.love_icon_grey));
			}
		}
	}

	@Override
	public void onClick(View v) {
		// LIKE button clicked
		Node profileNode = Services.currentState.getProfileViewed();
		Log.d("LIKE clicked on node " + profileNode.getId());
		Services.businessLogic.sendLike(profileNode.getId());
		buttonLike.setEnabled(likeButtonIsEnabledFor(Services.currentState.getProfileViewed().getId()));
		// change image to disabled button
		buttonLike.setImageDrawable(getResources().getDrawable(R.drawable.love_icon_grey));
		Toast.makeText(getActivity(), "You like " + profileNode.getProfile().getNickname(), Toast.LENGTH_LONG).show();
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
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

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