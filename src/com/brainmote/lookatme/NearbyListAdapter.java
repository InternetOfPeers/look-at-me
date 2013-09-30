package com.brainmote.lookatme;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.ImageUtil;

public class NearbyListAdapter extends BaseAdapter {

	private Activity activity;

	public NearbyListAdapter(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return Services.currentState.getSocialNodeMap().size();
	}

	@Override
	public Node getItem(int position) {
		List<Node> nodeList = Services.currentState.getSocialNodeMap().getNodeList();
		return nodeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// LayoutInflater class is used to instantiate layout XML file
			// into its corresponding View objects.
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.fragment_nearby_list_item, null);
		}
		// Imposto l'immagine del profilo
		ImageView photoImage = (ImageView) convertView.findViewById(R.id.profilePhotoImage);
		photoImage.setTag(position);
		// ProgressBar loadingImage = (ProgressBar)
		// convertView.findViewById(R.id.loadingImageProgressBar);
		BasicProfile profile = (BasicProfile) getItem(position).getProfile();
		// ImageLoader imageLoader = new ImageLoader(photoImage,loadingImage,
		// position,profile, activity);
		// imageLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

		Bitmap mainImageProfile = ImageUtil.getBitmapProfileImage(activity.getResources(), profile);
		Bitmap croppedImageProfile = ImageUtil.bitmapForThumbnail(mainImageProfile);
		photoImage.setImageBitmap(croppedImageProfile);
		// Se gli interessi matchano allora evidenzio il profilo
		if (Services.currentState.checkInterestMatch(profile)) {
			photoImage.setBackgroundResource(R.drawable.image_borders_match);
		}
		// Imposto i liked
		ImageView likedImage = (ImageView) convertView.findViewById(R.id.imageLiked);
		if (Services.currentState.getLikedSet().contains(getItem(position).getProfile().getId())) {
			likedImage.setImageDrawable(activity.getResources().getDrawable(R.drawable.love_icon_small));
		} else {
			// remove
			likedImage.setImageResource(android.R.color.transparent);
		}
		return convertView;
	}

	public class ImageLoader extends AsyncTask<String, Integer, Bitmap> {

		// private final WeakReference<ImageView> imageViewReference;
		// private final WeakReference<ProgressBar> loadingImageReference;

		private final ImageView imageViewReference;
		private final ProgressBar loadingImageReference;
		private final int position;

		private final BasicProfile profile;

		public ImageLoader(ImageView imageView, ProgressBar loadingImage, int position, BasicProfile profile, Context context) {
			imageViewReference = (imageView);
			loadingImageReference = (loadingImage);
			this.position = position;
			this.profile = profile;

		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap returnBitmap = null;
			try {

				Bitmap mainImageProfile = ImageUtil.getBitmapProfileImage(activity.getResources(), profile);
				returnBitmap = ImageUtil.bitmapForThumbnail(mainImageProfile);

			} catch (Exception e) {
				Log.e("async loading class", e.toString());
			}
			return returnBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			ImageView imageView = imageViewReference;
			if (imageView != null && ((Integer) imageView.getTag() == this.position)) {
				imageView.setImageBitmap(bitmap);
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

				imageViewReference.setVisibility(View.VISIBLE);
				loadingImageReference.setVisibility(View.INVISIBLE);
			}

		}

	}

}
