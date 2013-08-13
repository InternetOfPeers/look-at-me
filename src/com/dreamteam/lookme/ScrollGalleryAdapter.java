package com.dreamteam.lookme;



import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.lookme.bean.ProfileImage;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.lookme.service.Services;

public class ScrollGalleryAdapter extends BaseAdapter{
	
	private Activity activity;
//	private int imageWidth=0;
//	private int imageHeight=0;
	
	List<ProfileImage> imageList = null;

	public ScrollGalleryAdapter(Activity activity) {
		this.activity = activity;
		imageList=imageList==null?Services.currentState.getMyFullProfile().getProfileImages():imageList;
	}	

	@Override
	public int getCount() {
		
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;		
		if(imageList.size()*80>width)			
			return imageList.size() * 10;
		else return imageList.size();
	}

	@Override
	public ProfileImage getItem(int position) {
		return imageList.get(position % imageList.size());
	}

	@Override
	public long getItemId(int position) {
		return imageList.get(position % imageList.size()).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.scroll_gallery_item, null);
		TextView title = (TextView) retval.findViewById(R.id.title);
		ImageView image = (ImageView) retval.findViewById(R.id.image);	
		image.setImageBitmap(BitmapFactory.decodeByteArray(imageList.get(position % imageList.size()).getImage(), 0, imageList.get(position % imageList.size()).getImage().length));
//		imageWidth=image.getWidth();
//		imageHeight=image.getHeight();
		image.setOnClickListener(new ImageClickListener(getItemId(position), position));
		title.setText(String.valueOf(getItemId(position)));
		
		return retval;
	}

	
	private class ImageClickListener implements OnClickListener
	{
		long imageId;
		int position;
		
		public ImageClickListener(long id,int position) {
			this.imageId=id;
			this.position=position;
		}
		
		@Override
		public void onClick(View v) {
			
			final long idSelected = imageId;
			final Dialog dialog = new Dialog(activity);
			
			// tell the Dialog to use the dialog.xml as it's layout description
			dialog.setContentView(R.layout.chosed_image_dialog);
			dialog.setTitle("What do u wanna do?");
			ImageView image = (ImageView) dialog.findViewById(R.id.image);	
			image.setImageBitmap(BitmapFactory.decodeByteArray(imageList.get(position % imageList.size()).getImage(), 0, imageList.get(position % imageList.size()).getImage().length));
			Button deleteButton = null;//(Button) dialog.findViewById(R.id.setAsMainImage);			
			deleteButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();					
					Iterator<ProfileImage>iter =imageList.iterator();
					while(iter.hasNext())
					{
						ProfileImage tempProfileImage = iter.next();
						if(tempProfileImage.getId()==idSelected)
							tempProfileImage.setMainImage(true);
						else tempProfileImage.setMainImage(false);
					}					
					notifyDataSetChanged();					
				}

			});

			Button setAsMainImageButton = (Button) dialog.findViewById(R.id.delete);
			setAsMainImageButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();					
					Iterator<ProfileImage>iter =imageList.iterator();
					int position = 0;
					while(iter.hasNext())
					{
						ProfileImage tempProfileImage = iter.next();
						if(tempProfileImage.getId()==idSelected)
							break;
						position++;
					}					
					if(imageList.get(position).isMainImage())
					{
						Toast.makeText(activity, "YOU CAN'T DELETE YOUR MAIN IMAGE!CHOOSE ANOTHER ONE FIRST.", Toast.LENGTH_SHORT).show();;
						return;
					}
					try{
						DBOpenHelper db=DBOpenHelperImpl.getInstance(activity);
						db.deleteImage(imageList.remove(position).getId());						
					}catch(Exception e)
					{
						Log.e("imageGallery","error during delete of image");
					}
					notifyDataSetChanged();
				}
				

			});
			dialog.show();	
			
		}
		
	}


}
