package com.brainmote.lookatme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

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

		ImageView imageMobile = (ImageView) convertView.findViewById(R.id.imageContactMobile);
		imageMobile.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_CALL);
		        intent.setData(Uri.parse("tel:00393483953186"));
		        activity.startActivity(intent);
		        //activity.startActivity(Intent.createChooser(intent, "How call:"));
		    }
		});
		
		ImageView imageEmail = (ImageView) convertView.findViewById(R.id.imageContactEmail);
		imageEmail.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		    	Intent email = new Intent(Intent.ACTION_SEND);
		    	email.putExtra(Intent.EXTRA_EMAIL, new String[]{"youremail@yahoo.com"});		  
		    	email.putExtra(Intent.EXTRA_SUBJECT, "subject");
		    	email.putExtra(Intent.EXTRA_TEXT, "message");
		    	email.setType("message/rfc822");
		    	activity.startActivity(Intent.createChooser(email, "Choose an Email client:"));
		    }
		});
		
		ImageView imageFacebook = (ImageView) convertView.findViewById(R.id.imageContactFacebook);
		imageFacebook.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		    	
		    	try{
		    	    activity.getPackageManager().getApplicationInfo("com.facebook.android", 0 );
		    	    String uri = "facebook://facebook.com/pirone.stefano";
			        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			        activity.startActivity(intent);
		    	} catch( PackageManager.NameNotFoundException e ){
		    		String uri = "http://facebook.com/pirone.stefano";
			        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
			        activity.startActivity(intent);
		    	}
		    	
		       

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
