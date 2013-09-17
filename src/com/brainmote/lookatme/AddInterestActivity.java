package com.brainmote.lookatme;

import android.os.Bundle;
import android.widget.ExpandableListView;

public class AddInterestActivity extends CommonActivity {

	private ExpandableListView interestList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_interest);
		initDrawerMenu(savedInstanceState, this.getClass(), false);
		
		interestList = (ExpandableListView) findViewById(R.id.expandableListInterests);
		interestList.setAdapter(new InterestAdapter(getApplicationContext()));

	}

//	// LIST ADAPTER
//	private class InterestAdapter extends ArrayAdapter<Interest> {
//
//		private TreeSet<Interest> interestList;
//
//		public InterestAdapter(Context context, int textViewResourceId, int interestID, Set<Interest> interestList) {
//
//			super(context, textViewResourceId, interestID, new ArrayList<Interest>(interestList));
//			this.interestList = new TreeSet<Interest>();
//			this.interestList.addAll(interestList);
//		}
//
//		private class ViewHolder {
//			TextView code;
//			CheckBox cb;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//
//			ViewHolder holder = null;
//
//			if (convertView == null) {
//				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				convertView = vi.inflate(R.layout.interest_info_add, null);
//
//				holder = new ViewHolder();
//				holder.code = (TextView) convertView.findViewById(R.id.interestId);
//				holder.cb = (CheckBox) convertView.findViewById(R.id.checkBoxAddInterest);
//				convertView.setTag(holder);
//
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//
//			ArrayList<Interest> list = new ArrayList<Interest>(interestList);
//			Interest interest = list.get(position);
//			holder.code.setText(" (" + interest.getDesc() + ")");
//
//			for (Interest profileInterest : Services.currentState.getMyFullProfile().getInterestList()) {
//				System.out.println(position + " ########################### PROFILE INTEREST_ID = " + profileInterest.getId() + " ########################### ");
//				if (profileInterest.getId() == interest.getId()) {
//					System.out.println(position + " ########################### COINCIDONOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO ########################### ");
//					holder.cb.setChecked(true);
//					holder.code.setClickable(false);
//					convertView.setClickable(false);
//				}
//			}
//			return convertView;
//
//		}
//
//	}

}
