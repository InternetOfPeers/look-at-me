package com.dreamteam.lookme.enumattribute;

public  enum Country {


	CA("CANADA"), CN("CHINA"), DE("GERMANY"), FR("FRANCE"),IT("ITALY"), 

	JA("JAPAN"),KR("KOREA"),TW("TAIWAN"),UK("UK"),US("US");

	String country;

	Country(String country) {
		this.country = country;
	}

	public static String toString(Country country) {
		if (country == null)
			return null;
		return country.country;
	}

	public static Country parse(String s) {
		if ("CANADA".equals(s)||"CA".equals(s)) {
			return CA;
		} else if ("CHINA".equals(s)||"CN".equals(s)) {
			return CN;
		} else if ("GERMANY".equals(s)||"DE".equals(s)) {
			return DE;
		} else if ("FRANCE".equals(s)||"FR".equals(s)) {
			return FR;
		} else if ("ITALY".equals(s)||"IT".equals(s)) {
			return IT;
		} else if ("JAPAN".equals(s)||"JA".equals(s)) {
			return JA;
		} else if ("KOREA".equals(s)||"KR".equals(s)) {
			return KR;
		} else if ("TAIWAN".equals(s)||"TW".equals(s)) {
			return TW;
		} else if ("UK".equals(s)) {
			return UK;
		} else if ("US".equals(s)) {
			return US;
		} 
		return null;
	}

	public String toString() {
		return this.country;
	}

}
