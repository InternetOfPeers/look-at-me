package com.dreamteam.lookme;

import com.dreamteam.util.Notify;
import com.dreamteam.util.NotifyImpl;

public interface Services {

	public static Notify notify = NotifyImpl.Factory.getNotify();

}
