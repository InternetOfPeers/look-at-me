package com.brainmote.lookatme.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.DisplayMetrics;

import com.brainmote.lookatme.R;
import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.ProfileImage;
import com.brainmote.lookatme.service.Services;

public class ImageUtil {

	public static final int MAX_IMAGE_SIZE = 1080;
	public static final int DEFAULT_SIZE_IN_DP = 600;
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

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap loadBitmap(String filePath) {
		if (!isFilePathValid(filePath))
			return null;
		BitmapFactory.Options options = getOptions(filePath);
		Bitmap bitmap = null;
		Bitmap tempBitmap = null;
		try {
			// Carico l'immagine
			tempBitmap = BitmapFactory.decodeFile(filePath, options);
			if (tempBitmap == null)
				return null;
			bitmap = scaleImage(tempBitmap, DEFAULT_SIZE_IN_DP);
		} catch (OutOfMemoryError oome) {
			Log.d("Out of memory error... cleaning memory");
			CommonUtils.cleanMem();
			// Riprovo a caricare l'immagine
			try {
				bitmap = scaleImage(tempBitmap, DEFAULT_SIZE_IN_DP);
			} catch (Exception e) {
				// Rinuncio
				Log.d(e);
				return null;
			}
		}
		// Log.d("Result an img with density " + bitmap.getDensity() + " size "
		// + bitmap.getWidth() + " x " + bitmap.getHeight() + " and " +
		// bitmap.getByteCount() + " bytes");
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

			// Log.d("Exif orientation is " + orientation);
		}
		if (rotate != 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(rotate);
			try {
				// Carico l'immagine
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			} catch (OutOfMemoryError oome) {
				Log.d("Out of memory error... cleaning memory");
				CommonUtils.cleanMem();
				// Riprovo a caricare l'immagine
				try {
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				} catch (Exception e) {
					// Rinuncio
					Log.d(e);
					return null;
				}
			}
		}
		return bitmap;
	}

	public static Bitmap bitmapForThumbnail(Bitmap bitmap) {
		DisplayMetrics displayMetrics = Services.currentState.getContext().getResources().getDisplayMetrics();
		int display_size_in_px = Math.round(displayMetrics.widthPixels);
		Log.d("Display width is " + display_size_in_px);
		int numCols = 4; // TODO: qui prelevare il numero di colonne in base al
							// layout
		int verticalMargin = Services.currentState.getContext().getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
		int thumbnailSpacing = Services.currentState.getContext().getResources().getDimensionPixelSize(R.dimen.nearby_thumbnail_spacing);
		int thumbnail_size_in_px = (display_size_in_px - (verticalMargin * 2) - (thumbnailSpacing * (numCols - 1))) / numCols;
		Log.d("Thumbnail bound box pixel " + thumbnail_size_in_px);
		return scaleThumbnail(cropBitmap(bitmap, ASPECT_THUMBNAIL, ASPECT_THUMBNAIL), thumbnail_size_in_px);
	}

	public static Bitmap bitmapForCustomThumbnail(Bitmap bitmap, int px) {
		Log.d("Thumbnail bound box pixel " + px);
		return scaleThumbnail(cropBitmap(bitmap, ASPECT_THUMBNAIL, ASPECT_THUMBNAIL), px);
	}

	public static Bitmap bitmapForGallery(Bitmap bitmap) {
		return cropBitmap(bitmap, ASPECT_WIDTH, ASPECT_HEIGHT);
	}

	public static byte[] bitmapToByteArray(Bitmap bitmap) {
		try {
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

		} catch (Exception e) {
			Log.e(e.toString());
			return null;
		}
	}

	public static Bitmap getBitmapProfileImage(Resources resources, BasicProfile profile) {
		try {
			ProfileImage profileImage = profile.getMainProfileImage();
			if (profileImage == null)
				return BitmapFactory.decodeResource(resources, R.drawable.ic_profile_image);
			byte[] profileImageByteArray = profileImage.getImage();
			if (profileImageByteArray == null) {
				return BitmapFactory.decodeResource(resources, R.drawable.ic_profile_image);
			}
			return BitmapFactory.decodeByteArray(profileImageByteArray, 0, profileImageByteArray.length);
		} catch (Exception e) {
			Log.e(e.toString());
			return null;
		}
	}

	private static Bitmap cropBitmap(Bitmap bitmap, int aspectWidth, int aspectHeight) {
		try {
			// Assumo che la bitmap di partenza abbia altezza >= larghezza
			// Log.d("Cropping bitmap " + bitmap.getWidth() + " x " +
			// bitmap.getHeight());
			int newWidth = bitmap.getWidth();
			int newHeight = bitmap.getHeight();
			int offsetX = 0;
			int offsetY = 0;
			// Calcolo il rapporto destinazione e sorgente
			float ratioSrc = (float) bitmap.getWidth() / (float) bitmap.getHeight();
			float ratioDst = (float) aspectWidth / (float) aspectHeight;
			// Log.d("Src ratio is " + ratioSrc);
			// Log.d("Dst ratio is " + ratioDst);

			if (ratioSrc > ratioDst) {
				// l'immagine va stretta
				// Log.d("Reducing width");
				newWidth = (int) (bitmap.getHeight() * ratioDst);
				offsetX = (bitmap.getWidth() - newWidth) / 2;
			} else if (ratioSrc < ratioDst) {
				// l'immagine va accorciata
				// Log.d("Reducing height");
				newHeight = (int) (bitmap.getWidth() / ratioDst);
				offsetY = (bitmap.getHeight() - newHeight) / 2;
			} else if (ratioSrc == ratioDst) {
				// NOP
				// Log.d("No need to reduce image");
				return bitmap;
			}
			// Log.d("New width is " + newWidth);
			// Log.d("New height is " + newHeight);
			// Log.d("Offset X is " + offsetX);
			// Log.d("Offset Y is " + offsetY);
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
		} catch (Exception e) {
			Log.e(e.toString());
			return null;
		}
	}

	public static Bitmap scaleImage(Bitmap bitmap, int boundBoxInDp) {
		try {
			// Get current dimensions
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// Log.d("Scaling img with density " + bitmap.getDensity() +
			// " size " +
			// width + " x " + height + " and " + bitmap.getByteCount() +
			// " bytes");

			// Determine how much to scale: the dimension requiring less scaling
			// is
			// closer to the its side. This way the image always stays inside
			// your
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
			// Log.d("Result an img with density " + scaledBitmap.getDensity() +
			// " size " + scaledBitmap.getWidth() + " x " +
			// scaledBitmap.getHeight()
			// + " and " + scaledBitmap.getByteCount() + " bytes");

			return scaledBitmap;
		} catch (Exception e) {
			Log.e(e.toString());
			return null;
		}
	}

	private static Bitmap scaleThumbnail(Bitmap bitmap, int boundBoxInPx) {
		try {
			// Get current dimensions
			// int width = bitmap.getWidth();
			// int height = bitmap.getHeight();
			// Log.d("Scaling img with density " + bitmap.getDensity() +
			// " size " +
			// width + " x " + height + " and " + bitmap.getByteCount() +
			// " bytes");
			// DisplayMetrics displayMetrics =
			// Services.currentState.getContext().getResources().getDisplayMetrics();
			// int px = Math.round(boundBoxInDp * (displayMetrics.xdpi /
			// DisplayMetrics.DENSITY_DEFAULT));
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, boundBoxInPx, boundBoxInPx, false);
			// Log.d("Result an img with density " + scaledBitmap.getDensity() +
			// " size " + scaledBitmap.getWidth() + " x " +
			// scaledBitmap.getHeight()
			// + " and " + scaledBitmap.getByteCount() + " bytes");

			return scaledBitmap;
		} catch (Exception e) {
			Log.e(e.toString());
			return null;
		}
	}

	public static boolean isFilePathValid(String filePath) {
		Log.d("filePath: " + filePath);
		if (filePath.startsWith("https:") || filePath.startsWith("http:"))
			return false;
		return true;
	}
}
