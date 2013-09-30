package com.brainmote.lookatme.bean;

public class Interest implements Comparable<Interest> {

	private int id;

	private Interest parent;

	private boolean selected;

	private String desc;

	public Interest(int id, String desc, boolean selected) {
		this.id = id;
		this.selected = selected;
		this.desc = desc;
	}

	public Interest getParent() {
		return parent;
	}

	public void setParent(Interest parent) {
		this.parent = parent;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int compareTo(Interest another) {
		return Integer.valueOf(this.getId()).compareTo(Integer.valueOf(another.getId()));
	}

	@Override
	public boolean equals(Object object) {
		return this == object || Integer.valueOf(this.getId()).equals(Integer.valueOf(((Interest) object).getId()));
	}

}
