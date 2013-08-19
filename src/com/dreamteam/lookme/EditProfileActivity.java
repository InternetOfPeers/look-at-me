package com.dreamteam.lookme;

import java.util.Locale;

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
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devsmart.android.ui.HorizontalListView;
import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.Interest;
import com.dreamteam.lookme.bean.ProfileImage;
import com.dreamteam.lookme.constants.AppSettings;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.lookme.enumattribute.Country;
import com.dreamteam.lookme.enumattribute.Language;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.ImageUtil;
import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;

public class EditProfileActivity extends CommonActivity {

	protected static final int PHOTO_PICKED = 0;

	private ScrollGalleryAdapter scrollGalleryAdapter;
	private String profileId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.activity_edit_profile);
			FullProfile oldProfile = Services.currentState.getMyFullProfile();
			Spinner spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
			spinnerGender.setAdapter(new ImageSpinnerAdapter(this, R.id.spinner_gender, CommonUtils.genderArray, CommonUtils.genderImages));
			Spinner spinnerCountry = (Spinner) findViewById(R.id.spinner_country);
			spinnerCountry.setAdapter(new ImageSpinnerAdapter(this, R.id.spinner_gender, CommonUtils.countryArray, CommonUtils.countryImages));
			
			HorizontalListView listview = (HorizontalListView) findViewById(R.id.listview);
			scrollGalleryAdapter = new ScrollGalleryAdapter(this);
			listview.setAdapter(scrollGalleryAdapter);
			
			if (oldProfile != null) {
				profileId = oldProfile.getId();
				switchToUpdateAccount(oldProfile);
				scrollGalleryAdapter.setProfileImageList(oldProfile.getProfileImages());
			} else {WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				WifiInfo info = manager.getConnectionInfo();
				String deviceId = info.getMacAddress();
				if (deviceId == null) {
					TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
					deviceId = tm.getDeviceId();
				}
				profileId = deviceId;
				Locale locale = getResources().getConfiguration().locale;
				String country = locale.getCountry();
				spinnerCountry = (Spinner) findViewById(R.id.spinner_country);
				setSpinnerSelectedStringValue(spinnerCountry, Country.toString(Country.parse(country)));
				String language = locale.getLanguage();
				Spinner spinnerLanguage = (Spinner) findViewById(R.id.spinner_language);
				setSpinnerSelectedStringValue(spinnerLanguage, Language.toString(Language.parse(language)));
			}

			initDrawerMenu(savedInstanceState, this.getClass(), true);

		} catch (Exception e) {
			Log.e("errore during create of registration activity! error: " + e.getMessage());
		}
	}

	public void onRegister(View view) {
		Log.d();
		try {
			TextView nameScreen = (TextView) findViewById(R.id.reg_name);
			TextView surnameScreen = (TextView) findViewById(R.id.reg_surname);
			TextView usernameScreen = (TextView) findViewById(R.id.reg_nickname);
			Spinner spinnerAge = (Spinner) findViewById(R.id.spinner_age);
			Spinner spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
			Spinner spinnerCountry = (Spinner) findViewById(R.id.spinner_country);
			Spinner spinnerLanguage = (Spinner) findViewById(R.id.spinner_language);
			ImageView imageView = (ImageView) findViewById(R.id.imgView);
			if (usernameScreen.getText() == null || usernameScreen.getText().toString().equals("")) {
				// Verifico anche che sia stata impostata
				// un'immagine di profilo
				if (imageView.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_profile_image).getConstantState())) {
					Toast.makeText(this, "To create a new profile you need to insert at least an image and a nickname.", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			FullProfile profile = Services.currentState.getMyFullProfile();
			if (profile == null)
				profile = new FullProfile();
			profile.setName(nameScreen.getText().toString());
			profile.setSurname(surnameScreen.getText().toString());
			profile.setNickname(usernameScreen.getText().toString());
			profile.setId(profileId);
			String age = (String) spinnerAge.getSelectedItem();
			if (age != null && !age.isEmpty() && !age.equals("age"))
				profile.setAge(Integer.valueOf(age));

			String gender = (String) spinnerGender.getSelectedItem();
			if (gender != null && !gender.isEmpty() && !gender.equals("gender"))
				profile.setGender(gender);

			String country = (String) spinnerCountry.getSelectedItem();
			if (country != null && !country.isEmpty()) {
				profile.setLivingCountry(country);
			}

			String language = (String) spinnerLanguage.getSelectedItem();
			if (language != null && !country.isEmpty()) {
				profile.setPrimaryLanguage(language);
			}
			
			if (scrollGalleryAdapter.imageList.size() > 0) {
				profile.setProfileImages(scrollGalleryAdapter.imageList);
			}

//			if (imageView.getDrawable() != null) {
//				Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//
//				ProfileImage profileImage = null;
//
//				if (scrollGalleryAdapter != null)
//					profile.getProfileImages().addAll(scrollGalleryAdapter.imageList);
//				else {
//					profileImage = new ProfileImage();
//					profileImage.setProfileId(profile.getId());
//					profileImage.setImage(ImageUtil.bitmapToByteArray(bitmap));
//					profileImage.setMainImage(true);
//					profile.getProfileImages().add(profileImage);
//				}
//
//			}

			DBOpenHelper dbOpenHelper = DBOpenHelperImpl.getInstance(this);
			FullProfile savedProfile = dbOpenHelper.saveOrUpdateProfile(profile);
			Services.businessLogic.notifyMyProfileIsUpdated();
			switchToUpdateAccount(savedProfile);
			Toast toast = Toast.makeText(getApplicationContext(), "Welcome on Look@me!", Toast.LENGTH_SHORT);
			toast.show();

			// Se necessario, aggiungo una conversazione fittizia
			if (AppSettings.isFakeUserEnabled) {
				Services.businessLogic.storeConversation(Services.businessLogic.getFakeUser().getConversation(Services.currentState.getMyBasicProfile().getId()));
			}

			Nav.startActivity(this, NearbyActivity.class);

		} catch (Exception e) {
			Log.e("errore during registration! error: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void onChooseImage(View view) {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			startActivityForResult(Intent.createChooser(intent, "Select Picture"), PHOTO_PICKED);
		} catch (Exception e) {
			Log.e("errore during registration! error: " + e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PHOTO_PICKED && resultCode == RESULT_OK && null != data) {
			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();

				Bitmap photo = ImageUtil.loadBitmap(filePath);
				
				ProfileImage profileImage = new ProfileImage();
				profileImage.setImage(ImageUtil.bitmapToByteArray(photo));
				profileImage.setProfileId(profileId);

				if (scrollGalleryAdapter.imageList.size() == 0) {
					// setto come immagine principale
					profileImage.setMainImage(true);
					Bitmap photoThumbnail = ImageUtil.bitmapForCustomThumbnail(photo, 50);
					ImageView imageView = (ImageView) findViewById(R.id.imgView);
					imageView.setImageBitmap(photoThumbnail);
				}

				scrollGalleryAdapter.imageList.add(profileImage);
				refreshFragment();
				
			} catch (OutOfMemoryError e) {
				Log.d("Out of memory error... cleaning memory");
				Toast.makeText(getApplicationContext(), "Ops! Unable to load image ", Toast.LENGTH_LONG).show();
				CommonUtils.cleanMem();
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("error changing image, error: " + e.toString());
				Toast.makeText(getApplicationContext(), "Ops! Unable to load image ", Toast.LENGTH_LONG).show();
			}
		}

	}

	private void switchToUpdateAccount(FullProfile profile) {
		TextView nameScreen = (TextView) findViewById(R.id.reg_name);
		nameScreen.setText(profile.getName());
		TextView surnameScreen = (TextView) findViewById(R.id.reg_surname);
		surnameScreen.setText(profile.getSurname());
		TextView usernameScreen = (TextView) findViewById(R.id.reg_nickname);
		usernameScreen.setText(profile.getNickname());

		if (profile.getAge() != 0) {
			setSpinnerSelectedStringValue((Spinner) findViewById(R.id.spinner_age), String.valueOf(profile.getAge()));
		}

		if (profile.getLivingCountry() != null && !profile.getLivingCountry().isEmpty()) {
			setSpinnerSelectedStringValue((Spinner) findViewById(R.id.spinner_country), (profile.getLivingCountry()));
		}

		if (profile.getPrimaryLanguage() != null && !profile.getPrimaryLanguage().isEmpty()) {
			setSpinnerSelectedStringValue((Spinner) findViewById(R.id.spinner_language), (profile.getPrimaryLanguage()));
		}

		if (profile.getGender() != null && !profile.getGender().isEmpty()) {
			setSpinnerSelectedStringValue((Spinner) findViewById(R.id.spinner_gender), (profile.getGender()));
		}

		ImageView imageView = (ImageView) findViewById(R.id.imgView);
		imageView.setImageBitmap(ImageUtil.bitmapForThumbnail(BitmapFactory.decodeByteArray(profile.getProfileImages().get(0).getImage(), 0, profile.getProfileImages().get(0).getImage().length)));

		TextView interest = (TextView) findViewById(R.id.reg_interest);
		StringBuilder sb = new StringBuilder();

		for (Interest inter : Services.currentState.getMyFullProfile().getInterestList()) {
			sb.append(inter.getDesc() + " ");
		}

		interest.setText(sb.toString());

		interest.setOnFocusChangeListener(new InterestOnFocusListner(this));

		Button button = (Button) findViewById(R.id.btnRegister);
		button.setText("Save profile");
	}

	private class InterestOnFocusListner implements OnFocusChangeListener {
		private Activity activity;

		public InterestOnFocusListner(Activity activity) {
			this.activity = activity;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				Nav.startActivity(activity, ManageInterestActivity.class);
			}
		}
	}

	private void setSpinnerSelectedStringValue(Spinner spinner, String value) {
		ArrayAdapter<String> myAdapter = (ArrayAdapter) spinner.getAdapter();
		int spinnerPosition = myAdapter.getPosition(value);
		spinner.setSelection(spinnerPosition);

	}

//	protected void setMainProfileImage(ProfileImage profileImage) {
//		ImageView imageView = (ImageView) findViewById(R.id.imgView);
//		imageView.setImageBitmap(BitmapFactory.decodeByteArray(profileImage.getImage(), 0, profileImage.getImage().length));
//	}

	private void refreshFragment() {
		scrollGalleryAdapter.notifyDataSetChanged();
	}

}
