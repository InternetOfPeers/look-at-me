package com.dreamteam.lookme;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.lookme.bean.Profile;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;

public class RegisterActivity extends Activity {
	private static int RESULT_LOAD_IMAGE = 1;

	private static final int PICK_IMAGE = 1;
	private DBOpenHelper dbOpenHelper;
	String imageFilePath = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			dbOpenHelper = DBOpenHelperImpl.getInstance(this);
			// Set View to register.xml
			setContentView(R.layout.register);

			// TODO:this check SUCKS!!!!!I HAVE TO CHANGE
			Profile oldProfile = dbOpenHelper.getMyProfile();

			if (oldProfile != null) {
				switchToUpdateAccount(oldProfile);
			}

		} catch (Exception e) {
			Log.e("REGISTER",
					"errore during create of registration activity! error: "
							+ e.getMessage());
		}

	}

	public void onRegister(View view) {
		try {

			TelephonyManager tm = (TelephonyManager) this
					.getSystemService(Context.TELEPHONY_SERVICE);

			TextView nameScreen = (TextView) findViewById(R.id.reg_name);
			TextView surnameScreen = (TextView) findViewById(R.id.reg_surname);
			TextView usernameScreen = (TextView) findViewById(R.id.reg_username);
			ImageView imageView = (ImageView) findViewById(R.id.imgView);

			if (imageView.getDrawable() == null) {
				imageView.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.ic_launcher));
			}

			if (nameScreen.getText() == null || nameScreen.getText().equals("")
					|| surnameScreen.getText() == null
					|| surnameScreen.getText().equals("")
					|| usernameScreen.getText() == null
					|| usernameScreen.getText().equals(""))
			// ||imageView.getDrawable()==null)
			{
				Toast.makeText(this, "All Fields Required.", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			Profile profile = null;

			profile = dbOpenHelper.getMyProfile();

			if (profile == null)
				profile = new Profile();
			profile.setName(nameScreen.getText().toString());

			profile.setSurname(surnameScreen.getText().toString());

			WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = manager.getConnectionInfo();
			String deviceId = info.getMacAddress();

			profile.setNickname(usernameScreen.getText().toString());

			profile.setDeviceId(deviceId);

			if (imageView.getDrawable() != null) {
				Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable())
						.getBitmap();

				profile.setImage(bitmapToByteArray(bitmap));

			}

			Profile savedProfile = dbOpenHelper.saveOrUpdateProfile(profile);
			switchToUpdateAccount(savedProfile);
			Toast toast = Toast.makeText(getApplicationContext(),
					"welcome on Look@ME!", 10);
			toast.show();

		} catch (Exception e) {
			Log.e("REGISTER",
					"errore during registration! error: " + e.getMessage());
		}

	}

	public void onChooseImage(View view) {
		try {

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"), 1);
		} catch (Exception e) {
			Log.e("REGISTER",
					"errore during registration! error: " + e.getMessage());
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Context context = getApplicationContext();
		CharSequence text = "";
		byte[] image = null;
		try {
			super.onActivityResult(requestCode, resultCode, data);

			if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
					&& null != data) {
				Uri selectedImage = data.getData();

				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);

				image = getImageFromPicturePath(picturePath);
				ImageView imageView = (ImageView) findViewById(R.id.imgView);
				imageView.setImageBitmap(BitmapFactory.decodeByteArray(image,
						0, image.length));

				cursor.close();
				text = "COOL PICTURE!";
			}

		} catch (Exception e) {
			Log.e("changing image",
					"error changing image, error: " + e.toString());
			text = "ops!Unable to load image ";

		}
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

	}

	private void switchToUpdateAccount(Profile profile) {
		TextView nameScreen = (TextView) findViewById(R.id.reg_name);
		nameScreen.setText(profile.getName());
		TextView surnameScreen = (TextView) findViewById(R.id.reg_surname);
		surnameScreen.setText(profile.getSurname());
		TextView usernameScreen = (TextView) findViewById(R.id.reg_username);
		usernameScreen.setText(profile.getNickname());

		ImageView imageView = (ImageView) findViewById(R.id.imgView);
		imageView.setImageBitmap(BitmapFactory.decodeByteArray(
				profile.getImage(), 0, profile.getImage().length));

		Button button = (Button) findViewById(R.id.btnRegister);
		button.setText("change my profile");
	}

	public static byte[] getImageFromPicturePath(String picturePath) {
		try {
			Bitmap b = BitmapLoader.loadBitmap(picturePath, 512, 256);
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

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, newWidth,
				newHeight, false);

		return resizedBitmap;

	}

}

class BitmapLoader {
	public static int getScale(int originalWidth, int originalHeight,
			final int requiredWidth, final int requiredHeight) {
		// a scale of 1 means the original dimensions
		// of the image are maintained
		int scale = 1;

		// calculate scale only if the height or width of
		// the image exceeds the required value.
		if ((originalWidth > requiredWidth)
				|| (originalHeight > requiredHeight)) {
			// calculate scale with respect to
			// the smaller dimension
			if (originalWidth < originalHeight)
				scale = Math.round((float) originalWidth / requiredWidth);
			else
				scale = Math.round((float) originalHeight / requiredHeight);

		}

		return scale;
	}

	public static BitmapFactory.Options getOptions(String filePath,
			int requiredWidth, int requiredHeight) {

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
		options.inSampleSize = getScale(options.outWidth, options.outHeight,
				requiredWidth, requiredHeight);

		// set inJustDecodeBounds to false again
		// so that we can now actually allocate the
		// bitmap some memory
		options.inJustDecodeBounds = false;

		return options;

	}

	public static Bitmap loadBitmap(String filePath, int requiredWidth,
			int requiredHeight) {

		BitmapFactory.Options options = getOptions(filePath, requiredWidth,
				requiredHeight);

		return BitmapFactory.decodeFile(filePath, options);
	}
}
