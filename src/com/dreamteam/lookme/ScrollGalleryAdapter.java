package com.dreamteam.lookme;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dreamteam.lookme.bean.ProfileImage;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.util.ImageUtil;

public class ScrollGalleryAdapter extends BaseAdapter {

	private Activity activity;
	// private int imageWidth=0;
	// private int imageHeight=0;

	List<ProfileImage> imageList = null;

	public ScrollGalleryAdapter(Activity activity) {
		this.activity = activity;
		//imageList = imageList == null ? Services.currentState.getMyFullProfile().getProfileImages() : imageList;
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
		Bitmap thumbnailBitmap = ImageUtil.bitmapForThumbnail(BitmapFactory.decodeByteArray(getItem(position).getImage(), 0,
				getItem(position).getImage().length));
		image.setImageBitmap(thumbnailBitmap);
		// imageWidth=image.getWidth();
		// imageHeight=image.getHeight();
		image.setOnClickListener(new ImageClickListener(position));

		return retval;
	}
	
	public void setProfileImageList(List<ProfileImage> imageList) {
		this.imageList = imageList;
	}

	private class ImageClickListener implements OnClickListener {
		//long imageId;
		//int position;
		ProfileImage clickedImage;

		public ImageClickListener(int position) {
			//this.imageId = getItemId(position);
			this.clickedImage = getItem(position);
		}

		@Override
		public void onClick(View v) {

			//final long idSelected = imageId;
			final Dialog dialog = new Dialog(activity);

			// tell the Dialog to use the dialog.xml as it's layout description
			dialog.setContentView(R.layout.chosed_image_dialog);
			dialog.setTitle("What do u wanna do?");
			
			Bitmap thumbnailBitmap = ImageUtil.bitmapForThumbnail(BitmapFactory.decodeByteArray(clickedImage.getImage(), 0, clickedImage.getImage().length));
			ImageView image = (ImageView) dialog.findViewById(R.id.image);
			image.setImageBitmap(thumbnailBitmap);
			
			Button setAsMainImageButton = (Button) dialog.findViewById(R.id.setAsMainImage);
			setAsMainImageButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Iterator<ProfileImage> iter = imageList.iterator();
					while (iter.hasNext()) {
						ProfileImage tempProfileImage = iter.next();
						if (tempProfileImage.equals(clickedImage)) {
							tempProfileImage.setMainImage(true);
							ImageView imageView = (ImageView) activity.findViewById(R.id.imgView);
							Bitmap thumbnailBitmap = ImageUtil.bitmapForThumbnail(tempProfileImage.getImageBitmap());
							imageView.setImageBitmap(thumbnailBitmap);
						}
						else {
							tempProfileImage.setMainImage(false);
						}
					}
					notifyDataSetChanged();
				}

			});

			Button deleteImageButton = (Button) dialog.findViewById(R.id.delete);
			deleteImageButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Iterator<ProfileImage> iter = imageList.iterator();
					//int position = 0;
					if (clickedImage.isMainImage()) {
						Toast.makeText(activity, "YOU CAN'T DELETE YOUR MAIN IMAGE!CHOOSE ANOTHER ONE FIRST.", Toast.LENGTH_SHORT).show();
						return;
					}
//					while (iter.hasNext()) {
//						ProfileImage tempProfileImage = iter.next();
//						if (tempProfileImage.equals(clickedImage))
//							break;
//						position++;
//					}
					try {
						// TODO: perché non posticipare i cambiamenti sul db solo al momento del salvataggio?
						if (clickedImage.getId() != 0) {
							DBOpenHelper db = DBOpenHelperImpl.getInstance(activity);
							db.deleteImage(clickedImage.getId());
						}
						imageList.remove(clickedImage);
					} catch (Exception e) {
						Log.e("imageGallery", "error during delete of image");
					}
					notifyDataSetChanged();
				}

			});
			dialog.show();

		}

	}

}
