package com.dreamteam.lookme;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.ProfileImage;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.util.ImageUtil;
import com.dreamteam.util.Log;

public class ProfileActivity extends CommonActivity {
	private static int RESULT_LOAD_IMAGE = 1;

	private static final int PICK_IMAGE = 1;
	
	String imageFilePath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			
			setContentView(R.layout.activity_profile);
			FullProfile oldProfile = dbOpenHelper.getMyFullProfile();
			if (oldProfile != null) {
				switchToUpdateAccount(oldProfile);
			}
			initMenu();
			if (savedInstanceState == null) {
				setMenuItem(0);
			}
		} catch (Exception e) {
			Log.e("errore during create of registration activity! error: " + e.getMessage());
		}
	}

	public void onRegister(View view) {
		try {

			TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

			TextView nameScreen = (TextView) findViewById(R.id.reg_name);
			TextView surnameScreen = (TextView) findViewById(R.id.reg_surname);
			TextView usernameScreen = (TextView) findViewById(R.id.reg_nickname);
			ImageView imageView = (ImageView) findViewById(R.id.imgView);

			if (imageView.getDrawable() == null) {
				imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile_image));
			}

			if (usernameScreen.getText() == null || usernameScreen.getText().equals(""))
			// ||imageView.getDrawable()==null)
			{
				Toast.makeText(this, "All Fields Required.", Toast.LENGTH_SHORT).show();
				return;
			}

			FullProfile profile = null;

			profile = dbOpenHelper.getMyFullProfile();

			if (profile == null)
				profile = new FullProfile();
			profile.setName(nameScreen.getText().toString());

			profile.setSurname(surnameScreen.getText().toString());

			WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = manager.getConnectionInfo();
			String deviceId = info.getMacAddress();

			if (deviceId == null)
				deviceId = tm.getDeviceId();

			profile.setNickname(usernameScreen.getText().toString());

			profile.setId(deviceId);

			if (imageView.getDrawable() != null) {
				Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

				ProfileImage profileImage = null;
				if (profile.getProfileImages() != null & !profile.getProfileImages().isEmpty()) {

					profileImage = profile.getProfileImages().get(0);
					profile.getProfileImages().clear();
				} else
					profileImage = new ProfileImage();
				profileImage.setProfileId(profile.getId());
				profileImage.setImage(ImageUtil.bitmapToByteArray(bitmap));
				profileImage.setMainImage(true);
				profile.getProfileImages().add(profileImage);

			}

			FullProfile savedProfile = dbOpenHelper.saveOrUpdateProfile(profile);
			switchToUpdateAccount(savedProfile);
			Toast toast = Toast.makeText(getApplicationContext(), "welcome on Look@ME!", 10);
			toast.show();

			Intent mainIntent = new Intent(this, SocialActivity.class);
			this.startActivity(mainIntent);
			this.finish();

		} catch (Exception e) {
			Log.e("errore during registration! error: " + e.getMessage());
		}

	}

	public void onChooseImage(View view) {
		try {

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
		} catch (Exception e) {
			Log.e("errore during registration! error: " + e.getMessage());
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Context context = getApplicationContext();
		CharSequence text = "";
		byte[] image = null;
		try {
			super.onActivityResult(requestCode, resultCode, data);

			if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
				Uri selectedImage = data.getData();

				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);

				image = ImageUtil.getImageFromPicturePath(picturePath);
				ImageView imageView = (ImageView) findViewById(R.id.imgView);
				imageView.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));

				cursor.close();
				text = "COOL PICTURE!";
			}

		} catch (Exception e) {
			Log.e("error changing image, error: " + e.toString());
			text = "ops!Unable to load image ";

		}
		int duration = Toast.LENGTH_LONG;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

	}

	private void switchToUpdateAccount(FullProfile profile) {
		TextView nameScreen = (TextView) findViewById(R.id.reg_name);
		nameScreen.setText(profile.getName());
		TextView surnameScreen = (TextView) findViewById(R.id.reg_surname);
		surnameScreen.setText(profile.getSurname());
		TextView usernameScreen = (TextView) findViewById(R.id.reg_nickname);
		usernameScreen.setText(profile.getNickname());

		ImageView imageView = (ImageView) findViewById(R.id.imgView);
		imageView.setImageBitmap(BitmapFactory.decodeByteArray(profile.getProfileImages().get(0).getImage(), 0, profile.getProfileImages().get(0).getImage().length));

		Button button = (Button) findViewById(R.id.btnRegister);
		button.setText("change my profile");
	}

	// START ILookAtMeCommunicationListener implementation
	@Override
	public void onSocialNodeLeft(String nodeName) {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub
	}

	@Override
	public void onSocialNodeJoined(LookAtMeNode node) {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub
	}

	@Override
	public void onCommunicationStopped() {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub

	}

	@Override
	public void onCommunicationStarted() {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub

	}

	@Override
	public void onSocialNodeProfileReceived(LookAtMeNode node) {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub
	}

	@Override
	public void onLikeReceived(String nodeFrom) {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub
	}

	@Override
	public void onChatMessageReceived(String nodeFrom, String message) {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub

	}

	@Override
	public void onSocialNodeUpdated(LookAtMeNode node) {
		Log.d("NOT IMPLEMENTED");
		// TODO Auto-generated method stub
	}
	// END ILookAtMeCommunicationListener implementation

	@Override
	public void onChatMessageReceived(LookAtMeNode nodeFrom, String message) {
		// TODO Auto-generated method stub
		
	}

}