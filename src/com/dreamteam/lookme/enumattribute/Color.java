package com.dreamteam.lookme.enumattribute;

public enum Color {

	BLACK("Black"), BROWN("Brown"), BLONDE("Blonde"), AUBURN("Auburn"), // castano
																		// ramato
	CHESTNUT("Chestnut"), // castano
	RED("Red"), GREY("Grey"), WHYTE("Whyte"),

	AMBER("Amber"), BLUE("Blue"), GREEN("Green"), HAZEL("Hazel"), // castani/verdi
																	// per gli
																	// occhi
	VIOLET("Violet");

	String color;

	Color(String color) {
		this.color = color;
	}

	public static String toString(Color color) {
		if (color == null)
			return null;
		return color.color;
	}

	public static Color parse(String s) {
		if ("Black".equals(s)) {
			return BLACK;
		} else if ("Brown".equals(s)) {
			return BROWN;
		} else if ("Blonde".equals(s)) {
			return BLONDE;
		} else if ("Auburn".equals(s)) {
			return AUBURN;
		} else if ("Chestnut".equals(s)) {
			return CHESTNUT;
		} else if ("Red".equals(s)) {
			return RED;
		} else if ("Grey".equals(s)) {
			return GREY;
		} else if ("Whyte".equals(s)) {
			return WHYTE;
		} else if ("Amber".equals(s)) {
			return AMBER;
		} else if ("Blue".equals(s)) {
			return BLUE;
		} else if ("Green".equals(s)) {
			return GREEN;
		} else if ("Hazel".equals(s)) {
			return HAZEL;
		} else if ("Violet".equals(s)) {
			return VIOLET;
		}
		return null;
	}

	public String toString() {
		return this.color;
	}

}
