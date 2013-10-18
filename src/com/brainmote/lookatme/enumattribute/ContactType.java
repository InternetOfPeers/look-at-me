package com.brainmote.lookatme.enumattribute;

public enum ContactType {

	PHONE("Phone"), EMAIL("Email"), LINKEDIN("LinkedIn"), FACEBOOK("Facebook");

	String contact;

	ContactType(String contactType) {
		this.contact = contactType;
	}

	public static String toString(ContactType contactType) {
		if (contactType == null)
			return null;
		return contactType.contact;
	}

	public static ContactType parse(String s) {
		if ("Phone".equals(s)) {
			return PHONE;
		} else if ("Email".equals(s)) {
			return EMAIL;
		} else if ("LinkedIn".equals(s)) {
			return LINKEDIN;
		} else if ("Facebook".equals(s)) {
			return FACEBOOK;
		}
		return null;
	}

	public String toString() {
		return contact;
	}

}
