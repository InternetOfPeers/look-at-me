package com.dreamteam.lookme;

import com.dreamteam.lookme.service.CommunicationService;
import com.dreamteam.util.Log;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class SocialProfileFragment extends Fragment {
	
	int[] gallery_grid_Images;
	
	ViewFlipper profilePhotoFlipper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_social_profile, null);
		
		gallery_grid_Images = new int[3];
		gallery_grid_Images[0] = R.drawable.demo_gallery_1;
		gallery_grid_Images[1] = R.drawable.demo_gallery_2;
		gallery_grid_Images[2] = R.drawable.demo_gallery_3;
		
		profilePhotoFlipper = (ViewFlipper) view.findViewById(R.id.profilePhotoFlipper);
		for (int i=0; i < gallery_grid_Images.length; i++) {
			// This will create dynamic image view and add them to ViewFlipper
			ImageView image = new ImageView(this.getActivity());
		    image.setBackgroundResource(gallery_grid_Images[i]);
		    profilePhotoFlipper.addView(image);
        }
		
		return view;
	}

}