package com.dreamteam.lookme.communication;

public interface ILookAtMeCommunicationListener {
	
	public void onCommunicationStarted();
	
	public void onCommunicationStopped();
	
	public void onSocialNodeJoined();
	
	public void onSocialNodeLeft();

}
