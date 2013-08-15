package com.dreamteam.lookme;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.ProfileImage;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.constants.AppSettings;
import com.dreamteam.lookme.service.Event;
import com.dreamteam.lookme.service.NotificationService;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;
import com.squareup.otto.Subscribe;

public class ProfileFragment extends Fragment {

	private ViewPager profilePhoto;
	private ImageButton buttonLike;
	private ImageButton buttonChat;
	private List<Bitmap> gallery_images;

	private ProgressDialog loadingDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d();
		View view = inflater.inflate(R.layout.fragment_profile, null);
		buttonLike = (ImageButton) view.findViewById(R.id.buttonLike);
		buttonChat = (ImageButton) view.findViewById(R.id.buttonChat);
		profilePhoto = (HackyViewPager) view.findViewById(R.id.hackyViewPager);
		profilePhoto.setAdapter(new SamplePagerAdapter());
		// recupero il node id, entro in attesa e invio la richiesta di full
		// profile
		Bundle parameters = getActivity().getIntent().getExtras();
		final String nodeId = parameters.getString(Nav.PROFILE_ID_KEY);
		buttonLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.businessLogic.sendLike(nodeId);
				buttonLike.setEnabled(likeButtonIsEnabledFor(nodeId));
				// change image to disabled button
				buttonLike.setImageDrawable(getResources().getDrawable(R.drawable.love_icon_grey));
				Toast.makeText(getActivity(), "You like " + Services.currentState.getSocialNodeMap().get(nodeId).getProfile().getNickname(), Toast.LENGTH_LONG).show();
			}
		});

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

		if (AppSettings.isFakeUserEnabled && nodeId.equals(Services.businessLogic.getFakeUser().getNode().getId())) {
			Services.currentState.setProfileViewed(Services.businessLogic.getFakeUser().getNode());
			prepareProfileAttributes();
		} else {
			Services.businessLogic.requestFullProfile(nodeId);
			loadingDialog = new ProgressDialog(getActivity());
			loadingDialog.setTitle("Loading profile");
			loadingDialog.show();
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
		gallery_images = new ArrayList<Bitmap>();
		if (profile != null) {
			// Imposto il title con il nickname e l'età dell'utente selezionato
			String age = profile.getAge() > 0 ? ", " + String.valueOf(profile.getAge()) : "";
			getActivity().setTitle(profile.getNickname() + age);
			// Imposto le immagini del profilo utente
			if (profile.getProfileImages() != null) {
				for (ProfileImage image : profile.getProfileImages()) {
					gallery_images.add(BitmapFactory.decodeByteArray(image.getImage(), 0, image.getImage().length));
				}
			} else {
				gallery_images.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile_image));
			}
			buttonLike.setEnabled(likeButtonIsEnabledFor(Services.currentState.getProfileViewed().getId()));
			if (!likeButtonIsEnabledFor(Services.currentState.getProfileViewed().getId())) {
				// change image to disabled button
				buttonLike.setImageDrawable(getResources().getDrawable(R.drawable.love_icon_grey));
			}
		}
	}

	private boolean likeButtonIsEnabledFor(String nodeId) {
		return !Services.currentState.getILikeSet().contains(nodeId);
	}

	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return gallery_images.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			// Get bitmap and crop to fill display size
			Display display = getActivity().getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			Log.d("Display size is " + size.x + " x " + size.y);
			float width = (float) size.x;
			float height = (float) size.y;
			float ratio = width / height;
			Log.d("Display ratio is " + ratio);
			Bitmap photoImageSrc = gallery_images.get(position);
			Log.d("Bitmap size is " + photoImageSrc.getWidth() + " x " + photoImageSrc.getHeight());
			int newWidth = (int) (photoImageSrc.getHeight() * ratio);
			Log.d("New width is " + newWidth);
			int offset = photoImageSrc.getWidth() / 2 - newWidth / 2;
			Log.d("Offset is " + offset);
			Bitmap photoImageDst = Bitmap.createBitmap(photoImageSrc, offset, 0, newWidth, photoImageSrc.getHeight());
			photoView.setImageBitmap(photoImageDst);
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

}
