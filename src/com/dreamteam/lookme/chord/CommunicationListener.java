package com.dreamteam.lookme.chord;

public interface CommunicationListener {

	public void onCommunicationStarted();

	public void onCommunicationStopped();

	public void onBasicProfileNodeReceived(Node node);

	public void onNodeLeft(String nodeName);

	public void onFullProfileNodeReceived(Node node);

	public void onLikeReceived(String fromNode);

	public void onChatMessageReceived(String fromNode, String message);

	public void onProfileNodeUpdated(Node node);

}
