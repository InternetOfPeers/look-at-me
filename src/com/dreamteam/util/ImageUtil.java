package com.dreamteam.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.dreamteam.lookme.R;
import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.ProfileImage;

public class ImageUtil {

	public static int getScale(int originalWidth, int originalHeight, final int requiredWidth, final int requiredHeight) {
		// a scale of 1 means the original dimensions
		// of the image are maintained
		int scale = 1;

		// calculate scale only if the height or width of
		// the image exceeds the required value.
		if ((originalWidth > requiredWidth) || (originalHeight > requiredHeight)) {
			// calculate scale with respect to
			// the smaller dimension
			if (originalWidth < originalHeight)
				scale = Math.round((float) originalWidth / requiredWidth);
			else
				scale = Math.round((float) originalHeight / requiredHeight);
		}
		return scale;
	}

	public static BitmapFactory.Options getOptions(String filePath, int requiredWidth, int requiredHeight) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		// setting inJustDecodeBounds to true
		// ensures that we are able to measure
		// the dimensions of the image,without
		// actually allocating it memory
		options.inJustDecodeBounds = true;

		// decode the file for measurement
		BitmapFactory.decodeFile(filePath, options);

		// obtain the inSampleSize for loading a
		// scaled down version of the image.
		// options.outWidth and options.outHeight
		// are the measured dimensions of the
		// original image
		options.inSampleSize = getScale(options.outWidth, options.outHeight, requiredWidth, requiredHeight);

		// set inJustDecodeBounds to false again
		// so that we can now actually allocate the
		// bitmap some memory
		options.inJustDecodeBounds = false;

		return options;

	}

	public static Bitmap loadBitmap(String filePath, int requiredWidth, int requiredHeight) {

		BitmapFactory.Options options = getOptions(filePath, requiredWidth, requiredHeight);

		return BitmapFactory.decodeFile(filePath, options);
	}

	public static byte[] getImageFromPicturePath(String picturePath) {
		try {
			Bitmap b = ImageUtil.loadBitmap(picturePath, 512, 256);
			b = getResizedBitmap(b, 256, 256);

			return bitmapToByteArray(b);

		} catch (Exception e) {
			Log.e("image", e.getMessage());
		}
		return null;
	}

	public static byte[] bitmapToByteArray(Bitmap bitmap) {
		int size = bitmap.getWidth() * bitmap.getHeight() * 2;
		ByteArrayOutputStream outStream;
		while (true) {
			outStream = new ByteArrayOutputStream(size);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 0, outStream))
				break;
			size = size * 3 / 2;
		}
		return outStream.toByteArray();
	}

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		return Bitmap.createScaledBitmap(bm, newWidth, newHeight, false);
	}

	public static Bitmap getBitmapProfileImage(Resources resources, BasicProfile profile) {
		ProfileImage profileImage = profile.getMainProfileImage();
		if (profileImage == null)
			return BitmapFactory.decodeResource(resources, R.drawable.ic_profile_image);
		byte[] profileImageByteArray = profileImage.getImage();
		if (profileImageByteArray == null) {
			return BitmapFactory.decodeResource(resources, R.drawable.ic_profile_image);
		}
		return BitmapFactory.decodeByteArray(profileImageByteArray, 0, profileImageByteArray.length);
	}
	
    public static Drawable loadImageFromAsset(Context context,String imageFileName) {

        // load image
        try {
            // get input stream
            InputStream ims = context.getAssets().open("avatar.jpg");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            return d;
        }
        catch(IOException ex) {
            Log.e("ImageUtil", "unable to load image! name: "+ imageFileName);
        }
        return null;
 
    }

}
