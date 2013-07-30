/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.communication;

public interface ILookAtMeCommunicationListener {

	public void onCommunicationStarted();

	public void onCommunicationStopped();

	public void onSocialNodeJoined(LookAtMeNode node);

	public void onSocialNodeLeft(String nodeName);

	public void onSocialNodeProfileReceived(LookAtMeNode node);

	public void onLikeReceived(String nodeFrom);

	public void onChatMessageReceived(String nodeFrom, String message);

	public void onStartChatMessageReceived(String nodeFrom, String channelName);

	public void onSocialNodeUpdated(LookAtMeNode node);

}
