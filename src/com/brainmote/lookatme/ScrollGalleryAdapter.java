package com.brainmote.lookatme;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.brainmote.lookatme.bean.ProfileImage;
import com.brainmote.lookatme.db.DBOpenHelper;
import com.brainmote.lookatme.db.DBOpenHelperImpl;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.ImageUtil;
import com.brainmote.lookatme.util.Log;

public class ScrollGalleryAdapter extends BaseAdapter {

	private Activity activity;

	List<ProfileImage> imageList = null;

	public ScrollGalleryAdapter(Activity activity) {
		this.activity = activity;
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
		image.setOnClickListener(new ImageClickListener(position));
		return retval;
	}

	public void setProfileImageList(List<ProfileImage> imageList) {
		this.imageList.clear();
		for (ProfileImage image : imageList) {
			this.imageList.add(image);
		}
		notifyDataSetChanged();
	}

	private class ImageClickListener implements OnClickListener {

		ProfileImage clickedImage;

		public ImageClickListener(int position) {
			this.clickedImage = getItem(position);
		}

		@Override
		public void onClick(View v) {
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.dialog_manage_image, null);
			dialogBuilder.setView(view);
			dialogBuilder.setTitle(activity.getResources().getString(R.string.dialog_image_management));
			Bitmap thumbnailBitmap = ImageUtil.bitmapForCustomThumbnail(clickedImage.getImageBitmap(), Services.currentState.getContext().getResources()
					.getDimensionPixelSize(R.dimen.manage_image_thumbnail_size));
			ImageView image = (ImageView) view.findViewById(R.id.image);
			image.setImageBitmap(thumbnailBitmap);
			final RadioButton radioSetAsMain = (RadioButton) view.findViewById(R.id.radioButtonSetAsMain);
			final RadioButton radioDelete = (RadioButton) view.findViewById(R.id.radioButtonDelete);
			radioSetAsMain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton button, boolean checked) {
					if (checked) {
						radioDelete.setChecked(false);
					}
				}
			});
			radioDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton button, boolean checked) {
					if (checked) {
						radioSetAsMain.setChecked(false);
					}
				}
			});
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (radioSetAsMain.isChecked()) {
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
						EditProfileActivity parent = (EditProfileActivity) activity;
						parent.saveProfile();
						notifyDataSetChanged();
					}
					else if (radioDelete.isChecked()) {
						if (clickedImage.isMainImage()) {
							EditProfileActivity parent = (EditProfileActivity) activity;
							parent.showDialog(parent.getResources().getString(R.string.message_warning),
									parent.getResources().getString(R.string.message_cant_delete_profile_image));
							return;
						}
						try {
							if (clickedImage.getId() != 0) {
								DBOpenHelper db = DBOpenHelperImpl.getInstance(activity);
								db.deleteImage(clickedImage.getId());
							}
							imageList.remove(clickedImage);
						} catch (Exception e) {
							Log.e("error during delete of image");
						}
						EditProfileActivity parent = (EditProfileActivity) activity;
						parent.saveProfile();
						notifyDataSetChanged();
					}
					dialog.dismiss();
				}
			});
			dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			dialogBuilder.create().show();
		}

	}

}
