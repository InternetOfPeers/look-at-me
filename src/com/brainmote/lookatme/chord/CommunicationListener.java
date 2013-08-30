package com.brainmote.lookatme.chord;

public interface CommunicationListener {

	public void onBasicProfileNodeReceived(Node node);

	public void onNodeJoined(String nodeId);

	public void onNodeLeft(String nodeName);

	public void onFullProfileNodeReceived(Node node);

	public void onLikeReceived(String fromNode);

	public void onChatMessageReceived(String fromNode, String message);

	public void onFullProfileRequestReceived(String nodeId);

}
