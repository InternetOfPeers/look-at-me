package com.brainmote.lookatme.chord;

import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.Profile;

public class Node {

	private String id;
	private BasicProfile basicProfile;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Profile getProfile() {
		return basicProfile;
	}

	public void setProfile(BasicProfile basicProfile) {
		this.basicProfile = basicProfile;
	}

}
