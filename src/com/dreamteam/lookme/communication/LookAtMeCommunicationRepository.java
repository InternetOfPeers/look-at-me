package com.dreamteam.lookme.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.dreamteam.lookme.CommonActivity;
import com.dreamteam.lookme.bean.MessageItem;
import com.dreamteam.lookme.event.SocialNodeProfileReceivedEvent;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.Log;
import com.squareup.otto.Produce;

public  class LookAtMeCommunicationRepository {
	
	private static ILookAtMeCommunicationListener listener= null;	
	
	public static Map<String,List<MessageItem>> messagesHistoryMap= new HashMap<String, List<MessageItem>>();	
	
	public static Map<String, LookAtMeNode> socialNodeMap = new HashMap<String, LookAtMeNode> ();

	public static Set<String> iLike = new TreeSet<String>();
		
	public static LookAtMeNode profileViewed;
	
	public static Set<String> liked =new TreeSet<String>();
	
	private LookAtMeCommunicationRepository()
	{
		
	}
	
	public static ILookAtMeCommunicationListener getInstance()
	{
		if(listener==null)
		{
			listener=new ILookAtMeCommunicationListener() {
				
				@Override
				public void onSocialNodeUpdated(LookAtMeNode node) {
					Log.d();
					// TODO Auto-generated method stub
					socialNodeMap.remove(node.getId());
					socialNodeMap.put(node.getId(),node);
					
				}
				
				@Override
				public void onSocialNodeProfileReceived(LookAtMeNode node) {
					Log.d();					
					profileViewed=node;
					onSocialNodeProfileProduced(node);
				}
				
				@Produce
				public SocialNodeProfileReceivedEvent onSocialNodeProfileProduced(LookAtMeNode node) {
					Log.d();					
					return new SocialNodeProfileReceivedEvent(node);
				}
				
				@Override
				public void onSocialNodeLeft(String nodeName) {
					Log.d();
					// remove node from socialNodeMap
					socialNodeMap.remove(nodeName);
					
				}
				
				@Override
				public void onSocialNodeJoined(LookAtMeNode node) {
					Log.d();					
					socialNodeMap.put(node.getId(),node);
					
				}
				
				@Override
				public void onLikeReceived(String nodeFrom) {
					Log.d();	
					liked.add(nodeFrom);					
				}
				
				@Override
				public void onCommunicationStopped() {
					Log.d();	
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onCommunicationStarted() {
					Log.d();	
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onChatMessageReceived(LookAtMeNode nodeFrom, String message) {
					Log.d();	
					String channelName=CommonUtils.generateChannelName(nodeFrom.getProfile().getId(), CommonActivity.myProfile.getId());					
					MessageItem messageItem= new MessageItem(nodeFrom, message, false);
					messagesHistoryMap.get(channelName).add(messageItem);
				}
				
				@Override
				public void onChatMessageReceived(String nodeFrom, String message) {
					Log.d();	
					String channelName=CommonUtils.generateChannelName(nodeFrom, CommonActivity.myProfile.getId());					
					MessageItem messageItem= new MessageItem(socialNodeMap.get(nodeFrom), message, false);
					messagesHistoryMap.get(channelName).add(messageItem);					
				}
			};
		}
		return listener;
	}

}
