package com.brainmote.lookatme.service;

import com.brainmote.lookatme.service.impl.BusinessLogicServiceImpl;
import com.brainmote.lookatme.service.impl.CurrentStateImpl;
import com.brainmote.lookatme.service.impl.EventBusImpl;
import com.brainmote.lookatme.service.impl.NotificationServiceImpl;
import com.squareup.otto.Bus;

public interface Services {

	// Sevizio per la gestione delle notifiche di sistema Android
	public static NotificationService notification = NotificationServiceImpl.Factory.getNotificationService();
	// Servizio per le operazioni di business
	public static BusinessLogicService businessLogic = BusinessLogicServiceImpl.Factory.getBusinessLogicService();
	// Servizio per la sottoscrizione alla ricezione degli eventi
	public static Bus event = EventBusImpl.Factory.getEventBus();
	public static CurrentState currentState = CurrentStateImpl.Factory.getCurrentState();

}
