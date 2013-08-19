package com.dreamteam.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.DisplayMetrics;

import com.dreamteam.lookme.R;
import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.ProfileImage;
import com.dreamteam.lookme.service.Services;

public class ImageUtil {

	private static final int MAX_IMAGE_SIZE = 1080;
	private static final int DEFAULT_SIZE_IN_DP = 600;
	private static final int ASPECT_THUMBNAIL = 1;
	private static final int ASPECT_WIDTH = 2;
	private static final int ASPECT_HEIGHT = 3;
	private static final int JPEG_COMPRESSION_RATIO = 90;

	public static BitmapFactory.Options getOptions(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		if (options.outHeight > MAX_IMAGE_SIZE || options.outWidth > MAX_IMAGE_SIZE) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) options.outHeight / (float) MAX_IMAGE_SIZE);
			final int widthRatio = Math.round((float) options.outWidth / (float) MAX_IMAGE_SIZE);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			options.inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		} else {
			options.inSampleSize = 1;
		}

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return options;

	}

	public static Bitmap loadBitmap(String filePath) {
		BitmapFactory.Options options = getOptions(filePath);
		Bitmap bitmap = ImageUtil.scaleImage(BitmapFactory.decodeFile(filePath, options), DEFAULT_SIZE_IN_DP);

		int rotate = 0;
		ExifInterface exif = null;
		try {
			exif = new ExifInterface(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			}

			Log.d("Exif orientation is " + orientation);
		}
		if (rotate != 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(rotate);
			return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		}
		return bitmap;
	}

	public static Bitmap bitmapForThumbnail(Bitmap bitmap) {
		DisplayMetrics displayMetrics = Services.currentState.getContext().getResources().getDisplayMetrics();
		int display_size_in_dp = Math.round(displayMetrics.widthPixels / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		int thumbnail_size_in_dp = (display_size_in_dp - 50) / 4;
		return scaleImage(cropBitmap(bitmap, ASPECT_THUMBNAIL, ASPECT_THUMBNAIL), thumbnail_size_in_dp);
	}

	public static Bitmap bitmapForCustomThumbnail(Bitmap bitmap, int dp) {
		return scaleImage(cropBitmap(bitmap, ASPECT_THUMBNAIL, ASPECT_THUMBNAIL), dp);
	}

	public static Bitmap bitmapForGallery(Bitmap bitmap) {
		return cropBitmap(bitmap, ASPECT_WIDTH, ASPECT_HEIGHT);
	}

	public static byte[] bitmapToByteArray(Bitmap bitmap) {
		// int size = bitmap.getWidth() * bitmap.getHeight() * 2;
		// ByteArrayOutputStream outStream;
		// while (true) {
		// outStream = new ByteArrayOutputStream(size);
		// if (bitmap.compress(Bitmap.CompressFormat.JPEG,
		// JPEG_COMPRESSION_RATIO, outStream))
		// break;
		// size = size * 3 / 2;
		// }
		// return outStream.toByteArray();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_COMPRESSION_RATIO, outStream);
		return outStream.toByteArray();
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

	private static Bitmap cropBitmap(Bitmap bitmap, int aspectWidth, int aspectHeight) {
		// Assumo che la bitmap di partenza abbia altezza >= larghezza
		Log.d("Cropping bitmap " + bitmap.getWidth() + " x " + bitmap.getHeight());
		int newWidth = bitmap.getWidth();
		int newHeight = bitmap.getHeight();
		int offsetX = 0;
		int offsetY = 0;
		// Calcolo il rapporto destinazione e sorgente
		float ratioSrc = (float) bitmap.getWidth() / (float) bitmap.getHeight();
		float ratioDst = (float) aspectWidth / (float) aspectHeight;
		Log.d("Src ratio is " + ratioSrc);
		Log.d("Dst ratio is " + ratioDst);

		if (ratioSrc > ratioDst) {
			// l'immagine va stretta
			Log.d("Reducing width");
			newWidth = (int) (bitmap.getHeight() * ratioDst);
			offsetX = (bitmap.getWidth() - newWidth) / 2;
		} else if (ratioSrc < ratioDst) {
			// l'immagine va accorciata
			Log.d("Reducing height");
			newHeight = (int) (bitmap.getWidth() / ratioDst);
			offsetY = (bitmap.getHeight() - newHeight) / 2;
		} else if (ratioSrc == ratioDst) {
			// NOP
			Log.d("No need to reduce image");
			return bitmap;
		}
		Log.d("New width is " + newWidth);
		Log.d("New height is " + newHeight);
		Log.d("Offset X is " + offsetX);
		Log.d("Offset Y is " + offsetY);
		// I seguenti 4 controlli per evitare di avere valori negativi
		if (offsetX < 0)
			offsetX = 0;
		if (offsetY < 0)
			offsetY = 0;
		if (newWidth > bitmap.getWidth())
			newWidth = bitmap.getWidth();
		if (newHeight > bitmap.getHeight())
			newHeight = bitmap.getHeight();
		Bitmap photoImageDst = Bitmap.createBitmap(bitmap, offsetX, offsetY, newWidth, newHeight);
		return photoImageDst;
	}

	private static Bitmap scaleImage(Bitmap bitmap, int boundBoxInDp) {
		// Get current dimensions
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Log.d("Scaling img with density " + bitmap.getDensity() + " size " + width + " x " + height + " and " + bitmap.getByteCount() + " bytes");

		// Determine how much to scale: the dimension requiring less scaling is
		// closer to the its side. This way the image always stays inside your
		// bounding box AND either x/y axis touches it.
		float xScale = ((float) boundBoxInDp) / width;
		float yScale = ((float) boundBoxInDp) / height;
		float scale = (xScale <= yScale) ? xScale : yScale;

		// Create a matrix for the scaling and add the scaling data
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);

		// Create a new bitmap and convert it to a format understood by the
		// ImageView
		Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		Log.d("Result an img with density " + scaledBitmap.getDensity() + " size " + scaledBitmap.getWidth() + " x " + scaledBitmap.getHeight() + " and "
				+ scaledBitmap.getByteCount() + " bytes");

		return scaledBitmap;
	}
}
