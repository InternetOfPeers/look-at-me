package com.brainmote.lookatme;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.util.ImageUtil;

public class ContactFragment extends Fragment {
	
	private ContactListAdapter contactListAdapter;
	private ListView contactList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contact_list, null);
		if (((CommonActivity) getActivity()).checkIfProfileIsCompleted()) {
			contactListAdapter = new ContactListAdapter(this.getActivity());
			contactList = (ListView) view.findViewById(R.id.contactList);
			contactList.setAdapter(contactListAdapter);
			contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
					// Show alert dialog con riepilogo dati e scelta salva ed elimina
					showContactDialog((BasicProfile) contactListAdapter.getItem(position));
				}
			});
		}
		return view;
	}
	
	private void showContactDialog(BasicProfile profile) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity());
		LayoutInflater layoutInflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.dialog_manage_contact, null);
		dialogBuilder.setView(view);
		dialogBuilder.setTitle(profile.getNickname() + ", " + profile.getAge());
		
		ViewGroup linePhone = (ViewGroup) view.findViewById(R.id.lineManageContactPhone);
		ViewGroup lineMail = (ViewGroup) view.findViewById(R.id.lineManageContactMail);
		//linePhone.setVisibility(View.INVISIBLE);
		//lineMail.setVisibility(View.INVISIBLE);
		
		
		TextView phone = (TextView) view.findViewById(R.id.textManageContactTelephone);
		TextView email = (TextView) view.findViewById(R.id.textManageContactEmail);
		ImageView facebookImg = (ImageView) view.findViewById(R.id.imageManageContactFacebook);
		ImageView linkedinImg = (ImageView)view.findViewById(R.id.imageManageContactLinkedin);
		
		ImageView imageContact = (ImageView) view.findViewById(R.id.imageManageContactThumbnail);
		Bitmap mainImageProfile = ImageUtil.getBitmapProfileImage(getActivity().getResources(), profile);
		Bitmap croppedImageProfile = ImageUtil.bitmapForThumbnail(mainImageProfile);
		imageContact.setImageBitmap(croppedImageProfile);
		
		phone.setText("3211234567");
		email.setText("askjdhasd");
		
		dialogBuilder.setPositiveButton("Save in contacts", null);
		dialogBuilder.setNeutralButton("Remove favourite", null);
		dialogBuilder.setNegativeButton("Cancel", null);
		
		dialogBuilder.create().show();
	}
}
