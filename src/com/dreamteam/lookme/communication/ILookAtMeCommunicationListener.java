/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.communication;

public interface ILookAtMeCommunicationListener {

	public void onCommunicationStarted();

	public void onCommunicationStopped();

	public void onBasicProfileNodeReceived(LookAtMeNode node);

	public void onNodeLeft(String nodeName);

	public void onFullProfileNodeReceived(LookAtMeNode node);

	public void onLikeReceived(String nodeFrom);

	public void onChatMessageReceived(String nodeFrom, String message);

	public void onStartChatMessageReceived(String nodeFrom, String channelName);

	public void onProfileNodeUpdated(LookAtMeNode node);

}
