package com.brainmote.lookatme;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.brainmote.lookatme.bean.ProfileImage;
import com.brainmote.lookatme.db.DBOpenHelper;
import com.brainmote.lookatme.db.DBOpenHelperImpl;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.ImageUtil;
import com.brainmote.lookatme.util.Log;

public class ScrollGalleryAdapter extends BaseAdapter {

	private Activity activity;
	// private int imageWidth=0;
	// private int imageHeight=0;

	List<ProfileImage> imageList = null;

	public ScrollGalleryAdapter(Activity activity) {
		this.activity = activity;
		// imageList = imageList == null ?
		// Services.currentState.getMyFullProfile().getProfileImages() :
		// imageList;
		imageList = new ArrayList<ProfileImage>();
	}

	@Override
	public int getCount() {

		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		if (imageList.size() * 80 > width)
			return imageList.size() * 10;
		else
			return imageList.size();
	}

	@Override
	public ProfileImage getItem(int position) {
		return imageList.get(position % imageList.size());
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.scroll_gallery_item, null);
		ImageView image = (ImageView) retval.findViewById(R.id.image);
		// TODO: migliorare l'efficienza!!!
		Bitmap thumbnailBitmap = ImageUtil.bitmapForCustomThumbnail(getItem(position).getImageBitmap(), Services.currentState.getContext().getResources()
				.getDimensionPixelSize(R.dimen.edit_profile_scroll_thumbnail_size));
		image.setImageBitmap(thumbnailBitmap);
		// imageWidth=image.getWidth();
		// imageHeight=image.getHeight();
		image.setOnClickListener(new ImageClickListener(position));

		return retval;
	}

	public void setProfileImageList(List<ProfileImage> imageList) {
		this.imageList.clear();
		for (ProfileImage image : imageList) {
			this.imageList.add(image);
		}
		// this.imageList = imageList;
		notifyDataSetChanged();
	}

	private class ImageClickListener implements OnClickListener {

		ProfileImage clickedImage;

		public ImageClickListener(int position) {
			this.clickedImage = getItem(position);
		}

		@Override
		public void onClick(View v) {

			// final long idSelected = imageId;
			final Dialog dialog = new Dialog(activity);

			// tell the Dialog to use the dialog.xml as it's layout description
			dialog.setContentView(R.layout.dialog_manage_image);
			dialog.setTitle("What do u wanna do?");

			Bitmap thumbnailBitmap = ImageUtil.bitmapForCustomThumbnail(clickedImage.getImageBitmap(), Services.currentState.getContext().getResources()
					.getDimensionPixelSize(R.dimen.manage_image_thumbnail_size));
			ImageView image = (ImageView) dialog.findViewById(R.id.image);
			image.setImageBitmap(thumbnailBitmap);

			ImageButton mainImageButton = (ImageButton) dialog.findViewById(R.id.mainImageButton);
			mainImageButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Iterator<ProfileImage> iter = imageList.iterator();
					while (iter.hasNext()) {
						ProfileImage tempProfileImage = iter.next();
						if (tempProfileImage.equals(clickedImage)) {
							tempProfileImage.setMainImage(true);
							EditProfileActivity editProfileActivity = (EditProfileActivity) activity;
							editProfileActivity.setMainProfileImage(tempProfileImage);
						} else {
							tempProfileImage.setMainImage(false);
						}
					}
					notifyDataSetChanged();
				}

			});

			ImageButton deleteImageButton = (ImageButton) dialog.findViewById(R.id.deleteImageButton);
			deleteImageButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					if (clickedImage.isMainImage()) {
						EditProfileActivity parent = (EditProfileActivity)activity;
						parent.showDialog(parent.getResources().getString(R.string.message_warning), parent.getResources().getString(R.string.message_cant_delete_profile_image));
						return;
//						Toast.makeText(activity, R.string.message_cant_delete_profile_image, Toast.LENGTH_LONG).show();
//						return;
					}
					try {
						// TODO: perché non posticipare i cambiamenti sul db
						// solo al momento del salvataggio?
						if (clickedImage.getId() != 0) {
							DBOpenHelper db = DBOpenHelperImpl.getInstance(activity);
							db.deleteImage(clickedImage.getId());
						}
						imageList.remove(clickedImage);
					} catch (Exception e) {
						Log.e("error during delete of image");
					}
					notifyDataSetChanged();
				}

			});
			dialog.show();

		}

	}

}
