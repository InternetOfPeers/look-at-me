package com.brainmote.lookatme.enumattribute;

public enum Language {

	// <string name="language_fr_CA">CANADA_FRENCH</string>
	// <string name="language_zh">CHINESE</string>
	// <string name="language_en">ENGLISH</string>
	// <string name="language_fr">FRENCH</string>
	// <string name="language_de">GERMAN</string>
	// <string name="language_it">ITALIAN</string>
	// <string name="language_ja">JAPANESE</string>
	// <string name="language_ko">KOREAN</string>
	// <string name="language_zh_CN">SIMPLIFIED_CHINESE</string>
	// <string name="language_zh_TW">TRADITIONAL_CHINESE</string>

	fr_CA("CANADA_FRENCH"), zh("CHINESE"), en("ENGLISH"), fr("FRENCH"), it("ITALIAN"), de("GERMAN"),

	ja("JAPANESE"), ko("KOREAN"), zh_CN("SIMPLIFIED_CHINESE"), zh_TW("TRADITIONAL_CHINESE");

	String language;

	Language(String language) {
		this.language = language;
	}

	public static String toString(Language language) {
		if (language == null)
			return null;
		return language.language;
	}

	public static Language parse(String s) {
		if ("CANADA".equals(s) || "fr_CA".equals(s)) {
			return fr_CA;
		} else if ("CHINA".equals(s) || "zh".equals(s)) {
			return zh;
		} else if ("GERMAN".equals(s) || "de".equals(s)) {
			return de;
		} else if ("FRENCH".equals(s) || "fr".equals(s)) {
			return fr;
		} else if ("ITALIAN".equals(s) || "it".equals(s)) {
			return it;
		} else if ("JAPAN".equals(s) || "ja".equals(s)) {
			return ja;
		} else if ("KOREAN".equals(s) || "ko".equals(s)) {
			return ko;
		} else if ("SIMPLIFIED_CHINESE".equals(s) || "zh_CN".equals(s)) {
			return zh_CN;
		} else if ("TRADITIONAL_CHINESE".equals(s) || "zh_TW".equals(s)) {
			return zh_TW;
		}

		return null;
	}

	public String toString() {
		return this.language;
	}

}
