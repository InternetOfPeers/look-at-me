package com.brainmote.lookatme.service;

import com.brainmote.lookatme.service.impl.BusinessLogicServiceImpl;
import com.brainmote.lookatme.service.impl.CurrentStateImpl;
import com.brainmote.lookatme.service.impl.EventBusImpl;
import com.brainmote.lookatme.service.impl.NotificationServiceImpl;
import com.squareup.otto.Bus;

public interface Services {

	/**
	 * Servizio per tutte le operazioni di business
	 */
	public static BusinessLogicService businessLogic = BusinessLogicServiceImpl.Factory.getBusinessLogicService();

	/**
	 * Stato corrente dell'applicazione. Le classi dovrebbero limitarsi a
	 * leggere da questo servizio, mentre solo pochi eletti dovrebbero essere
	 * ammessi a scrivere nello stato
	 */
	public static CurrentState currentState = CurrentStateImpl.Factory.getCurrentState();

	/**
	 * Servizio per la sottoscrizione alla ricezione degli eventi applicativi.
	 * Anche in questo caso l'idea è che le classi si registrino al bus per
	 * ascolatere ne notifiche, mentre alla gestione ci pensa il
	 * CommunicationListener
	 */
	public static Bus event = EventBusImpl.Factory.getEventBus();

	/**
	 * Sevizio per la gestione delle notifiche di sistema Android
	 */
	public static NotificationService notification = NotificationServiceImpl.Factory.getNotificationService();

}
