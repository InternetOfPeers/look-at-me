package com.brainmote.lookatme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.QuickContactBadge;

public class ContactListAdapter extends BaseAdapter {
	
	private Activity activity;
	private List<String> contactList;
	
	public ContactListAdapter(Activity activity) {
		this.activity = activity;
		// fake list da sostituire quando 
		contactList = new ArrayList<String>();
		contactList.add("UNO");
		contactList.add("DUE");
		contactList.add("TRE");
	}

	@Override
	public int getCount() {
		return contactList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return contactList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.contact_list_row, null);
		}
		QuickContactBadge contactBadge = (QuickContactBadge) convertView.findViewById(R.id.contactBadge);
		contactBadge.setImageResource(R.drawable.ic_profile_image);
		contactBadge.assignContactFromEmail("any@gmail.com", true);  
		contactBadge.assignContactFromPhone("3211234567", true);
		//contactBadge.setMode(ContactsContract.QuickContact.MODE_SMALL); 
		
		ImageView imageFacebook = (ImageView) convertView.findViewById(R.id.imageContactFacebook);
		imageFacebook.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("http://www.facebook.com"));
		        activity.startActivity(intent);
		    }
		});
		ImageView imageLinkedin = (ImageView) convertView.findViewById(R.id.imageContactLinkedin);
		imageLinkedin.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("http://www.linkedin.com"));
		        activity.startActivity(intent);
		    }
		});
		return convertView;
	}

}
