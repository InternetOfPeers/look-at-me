package com.brainmote.lookatme;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.Contact;
import com.brainmote.lookatme.bean.FullProfile;
import com.brainmote.lookatme.bean.ProfileImage;
import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.db.DBOpenHelper;
import com.brainmote.lookatme.db.DBOpenHelperImpl;
import com.brainmote.lookatme.enumattribute.ContactType;
import com.brainmote.lookatme.enumattribute.Country;
import com.brainmote.lookatme.enumattribute.Language;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.CommonUtils;
import com.brainmote.lookatme.util.ImageUtil;
import com.brainmote.lookatme.util.Log;
import com.brainmote.lookatme.util.Nav;
import com.devsmart.android.ui.HorizontalListView;

public class EditProfileFragment extends Fragment {

	private View view;
	private ImageView mainProfileImage;
	private Spinner spinnerAge;
	private Spinner spinnerGender;
	private Spinner spinnerCountry;
	private Spinner spinnerLanguage;
	private HorizontalListView listview;
	private TextView nameScreen;
	private TextView surnameScreen;
	private TextView nicknameScreen;
	private TextView emailContactScreen;
	private TextView facebookContactScreen;
	private TextView linkedinContactScreen;
	private TextView phoneContactScreen;

	private ScrollGalleryAdapter scrollGalleryAdapter;
	private String profileId;
	private int widthPx;
	private boolean noPhoto;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_edit_profile, null);
		calculateImageWidthPixel();

		mainProfileImage = (ImageView) view.findViewById(R.id.editProfileMainImage);

		TabHost tabs = (TabHost) view.findViewById(R.id.editProfileTabs);
		tabs.setup();

		TabSpec tabSpecPhoto = tabs.newTabSpec("Photo");
		tabSpecPhoto.setIndicator("", getResources().getDrawable(R.drawable.ic_photo));
		tabSpecPhoto.setContent(R.id.tab_photo);
		tabs.addTab(tabSpecPhoto);

		TabSpec tabSpecData = tabs.newTabSpec("Profile");
		tabSpecData.setIndicator("", getResources().getDrawable(R.drawable.ic_edit_profile));
		tabSpecData.setContent(R.id.tab_data);
		tabs.addTab(tabSpecData);

		TabSpec tabSpecInterests = tabs.newTabSpec("Interests");
		tabSpecInterests.setIndicator("", getResources().getDrawable(R.drawable.ic_interest));
		tabSpecInterests.setContent(R.id.tab_interests);
		tabs.addTab(tabSpecInterests);

		TabSpec tabSpecContacts = tabs.newTabSpec("Contacts");
		tabSpecContacts.setIndicator("", getResources().getDrawable(R.drawable.ic_contact));
		tabSpecContacts.setContent(R.id.tab_contacts);
		tabs.addTab(tabSpecContacts);

		OnFocusChangeListener focusChange = new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean gainFocus) {
				if (gainFocus) {
					// onFocus
				} else {
					// onBlur
					saveProfile(false, false);
				}
			}
		};

		spinnerAge = (Spinner) view.findViewById(R.id.editProfileSpinnerAge);
		spinnerGender = (Spinner) view.findViewById(R.id.editProfileSpinnerGender);
		spinnerCountry = (Spinner) view.findViewById(R.id.editProfileSpinnerCountry);
		spinnerCountry = (Spinner) view.findViewById(R.id.editProfileSpinnerCountry);
		spinnerLanguage = (Spinner) view.findViewById(R.id.editProfileSpinnerLanguage);

		nameScreen = (TextView) view.findViewById(R.id.editProfileFieldName);
		surnameScreen = (TextView) view.findViewById(R.id.editProfileFieldSurname);
		nicknameScreen = (TextView) view.findViewById(R.id.editProfileFieldNickname);
		emailContactScreen = (TextView) view.findViewById(R.id.editProfileFieldEmail);
		phoneContactScreen = (TextView) view.findViewById(R.id.editProfileFieldMobile);
		facebookContactScreen = (TextView) view.findViewById(R.id.editProfileFieldFacebook);
		linkedinContactScreen = (TextView) view.findViewById(R.id.editProfileFieldLinkedIn);

		spinnerGender.setAdapter(new ImageSpinnerAdapter(getActivity(), R.id.editProfileSpinnerGender, CommonUtils.genderArray, CommonUtils.genderImages));
		spinnerCountry.setAdapter(new ImageSpinnerAdapter(getActivity(), R.id.editProfileSpinnerCountry, CommonUtils.countryArray, CommonUtils.countryImages));

		listview = (HorizontalListView) view.findViewById(R.id.editProfileImageList);
		scrollGalleryAdapter = new ScrollGalleryAdapter(getActivity());
		listview.setAdapter(scrollGalleryAdapter);

		FullProfile oldProfile = Services.currentState.getMyFullProfile();
		if (oldProfile != null) {
			profileId = oldProfile.getId();
			switchToUpdateAccount(oldProfile);
			noPhoto = false;
		} else {
			WifiManager manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = manager.getConnectionInfo();
			String deviceId = info.getMacAddress();
			if (deviceId == null) {
				TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
				deviceId = tm.getDeviceId();
			}
			profileId = deviceId;
			Locale locale = getResources().getConfiguration().locale;
			String country = locale.getCountry();
			setSpinnerSelectedStringValue(spinnerCountry, Country.toString(Country.parse(country)));
			String language = locale.getLanguage();
			setSpinnerSelectedStringValue(spinnerLanguage, Language.toString(Language.parse(language)));

			Bitmap noProfileImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile_image);
			mainProfileImage.setImageBitmap(ImageUtil.bitmapForCustomThumbnail(noProfileImage, widthPx));
			noPhoto = true;
		}

		// Se la camera non è presente, nascondo il pulsante e il testo
		if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			ImageButton addImageButton = (ImageButton) view.findViewById(R.id.addImageButton);
			addImageButton.setVisibility(ImageButton.GONE);
			addImageButton.setEnabled(false);
			TextView addImageLabel = (TextView) view.findViewById(R.id.addImageLabel);
			addImageLabel.setVisibility(TextView.GONE);
		}

		ExpandableHeightGridView gridInterest = (ExpandableHeightGridView) view.findViewById(R.id.gridInterestInEdit);
		EditProfileInterestGridAdapter gridInterestAdapter = new EditProfileInterestGridAdapter(getActivity(), Services.currentState.getInterestSet(), true);
		if (gridInterestAdapter.isEmpty()) {
			gridInterest.setNumColumns(1);
		} else {
			gridInterest.setNumColumns(GridView.AUTO_FIT);
		}
		gridInterest.setAdapter(gridInterestAdapter);
		gridInterest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Nav.startActivity(EditProfileFragment.this.getActivity(), ManageInterestActivity.class);
			}
		});

		spinnerAge.setOnItemSelectedListener((new OnItemSelectedListener() {
			boolean checked = false;

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				if (checked) {
					saveProfile(false, false);
				}
				checked = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}
		}));
		spinnerGender.setOnItemSelectedListener((new OnItemSelectedListener() {
			boolean checked = false;

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				if (checked) {
					saveProfile(false, false);
				}
				checked = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}
		}));
		spinnerCountry.setOnItemSelectedListener((new OnItemSelectedListener() {
			boolean checked = false;

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				if (checked) {
					saveProfile(false, false);
				}
				checked = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}
		}));
		spinnerLanguage.setOnItemSelectedListener((new OnItemSelectedListener() {
			boolean checked = false;

			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				if (checked) {
					saveProfile(false, false);
				}
				checked = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}
		}));

		nameScreen.setOnFocusChangeListener(focusChange);
		surnameScreen.setOnFocusChangeListener(focusChange);
		nicknameScreen.setOnFocusChangeListener(focusChange);
		nameScreen.setOnFocusChangeListener(focusChange);
		phoneContactScreen.setOnFocusChangeListener(focusChange);
		emailContactScreen.setOnFocusChangeListener(focusChange);
		facebookContactScreen.setOnFocusChangeListener(focusChange);
		linkedinContactScreen.setOnFocusChangeListener(focusChange);
		// Serve per far espandere la grid e fargli occupare tutto lo spazio
		// necessario (il wrap_content normale non funziona quando la gridview
		// sta dentro una scrollview)
		gridInterest.setExpanded(true);
		return view;
	}

	/**
	 * 
	 * @param saveAll
	 * @param backToNearby
	 *            TODO
	 */
	public void saveProfile(boolean saveAll, boolean backToNearby) {
		Log.d();
		try {
			// Verifico che sia inserito il nickname ed un'immagine di profilo
			if (nicknameScreen.getText() == null || nicknameScreen.getText().toString().trim().isEmpty() || noPhoto) {
				if (backToNearby)
					((CommonActivity) getActivity()).showDialog(getString(R.string.message_warning), getString(R.string.edit_profile_message_mandatory_fields_not_set));
				return;
			}
			FullProfile profile = Services.currentState.getMyFullProfile();
			if (profile == null) {
				saveAll = true;
				profile = new FullProfile();
				if (mainProfileImage.getDrawable() == null)
					return;
				ProfileImage profileImage = new ProfileImage();
				profileImage.setImage(ImageUtil.bitmapToByteArray(((BitmapDrawable) mainProfileImage.getDrawable()).getBitmap()));
				profileImage.setMainImage(true);
				profile.setProfileImages(new ArrayList<ProfileImage>());
				profile.getProfileImages().add(profileImage);
				if (scrollGalleryAdapter.imageList.isEmpty())
					scrollGalleryAdapter.imageList.add(profileImage);
			}

			profile.setName(nameScreen.getText().toString());
			profile.setSurname(surnameScreen.getText().toString());
			profile.setNickname(nicknameScreen.getText().toString());
			profile.setId(profileId);
			profile.setInterestSet(Services.currentState.getInterestSet());
			String age = (String) spinnerAge.getSelectedItem();
			if (age != null && !age.isEmpty() && !age.equals(getString(R.string.edit_profile_spinner_age_prompt)))
				profile.setAge(Integer.valueOf(age));

			String gender = (String) spinnerGender.getSelectedItem();
			if (gender != null && !gender.isEmpty() && !gender.equals(getString(R.string.edit_profile_spinner_gender_prompt)))
				profile.setGender(gender);

			String country = (String) spinnerCountry.getSelectedItem();
			if (country != null && !country.isEmpty()) {
				profile.setLivingCountry(country);
			}

			String language = (String) spinnerLanguage.getSelectedItem();
			if (language != null && !language.isEmpty()) {
				profile.setPrimaryLanguage(language);
			}

			if (saveAll && scrollGalleryAdapter.imageList.size() > 0) {
				profile.setProfileImages(scrollGalleryAdapter.imageList);
			}

			List<Contact> contactList = new ArrayList<Contact>();

			String email = emailContactScreen.getText().toString();
			if (email != null && !email.isEmpty()) {
				Contact contact = new Contact();
				contact.setProfileId(profileId);
				contact.setContactType(ContactType.EMAIL);
				contact.setReference(email);
				contactList.add(contact);
			}

			String phone = phoneContactScreen.getText().toString();
			if (phone != null && !phone.isEmpty()) {
				Contact contact = new Contact();
				contact.setProfileId(profileId);
				contact.setContactType(ContactType.PHONE);
				contact.setReference(phone);
				contactList.add(contact);
			}

			String facebookContact = facebookContactScreen.getText().toString();
			if (facebookContact != null && !facebookContact.isEmpty()) {
				Contact contact = new Contact();
				contact.setProfileId(profileId);
				contact.setContactType(ContactType.FACEBOOK);
				contact.setReference(facebookContact);
				contactList.add(contact);
			}

			String linkedinContact = linkedinContactScreen.getText().toString();
			if (linkedinContact != null && !linkedinContact.isEmpty()) {
				Contact contact = new Contact();
				contact.setProfileId(profileId);
				contact.setContactType(ContactType.LINKEDIN);
				contact.setReference(linkedinContact);
				contactList.add(contact);
			}

			if (!contactList.isEmpty())
				profile.setContactList(contactList);

			DBOpenHelper dbOpenHelper = DBOpenHelperImpl.getInstance(getActivity());
			FullProfile savedProfile = dbOpenHelper.saveOrUpdateProfile(profile);

			// TOFIX Se il servizio è spento qui va in eccezione
			Services.businessLogic.notifyMyProfileIsUpdated();

			switchToUpdateAccount(savedProfile);

			// ora che ho aggiornato il profilo devo vedere se gli interessi
			// matchano con quelli degli altri
			for (Node node : Services.currentState.getSocialNodeMap().getNodeList()) {
				if (Services.currentState.checkInterestMatch((BasicProfile) node.getProfile())) {
					Services.notification.perfectMatch(Services.currentState.getContext(), Services.currentState.getNickname(node.getId()));
				}
			}

			// Se necessario, aggiungo delle conversazioni fittizie
			if (Services.businessLogic.isCreditsInAppEnabled(getActivity())) {
				Services.businessLogic.initFakeUsersConversations();
			}

			// Riporta l'utente alla schermata di nearby
			if (backToNearby)
				Nav.startActivity(getActivity(), NearbyActivity.class);
		} catch (Exception e) {
			Log.e("errore during registration! error: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void setSpinnerSelectedStringValue(Spinner spinner, String value) {
		ArrayAdapter<String> myAdapter = (ArrayAdapter<String>) spinner.getAdapter();
		int spinnerPosition = myAdapter.getPosition(value);
		spinner.setSelection(spinnerPosition);

	}

	private void switchToUpdateAccount(FullProfile profile) {
		nameScreen.setText(profile.getName());
		surnameScreen.setText(profile.getSurname());
		nicknameScreen.setText(profile.getNickname());

		if (profile.getAge() != 0) {
			setSpinnerSelectedStringValue(spinnerAge, String.valueOf(profile.getAge()));
		}

		if (profile.getLivingCountry() != null && !profile.getLivingCountry().isEmpty()) {
			setSpinnerSelectedStringValue(spinnerCountry, (profile.getLivingCountry()));
		}

		if (profile.getPrimaryLanguage() != null && !profile.getPrimaryLanguage().isEmpty()) {
			setSpinnerSelectedStringValue(spinnerLanguage, (profile.getPrimaryLanguage()));
		}

		if (profile.getGender() != null && !profile.getGender().isEmpty()) {
			setSpinnerSelectedStringValue(spinnerGender, (profile.getGender()));
		}

		if (profile.getProfileImages() != null) {
			scrollGalleryAdapter.setProfileImageList(profile.getProfileImages());
		}

		if (profile.getContactList() != null && !profile.getContactList().isEmpty()) {
			Contact tempContact = new Contact();
			Iterator<Contact> iter = profile.getContactList().iterator();
			while (iter.hasNext()) {
				tempContact = iter.next();
				switch (tempContact.getContactType()) {
				case PHONE: {
					phoneContactScreen.setText(tempContact.getReference());
					break;
				}
				case EMAIL: {
					emailContactScreen.setText(tempContact.getReference());
					break;
				}
				case FACEBOOK: {
					facebookContactScreen.setText(tempContact.getReference());
					break;
				}
				case LINKEDIN: {
					linkedinContactScreen.setText(tempContact.getReference());
					break;
				}
				}
			}
		}

		mainProfileImage.setImageBitmap(ImageUtil.bitmapForCustomThumbnail(profile.getProfileImages().get(0).getImageBitmap(), widthPx));
		refreshFragment();
	}

	private void refreshFragment() {
		scrollGalleryAdapter.notifyDataSetChanged();
	}

	public void addProfileImage(Bitmap photo) {
		ProfileImage profileImage = new ProfileImage();
		profileImage.setImage(ImageUtil.bitmapToByteArray(photo));
		profileImage.setProfileId(profileId);

		if (scrollGalleryAdapter.imageList.size() == 0) {
			// setto come immagine principale
			profileImage.setMainImage(true);
			Bitmap photoThumbnail = ImageUtil.bitmapForCustomThumbnail(photo, widthPx);
			mainProfileImage.setImageBitmap(photoThumbnail);
		}

		scrollGalleryAdapter.imageList.add(profileImage);
		if (scrollGalleryAdapter.imageList.size() > 1
				|| (scrollGalleryAdapter.imageList.size() == 1 && nicknameScreen.getText() != null && nicknameScreen.getText().length() > 0 && Services.currentState
						.getMyFullProfile() == null))
			saveProfile(true, false);
		noPhoto = false;
		refreshFragment();
	}

	private void calculateImageWidthPixel() {
		// Display metrics per adattare l'immagine del profilo
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int display_size_in_px = Math.round(displayMetrics.widthPixels);
		Log.d("Display width is " + display_size_in_px);
		widthPx = display_size_in_px;

		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			widthPx = widthPx / 2;
			if (widthPx < 240)
				widthPx = 240;
		}
		widthPx = widthPx - (getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin)) * 2;
	}

	public void setMainProfileImage(ProfileImage photo) {
		Bitmap thumbnailBitmap = ImageUtil.bitmapForCustomThumbnail(photo.getImageBitmap(), widthPx);
		mainProfileImage.setImageBitmap(thumbnailBitmap);
	}
}
