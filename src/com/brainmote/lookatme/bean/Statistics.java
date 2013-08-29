package com.brainmote.lookatme.bean;

public class Statistics {

	private static final int VISIT_WEIGTH = 1;
	private static final int LIKE_WEIGHT = 3;

	private int visitCount;
	private int likeCount;

	public Statistics() {
		visitCount = 0;
		likeCount = 0;
	}

	public float getScore() {
		if (getVisitCount() == 0)
			return 0;
		return (float) VISIT_WEIGTH + (((float) getLikeCount() * (float) LIKE_WEIGHT) / (float) getVisitCount());
	}

	public float getRating() {
		float score = getScore();
		float rating = 0;
		if (score >= 2 && score < 4)
			rating = 0.5f;
		else if (score >= 4 && score < 6)
			rating = 1;
		else if (score >= 6 && score < 8)
			rating = 1.5f;
		else if (score >= 8 && score < 10)
			rating = 2;
		else if (score >= 10 && score < 12)
			rating = 2.5f;
		else if (score >= 12)
			rating = 3;
		return rating;
	}

	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public void incVisit() {
		this.visitCount++;
	}

	public void incLike() {
		this.likeCount++;
	}

}
