/**
 * Author: Carlo Tassi
 * 
 * Interface for managing device communication.
 * This is the main access point for Activities
 * to start communication between devices.
 */
package com.dreamteam.lookme.communication;

import java.util.List;
import java.util.Map;

import com.dreamteam.lookme.bean.MessageItem;
import com.dreamteam.lookme.error.LookAtMeException;
import com.samsung.chord.IChordChannel;

public interface ILookAtMeCommunicationManager {

	public void startCommunication() throws LookAtMeException;

	public void stopCommunication();

	public boolean sendProfilePreviewRequestAll(); /*
													 * Maybe not necessary (to
													 * testing)
													 */

	public boolean sendProfileRequest(String nodeTo);

	public boolean sendProfilePreviewAll(); /*
											 * to communicate all nodes my
											 * profile is updated
											 */

	public IChordChannel joinChannel(String channelName);
	
	public boolean sendChatMessage(LookAtMeNode nodeTo, String message,String channelName);
	
	

	public boolean sendLike(String nodeTo);
	
	public boolean sendPrivateChatMessage(String nodeTo, String message,String channelName);
	
	public IChordChannel getChannel(String channelName);
	
	public List<MessageItem> getChat(String channelName);
	
	public Map<String,IChordChannel> getOpenChannels();
	
	public Map<String,List<MessageItem>> getOpenedChat();

}
