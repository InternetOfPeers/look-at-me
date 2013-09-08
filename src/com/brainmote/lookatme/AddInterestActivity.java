package com.brainmote.lookatme;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brainmote.lookatme.bean.Interest;
import com.brainmote.lookatme.db.DBOpenHelperImpl;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.Log;

public class AddInterestActivity extends CommonActivity {

	private InterestAdapter interestAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Activity activity = this;
		setContentView(R.layout.activity_add_interest);
		initDrawerMenu(savedInstanceState, this.getClass(), false);

		// create an ArrayAdaptar from the String Array
		interestAdapter = new InterestAdapter(this, R.layout.interest_info_add, R.id.interestId, Services.currentState.getInterestList());

		final ListView listView = (ListView) findViewById(R.id.interestListView);
		// Assign adapter to ListView
		listView.setAdapter(interestAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// When clicked, show a toast with the TextView text
				Interest interest = (Interest) parent.getItemAtPosition(position);

				DBOpenHelperImpl.getInstance(activity).saveInterest(interest);
				// Services.currentState.getMyFullProfile().addInterest(interest);

				Log.d("**************************************************** " + Services.currentState.getMyFullProfile().getInterestList().size()
						+ " **********************************");
				Toast.makeText(
						getApplicationContext(),
						"Added: " + interest.getDesc() + "TO YOUR INTEREST, NOW YOU HAVE " + Services.currentState.getMyFullProfile().getInterestList().size() + "interests",
						Toast.LENGTH_SHORT).show();

				// create an ArrayAdaptar from the String Array
				interestAdapter = new InterestAdapter(activity, R.layout.interest_info_add, R.id.interestId, Services.currentState.getInterestList());
				listView.setAdapter(interestAdapter);
				interestAdapter.notifyDataSetChanged();
			}
		});

	}

	// LIST ADAPTER
	private class InterestAdapter extends ArrayAdapter<Interest> {

		private TreeSet<Interest> interestList;

		public InterestAdapter(Context context, int textViewResourceId, int interestID, Set<Interest> interestList) {

			super(context, textViewResourceId, interestID, new ArrayList<Interest>(interestList));
			this.interestList = new TreeSet<Interest>();
			this.interestList.addAll(interestList);
		}

		private class ViewHolder {
			TextView code;
			CheckBox cb;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.interest_info_add, null);

				holder = new ViewHolder();
				holder.code = (TextView) convertView.findViewById(R.id.interestId);
				holder.cb = (CheckBox) convertView.findViewById(R.id.checkBoxAddInterest);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ArrayList<Interest> list = new ArrayList<Interest>(interestList);
			Interest interest = list.get(position);
			holder.code.setText(" (" + interest.getDesc() + ")");

			for (Interest profileInterest : Services.currentState.getMyFullProfile().getInterestList()) {
				System.out.println(position + " ########################### PROFILE INTEREST_ID = " + profileInterest.getId() + " ########################### ");
				if (profileInterest.getId() == interest.getId()) {
					System.out.println(position + " ########################### COINCIDONOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO ########################### ");
					holder.cb.setChecked(true);
					holder.code.setClickable(false);
					convertView.setClickable(false);
				}
			}
			return convertView;

		}

	}

}
