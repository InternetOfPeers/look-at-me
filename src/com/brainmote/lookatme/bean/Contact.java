package com.brainmote.lookatme.bean;

import java.io.Serializable;

import com.brainmote.lookatme.enumattribute.ContactType;

public class Contact implements Serializable {

	private String profileId;
	private ContactType contactType;
	private String reference;

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

}
