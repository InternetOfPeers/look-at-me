package com.dreamteam.lookme;

import android.content.ContextWrapper;

public interface BusinessLogicOperations {

	public void start(ContextWrapper context);

	public void stop(ContextWrapper context);

	public boolean isRunning();
}
