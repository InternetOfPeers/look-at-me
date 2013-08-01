package com.dreamteam.lookme.service;

import com.dreamteam.lookme.service.impl.BusinessLogicServiceImpl;
import com.dreamteam.lookme.service.impl.CurrentStateImpl;
import com.dreamteam.lookme.service.impl.EventBusImpl;
import com.dreamteam.lookme.service.impl.NotifyImpl;
import com.squareup.otto.Bus;

public interface Services {

	public static Notify notify = NotifyImpl.Factory.getNotify();
	public static BusinessLogicService businessLogic = BusinessLogicServiceImpl.Factory.getBusinessLogicService();
	public static Bus eventBus = EventBusImpl.Factory.getEventBus();
	public static CurrentState currentState = CurrentStateImpl.Factory.getCurrentState();

}
