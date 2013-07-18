package com.dreamteam.lookatme.enumattribute;

public enum Gender {

	M("Male"), F("Female"), TG("Transgender");

	String gender;
	
	Gender(String gender) {
		this.gender = gender;
	}

	public static String toString(Gender dest) {
		if (dest == null) return null; 
		return dest.gender;
	}

	public static Gender parse(String s) {
		if ("Male".equals(s)) {
			return M;
		} else if ("Female".equals(s)) {
			return F;
		} else if ("Transgender".equals(s)) {
			return TG;
		}
		return null;
	}
	
	public String toString() {
		return gender;
	}

}
