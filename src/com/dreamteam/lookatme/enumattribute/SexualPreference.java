package com.dreamteam.lookatme.enumattribute;

public enum SexualPreference {
	
	M("Man"), F("Female"), NS("Not Specified");
	
	String sexualPreference;
	
	SexualPreference(String sp){
		this.sexualPreference = sp;
	}

	public static String toString(SexualPreference dest) {
		if (dest == null) return null; 
		return dest.sexualPreference;
	}

	public static SexualPreference parse(String s) {
		if ("Man".equals(s)) {
			return M;
		} else if ("Female".equals(s)) {
			return F;
		} else if ("Not Specified".equals(s)) {
			return NS;
		}
		return null;
	}
	
	public String toString() {
		return this.sexualPreference;
	}
}
