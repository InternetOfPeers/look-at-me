/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.communication;

public interface ILookAtMeCommunicationListener {

	public void onCommunicationStarted();

	public void onCommunicationStopped();

	public void onSocialNodeJoined(LookAtMeNode node);

	public void onSocialNodeLeft(String nodeName);

}
