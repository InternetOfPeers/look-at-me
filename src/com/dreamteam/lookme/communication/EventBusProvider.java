package com.dreamteam.lookme.communication;

import com.squareup.otto.Bus;

public class EventBusProvider {

	private static Bus BUS;

	private EventBusProvider() {
	}

	public static Bus getIntance() {
		if (BUS == null) {
			BUS = new Bus();
		}
		return BUS;
	}
}
