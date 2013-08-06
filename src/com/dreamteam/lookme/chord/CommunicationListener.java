package com.dreamteam.lookme.chord;

public interface CommunicationListener {

	public void onCommunicationStarted();

	public void onCommunicationStopped();

	public void onBasicProfileNodeReceived(Node node);

	public void onNodeLeft(String nodeName);

	public void onFullProfileNodeReceived(Node node);

	public void onLikeReceived(String nodeFrom);

	public void onChatMessageReceived(String nodeFrom, String message);

	public void onProfileNodeUpdated(Node node);

}
