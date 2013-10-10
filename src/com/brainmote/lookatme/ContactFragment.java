package com.brainmote.lookatme;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
		}
		return view;
	}
}
