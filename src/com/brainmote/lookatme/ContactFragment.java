package com.brainmote.lookatme;

import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.Contact;
import com.brainmote.lookatme.db.DBOpenHelper;
import com.brainmote.lookatme.db.DBOpenHelperImpl;
import com.brainmote.lookatme.util.ImageUtil;
import com.brainmote.lookatme.util.Log;

public class ContactFragment extends Fragment {

	private ContactListAdapter contactListAdapter;
	private ListView contactList;
	private TextView noContactMessage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contact_list, null);
		noContactMessage = (TextView) view.findViewById(R.id.contact_text_message);
		if (((CommonActivity) getActivity()).checkIfProfileIsCompleted()) {
			try {
				DBOpenHelper dbHelper = DBOpenHelperImpl.getInstance(getActivity());
				List<BasicProfile> contacts = dbHelper.getProfiles();
				if (contacts != null && contacts.size() > 0) {
					noContactMessage.setVisibility(View.GONE);
					contactListAdapter = new ContactListAdapter(this.getActivity(), contacts);
				} else {
					contactListAdapter = new ContactListAdapter(this.getActivity());
				}
				contactList = (ListView) view.findViewById(R.id.contactList);
				contactList.setAdapter(contactListAdapter);
				contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
						// Show alert dialog con riepilogo dati e scelta salva
						// ed elimina
						showContactDialog((BasicProfile) contactListAdapter.getItem(position));
					}
				});
			} catch (Exception e) {
				Log.e("Error while retrieving contact list");
			}
		}
		return view;
	}

	private void showContactDialog(final BasicProfile profile) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getActivity());
		LayoutInflater layoutInflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.dialog_manage_contact, null);
		dialogBuilder.setView(view);
		dialogBuilder.setTitle(profile.getNickname() + ", " + profile.getAge());

		ViewGroup linePhone = (ViewGroup) view.findViewById(R.id.lineManageContactPhone);
		ViewGroup lineMail = (ViewGroup) view.findViewById(R.id.lineManageContactMail);
		linePhone.setVisibility(View.GONE);
		lineMail.setVisibility(View.GONE);

		TextView phone = (TextView) view.findViewById(R.id.textManageContactTelephone);
		TextView email = (TextView) view.findViewById(R.id.textManageContactEmail);

		ImageView imageContact = (ImageView) view.findViewById(R.id.imageManageContactThumbnail);
		Bitmap mainImageProfile = ImageUtil.getBitmapProfileImage(getActivity().getResources(), profile);
		Bitmap croppedImageProfile = ImageUtil.bitmapForThumbnail(mainImageProfile);
		imageContact.setImageBitmap(croppedImageProfile);

		if (profile.getContactList() != null) {
			for (Contact contact : profile.getContactList()) {
				final String reference = contact.getReference();
				switch (contact.getContactType()) {
				case EMAIL:
					email.setText(reference);
					lineMail.setVisibility(View.VISIBLE);
					break;
				case PHONE:
					phone.setText(reference);
					linePhone.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			}
		}

		// dialogBuilder.setPositiveButton("Save in contacts", null);
		dialogBuilder.setNeutralButton(R.string.remove, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					DBOpenHelper dbHelper = DBOpenHelperImpl.getInstance(getActivity());
					dbHelper.deleteProfile(profile.getId());
					contactListAdapter.removeItem(profile);
					contactListAdapter.notifyDataSetChanged();
					if (contactListAdapter.getCount() == 0) {
						noContactMessage.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					Log.e("Error while removing favourite");
				}
			}
		});
		dialogBuilder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});

		dialogBuilder.create().show();
	}
}
