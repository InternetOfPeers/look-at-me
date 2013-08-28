package com.brainmote.lookatme;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.Toast;

import com.brainmote.lookatme.bean.FullProfile;
import com.brainmote.lookatme.bean.ProfileImage;
import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.constants.AppSettings;
import com.brainmote.lookatme.service.Event;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.CommonUtils;
import com.brainmote.lookatme.util.ImageUtil;
import com.brainmote.lookatme.util.Log;
import com.brainmote.lookatme.util.Nav;
import com.squareup.otto.Subscribe;

public class ProfileFragment extends Fragment {

	private ViewPager profilePhoto;
	private ImageButton buttonLike;
	private ImageButton buttonChat;
	private List<Bitmap> gallery_images;

	private ProgressDialog loadingDialog;
	private boolean profileReady;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d();
		profileReady = false;
		View view = inflater.inflate(R.layout.fragment_profile, null);
		profilePhoto = (HackyViewPager) view.findViewById(R.id.hackyViewPager);
		gallery_images = new ArrayList<Bitmap>();
		profilePhoto.setAdapter(new SamplePagerAdapter());
		// recupero il node id
		Bundle parameters = getActivity().getIntent().getExtras();
		final String nodeId = parameters.getString(Nav.NODE_KEY_ID);
		// preparo i pulsanti
		buttonLike = (LikeButton) view.findViewById(R.id.buttonLike);
		buttonLike.setEnabled(false); // waiting for PROFILE_RECEIVED event
		buttonLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.businessLogic.sendLike(nodeId);
				buttonLike.setEnabled(false);
				Toast.makeText(getActivity(), "You like " + Services.currentState.getSocialNodeMap().findNodeByNodeId(nodeId).getProfile().getNickname(), Toast.LENGTH_LONG)
						.show();
			}
		});
		buttonChat = (ImageButton) view.findViewById(R.id.buttonChat);
		buttonChat.setEnabled(false); // waiting for PROFILE_RECEIVED event
		buttonChat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Services.businessLogic.startChat(nodeId);
				Bundle parameters = new Bundle();
				parameters.putString(
						Nav.CONVERSATION_KEY_ID,
						CommonUtils.getConversationId(Services.currentState.getMyBasicProfile().getId(),
								Services.currentState.getSocialNodeMap().getProfileIdByNodeId(nodeId)));
				Nav.startActivityWithParameters(getActivity(), ChatMessagesActivity.class, parameters);
			}
		});
		// Verifico se è il profilo di un utente fake
		if (AppSettings.fakeUsersEnabled && Services.businessLogic.isFakeUserNode(nodeId)) {
			Services.currentState.setProfileViewed(Services.businessLogic.getFakeUser().getNode());
			prepareProfileAttributes();
		} else {
			// Controllo se il nodo è ancora presente
			final CommonActivity activity = (CommonActivity) getActivity();
			if (Services.businessLogic.isNodeConnected(nodeId)) {
				// invio la richiesta di full profile
				Services.businessLogic.requestFullProfile(nodeId);
				// apro un dialog di attesa se trascorre troppo tempo dalla
				// richiesta
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (!profileReady) {
							loadingDialog = new ProgressDialog(getActivity());
							loadingDialog.setMessage(getActivity().getResources().getString(R.string.loading_profile_message));
							loadingDialog.setCancelable(false);
							loadingDialog.show();
							// Dopo AppSettings.LOADING_PROFILE_TIMEOUT
							// millisecondi, se il popup di caricamento non è
							// stato chiuso, viene chiuso in automatico e viene
							// mostrato il messaggio di errore all'utente.
							new Handler().postDelayed(new Runnable() {
								public void run() {
									if (loadingDialog.isShowing()) {
										loadingDialog.dismiss();
										activity.showDialog(activity.getString(R.string.no_profile_title), activity.getString(R.string.no_profile_message),
												activity.getString(R.string.no_profile_button_label), NearbyActivity.class, true, true);
									}
								}
							}, AppSettings.LOADING_PROFILE_TIMEOUT);
						}
					}
				}, AppSettings.WAIT_BEFORE_SHOWING_LOADING_PROFILE_DIALOG);

			} else {
				activity.showDialog(activity.getString(R.string.no_profile_title), activity.getString(R.string.no_profile_message),
						activity.getString(R.string.no_profile_button_label), NearbyActivity.class, true, true);
			}
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
			if (loadingDialog != null)
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
			buttonChat.setEnabled(true);
			// Imposto il title con il nickname e l'et� dell'utente
			// selezionato
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
		}
		profilePhoto.getAdapter().notifyDataSetChanged();
		profileReady = true;
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
			Bitmap photoImage = gallery_images.get(position);
			// Crop to get same ratio
			photoView.setImageBitmap(ImageUtil.bitmapForGallery(photoImage));
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
