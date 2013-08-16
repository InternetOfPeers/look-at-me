package com.dreamteam.lookme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.lookme.bean.Interest;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;

public class ManageInterestActivity extends CommonActivity {

	private InterestAdapter interestAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Activity activity = this;
		setContentView(R.layout.activity_manage_interest);
		initDrawerMenu(savedInstanceState, this.getClass(), false);

		// create an ArrayAdaptar from the String Array
		interestAdapter = new InterestAdapter(this, R.layout.interest_info_manage, R.id.interestId, Services.currentState.getMyFullProfile().getInterestList());

		final ListView listView = (ListView) findViewById(R.id.myInterestListView);
		// Assign adapter to ListView
		listView.setAdapter(interestAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Interest interest = (Interest) parent.getItemAtPosition(position);

				// Services.currentState.getMyFullProfile().removeInterest(interest);
				DBOpenHelperImpl.getInstance(activity).deleteInterest(interest.getId());

				Log.d("**************************************************** " + Services.currentState.getMyFullProfile().getInterestList().size()
						+ " **********************************");
				Toast.makeText(
						getApplicationContext(),
						"Removed: " + interest.getDesc() + "TO YOUR INTEREST, NOW YOU HAVE " + Services.currentState.getMyFullProfile().getInterestList().size()
								+ "interests", Toast.LENGTH_SHORT).show();

				interestAdapter = new InterestAdapter(activity, R.layout.interest_info_manage, R.id.interestId, Services.currentState.getMyFullProfile().getInterestList());
				listView.setAdapter(interestAdapter);
				interestAdapter.notifyDataSetChanged();
			}
		});

	}

	// @Override
	// public void onBackPressed() {
	// Log.d();
	// Nav.startActivity(this, EditProfileActivity.class);
	// }

	public void onAddInterest(View view) {
		Nav.startActivity(this, AddInterestActivity.class);
	}

	// LIST ADAPTER
	private class InterestAdapter extends ArrayAdapter<Interest> {

		private List<Interest> interestList = new ArrayList<Interest>();

		public InterestAdapter(Context context, int textViewResourceId, int interestID, List<Interest> interestList) {

			super(context, textViewResourceId, interestID, new ArrayList<Interest>(interestList));
			this.interestList = interestList;
		}

		private class ViewHolder {
			TextView code;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.interest_info_manage, null);

				holder = new ViewHolder();
				holder.code = (TextView) convertView.findViewById(R.id.interestId);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ArrayList<Interest> list = new ArrayList<Interest>(interestList);
			Interest interest = list.get(position);
			holder.code.setText(" (" + interest.getDesc() + ")");

			return convertView;

		}

	}

}
