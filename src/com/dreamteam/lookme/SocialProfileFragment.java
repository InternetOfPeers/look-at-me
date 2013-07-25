package com.dreamteam.lookme;

import uk.co.senab.photoview.PhotoView;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.service.CommunicationService;
import com.dreamteam.util.Log;

public class SocialProfileFragment extends Fragment implements OnClickListener {
	
	private CommunicationService communicationService;
	
	private ViewPager profilePhoto;
	
	private TextView textNickname;
	private TextView textName;
	private TextView textSurname;
	private Button buttonLike;
	
	private int[] gallery_images;
	private LookAtMeNode profileNode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_social_profile, null);
		
		textNickname = (TextView) view.findViewById(R.id.textNickname);
		textName = (TextView) view.findViewById(R.id.textName);
		textSurname = (TextView) view.findViewById(R.id.textSurname);
		
		buttonLike = (Button) view.findViewById(R.id.buttonLike);
		buttonLike.setOnClickListener(this);
		
		profilePhoto = (HackyViewPager) view.findViewById(R.id.hackyViewPager);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		Log.d("LIKE clicked on node " + profileNode.getId());
		communicationService.sendLike(profileNode.getId());
		Toast.makeText(this.getActivity(), "You like " + profileNode.getProfile().getNickname(), Toast.LENGTH_LONG).show();
	}
	
	public void setProfileNode(LookAtMeNode node) {
		this.profileNode = node;
		FullProfile profile = (FullProfile) this.profileNode.getProfile();
		
		textNickname.setText(profile.getNickname());
		textName.setText(profile.getName());
		textSurname.setText(profile.getSurname());
		
		gallery_images = new int[3];
		gallery_images[0] = R.drawable.demo_gallery_1;
		gallery_images[1] = R.drawable.demo_gallery_2;
		gallery_images[2] = R.drawable.demo_gallery_3;
		profilePhoto.setAdapter(new SamplePagerAdapter());
	}
	
	public void setCommunicationService(CommunicationService communicationService) {
		this.communicationService = communicationService;
	}
	
	class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return gallery_images.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			photoView.setImageResource(gallery_images[position]);// in futuro utilizzare setImageBitmap

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
}