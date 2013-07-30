package com.dreamteam.lookme.event;

public class LookAtMeEvent {

	private LookAtMeEventType eventType;
	private Object eventObject;

	public LookAtMeEvent(LookAtMeEventType eventType, Object eventObject) {
		this.eventType = eventType;
		this.eventObject = eventObject;
	}

	public LookAtMeEventType getEventType() {
		return eventType;
	}

	public Object getEventObject() {
		return eventObject;
	}
}
