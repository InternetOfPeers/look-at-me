package com.dreamteam.lookme.service;

public class Event {

	private EventType eventType;
	private Object eventObject;

	public Event(EventType eventType, Object eventObject) {
		this.eventType = eventType;
		this.eventObject = eventObject;
	}

	public EventType getEventType() {
		return eventType;
	}

	public Object getEventObject() {
		return eventObject;
	}
}
