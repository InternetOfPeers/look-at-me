package com.brainmote.lookatme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.Contact;
import com.brainmote.lookatme.constants.AppSettings;
import com.brainmote.lookatme.util.ImageUtil;

public class ContactListAdapter extends BaseAdapter {

	private Activity activity;
	private List<BasicProfile> contactList;

	public ContactListAdapter(Activity activity) {
		this.activity = activity;
		contactList = new ArrayList<BasicProfile>();
	}

	public ContactListAdapter(Activity activity, List<BasicProfile> contactList) {
		this.activity = activity;
		this.contactList = contactList;
	}

	public void removeItem(BasicProfile profile) {
		if (contactList != null) {
			contactList.remove(profile);
		}
	}

	@Override
	public int getCount() {
		if (contactList == null) {
			return 0;
		}
		return contactList.size();
	}

	@Override
	public Object getItem(int arg0) {
		if (contactList == null) {
			return null;
		}
		return contactList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BasicProfile profile = (BasicProfile) getItem(position);
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.contact_list_row, null);
		}

		TextView textName = (TextView) convertView.findViewById(R.id.textContactName);
		TextView textSurname = (TextView) convertView.findViewById(R.id.textContactSurname);
		textName.setText(profile.getName());
		textSurname.setText(profile.getSurname());
		TextView textNick = (TextView) convertView.findViewById(R.id.textContactNick);
		TextView textAge = (TextView) convertView.findViewById(R.id.textContactAge);
		textNick.setText(profile.getNickname() + ",");
		textAge.setText(profile.getAge() + "");

		ImageView imageContact = (ImageView) convertView.findViewById(R.id.imageContact);
		ImageView imageMobile = (ImageView) convertView.findViewById(R.id.imageContactMobile);
		ImageView imageEmail = (ImageView) convertView.findViewById(R.id.imageContactEmail);
		ImageView imageFacebook = (ImageView) convertView.findViewById(R.id.imageContactFacebook);
		ImageView imageLinkedin = (ImageView) convertView.findViewById(R.id.imageContactLinkedin);

		Bitmap mainImageProfile = ImageUtil.getBitmapProfileImage(activity.getResources(), profile);
		Bitmap croppedImageProfile = ImageUtil.bitmapForThumbnail(mainImageProfile);
		imageContact.setImageBitmap(croppedImageProfile);

		imageMobile.setVisibility(View.GONE);
		imageEmail.setVisibility(View.GONE);
		imageFacebook.setVisibility(View.GONE);
		imageLinkedin.setVisibility(View.GONE);

		if (profile.getContactList() != null) {
			for (Contact contact : profile.getContactList()) {
				final String reference = contact.getReference();
				switch (contact.getContactType()) {
				case EMAIL:
					imageEmail.setVisibility(View.VISIBLE);
					imageEmail.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Intent email = new Intent(Intent.ACTION_SEND);
							email.putExtra(Intent.EXTRA_EMAIL, new String[] { reference });
							email.putExtra(Intent.EXTRA_SUBJECT, "subject");
							email.putExtra(Intent.EXTRA_TEXT, "message");
							email.setType("message/rfc822");
							activity.startActivity(Intent.createChooser(email, "Choose an Email client:"));
						}
					});
					break;
				case FACEBOOK:
					imageFacebook.setVisibility(View.VISIBLE);
					imageFacebook.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							String uri = AppSettings.URL_PREFIX_FACEBOOK + reference;
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
							activity.startActivity(intent);
						}
					});
					break;
				case LINKEDIN:
					imageLinkedin.setVisibility(View.VISIBLE);
					imageLinkedin.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setAction(Intent.ACTION_VIEW);
							intent.addCategory(Intent.CATEGORY_BROWSABLE);
							intent.setData(Uri.parse(AppSettings.URL_PREFIX_LINKEDIN + reference));
							activity.startActivity(intent);
						}
					});
					break;
				case PHONE:
					imageMobile.setVisibility(View.VISIBLE);
					imageMobile.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							Intent intent = new Intent();
							intent.setAction(Intent.ACTION_CALL);
							intent.setData(Uri.parse("tel:" + reference));
							activity.startActivity(intent);
						}
					});
					break;
				default:
					break;
				}
			}
		}

		return convertView;
	}

}
