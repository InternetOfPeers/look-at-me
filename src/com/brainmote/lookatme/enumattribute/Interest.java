package com.brainmote.lookatme.enumattribute;

import java.util.ArrayList;
import java.util.List;

import com.brainmote.lookatme.R;

public enum Interest {

	// Special interest
	Manage_interest(InterestCategory.NOT_AN_INTEREST),

	// Categories
	Sport(InterestCategory.SPORT), Music(InterestCategory.MUSIC), Movie(InterestCategory.MOVIE), Hobby(InterestCategory.HOBBY),

	// Interests
	Football(InterestCategory.SPORT + 1), Volleyball(InterestCategory.SPORT + 2), Basketball(InterestCategory.SPORT + 3), Tennis(InterestCategory.SPORT + 4), Golf(
			InterestCategory.SPORT + 5), Boxe(InterestCategory.SPORT + 6), Martial_arts(InterestCategory.SPORT + 7), Ping_pong(InterestCategory.SPORT + 8), Beach_volley(
			InterestCategory.SPORT + 9), Beach_tennis(InterestCategory.SPORT + 10), Body_building(InterestCategory.SPORT + 11), Swimming(InterestCategory.SPORT + 12), Surf(
			InterestCategory.SPORT + 13), Wind_surf(InterestCategory.SPORT + 14), Running(InterestCategory.SPORT + 15),

	Classical(InterestCategory.MUSIC + 1), Rock(InterestCategory.MUSIC + 2), Hard_rock(InterestCategory.MUSIC + 3), Metal(InterestCategory.MUSIC + 4), Folk(
			InterestCategory.MUSIC + 5), Rythm_Blues(InterestCategory.MUSIC + 6), Soul(InterestCategory.MUSIC + 7), Jazz(InterestCategory.MUSIC + 8), Funk(
			InterestCategory.MUSIC + 9), New_age(InterestCategory.MUSIC + 10), Ska(InterestCategory.MUSIC + 11), Blues(InterestCategory.MUSIC + 12), Dance(
			InterestCategory.MUSIC + 13), Hip_Hop(InterestCategory.MUSIC + 14),

	Romantic(InterestCategory.MOVIE + 1), Horror(InterestCategory.MOVIE + 2), Comedy(InterestCategory.MOVIE + 3), Thriller(InterestCategory.MOVIE + 4), Psycological(
			InterestCategory.MOVIE + 5), Documentary(InterestCategory.MOVIE + 6), Drama(InterestCategory.MOVIE + 7), Musical(InterestCategory.MOVIE + 8),

	Cook(InterestCategory.HOBBY + 1), Garden(InterestCategory.HOBBY + 2), Painting(InterestCategory.HOBBY + 3), Playing_music(InterestCategory.HOBBY + 4), Listening_music(
			InterestCategory.HOBBY + 5), Reading(InterestCategory.HOBBY + 6);

	private final int value;

	private Interest(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public int getInterestCategory() {
		int result = InterestCategory.NOT_AN_INTEREST;
		if (isCategory())
			return result;
		else {
			if (value > InterestCategory.HOBBY && value < InterestCategory.HOBBY + InterestCategory.CATEGORY_GAP)
				result = InterestCategory.HOBBY;
			else if (value > InterestCategory.MOVIE && value < InterestCategory.MOVIE + InterestCategory.CATEGORY_GAP)
				result = InterestCategory.MOVIE;
			else if (value > InterestCategory.MUSIC && value < InterestCategory.MUSIC + InterestCategory.CATEGORY_GAP)
				result = InterestCategory.MUSIC;
			else if (value > InterestCategory.SPORT && value < InterestCategory.SPORT + InterestCategory.CATEGORY_GAP)
				result = InterestCategory.SPORT;
			return result;
		}
	}

	public boolean isCategory() {
		return value == InterestCategory.HOBBY || value == InterestCategory.SPORT || value == InterestCategory.MOVIE || value == InterestCategory.MUSIC;
	}

	@Override
	public String toString() {
		return name().replaceAll("_", " ");
	}

	public static Interest getInterestWithName(String name) {
		return valueOf(name.replaceAll(" ", "_"));
	}

	public static Interest getInterestWithValue(int value) {
		for (Interest interest : values()) {
			if (interest.getValue() == value) {
				return interest;
			}
		}
		return null;
	}

	public static class InterestCategory {

		public static final int CATEGORY_GAP = 10000;
		public static final int NOT_AN_INTEREST = 0;
		public static final int SPORT = InterestCategory.CATEGORY_GAP * 1;
		public static final int MUSIC = InterestCategory.CATEGORY_GAP * 2;
		public static final int MOVIE = InterestCategory.CATEGORY_GAP * 3;
		public static final int HOBBY = InterestCategory.CATEGORY_GAP * 4;

		private static final int SPORT_ICON = R.drawable.ic_sport;
		private static final int MUSIC_ICON = R.drawable.ic_music;
		private static final int MOVIE_ICON = R.drawable.ic_movie;
		private static final int HOBBY_ICON = R.drawable.ic_misc;

		public static List<String> getInterestCategoryList() {
			List<String> result = new ArrayList<String>();
			Interest[] interests = Interest.values();
			for (Interest interest : interests) {
				if (interest.isCategory()) {
					result.add(interest.toString());
				}
			}

			return result;
		}

		public static List<String> getInterestOfCategory(int category) {
			List<String> result = new ArrayList<String>();
			Interest[] interests = Interest.values();
			for (Interest interest : interests) {
				if (interest.getInterestCategory() == category) {
					result.add(interest.toString());
				}
			}

			return result;
		}

		public static int getIconOfCategory(int category) {
			switch (category) {
			case SPORT:
				return SPORT_ICON;
			case MOVIE:
				return MOVIE_ICON;
			case MUSIC:
				return MUSIC_ICON;
			case HOBBY:
				return HOBBY_ICON;
			default:
				return R.drawable.ic_void;
			}
		}

	}
}
