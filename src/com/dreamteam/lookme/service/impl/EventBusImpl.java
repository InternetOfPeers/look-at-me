package com.dreamteam.lookme.service.impl;

import com.squareup.otto.Bus;

public class EventBusImpl {

	private EventBusImpl() {
	}

	public static class Factory {
		private static Bus instance;

		public static Bus getEventBus() {
			return instance == null ? instance = new Bus() : instance;
		}
	}

}
