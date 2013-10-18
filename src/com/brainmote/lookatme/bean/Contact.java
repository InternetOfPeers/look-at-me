package com.brainmote.lookatme.bean;

import java.io.Serializable;

import com.brainmote.lookatme.enumattribute.ContactType;

public class Contact implements Serializable {

	private static final long serialVersionUID = 1L;

	private String profileId;
	private ContactType contactType;
	private String reference;

	public String getProfileId() {
		return profileId;
	}

	public Contact setProfileId(String profileId) {
		this.profileId = profileId;
		return this;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public Contact setContactType(ContactType contactType) {
		this.contactType = contactType;
		return this;
	}

	public String getReference() {
		return reference;
	}

	public Contact setReference(String reference) {
		this.reference = reference;
		return this;
	}

}
