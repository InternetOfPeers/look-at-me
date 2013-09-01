package com.brainmote.lookatme;

import java.util.Locale;

import com.brainmote.lookatme.enumattribute.Country;
import com.brainmote.lookatme.enumattribute.Language;
import com.brainmote.lookatme.util.CommonUtils;
import com.devsmart.android.ui.HorizontalListView;

import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class NewEditProfileFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_edit_profile, null);
//		TabHost tabs = (TabHost) view.findViewById(R.id.editProfileTabs);
//		TabSpec tabData = tabs.newTabSpec("DATA");
//		tabData.setContent(R.id.tab_data);
//		TabSpec tabPhoto = tabs.newTabSpec("PHOTO");
//		tabPhoto.setContent(R.id.tab_photo);
//		TabSpec tabInterests = tabs.newTabSpec("INTERESTS");
//		tabInterests.setContent(R.id.tab_interests);
		
		Spinner spinnerGender = (Spinner) view.findViewById(R.id.edit_profile_spinner_gender);
		spinnerGender.setAdapter(new ImageSpinnerAdapter(getActivity(), R.id.edit_profile_spinner_gender, CommonUtils.genderArray, CommonUtils.genderImages));
		Spinner spinnerCountry = (Spinner) view.findViewById(R.id.edit_profile_spinner_country);
		spinnerCountry.setAdapter(new ImageSpinnerAdapter(getActivity(), R.id.edit_profile_spinner_gender, CommonUtils.countryArray, CommonUtils.countryImages));
		
		HorizontalListView listview = (HorizontalListView) view.findViewById(R.id.edit_profile_image_list);
		ScrollGalleryAdapter scrollGalleryAdapter = new ScrollGalleryAdapter(getActivity());
		listview.setAdapter(scrollGalleryAdapter);
		
		Locale locale = getResources().getConfiguration().locale;
		String country = locale.getCountry();
		spinnerCountry = (Spinner) view.findViewById(R.id.edit_profile_spinner_country);
		setSpinnerSelectedStringValue(spinnerCountry, Country.toString(Country.parse(country)));
		String language = locale.getLanguage();
		Spinner spinnerLanguage = (Spinner) view.findViewById(R.id.edit_profile_spinner_language);
		setSpinnerSelectedStringValue(spinnerLanguage, Language.toString(Language.parse(language)));
		
		return view;
	}
	
	private void setSpinnerSelectedStringValue(Spinner spinner, String value) {
		ArrayAdapter<String> myAdapter = (ArrayAdapter) spinner.getAdapter();
		int spinnerPosition = myAdapter.getPosition(value);
		spinner.setSelection(spinnerPosition);

	}

}
