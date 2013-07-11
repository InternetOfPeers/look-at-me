/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.chord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.dreamteam.lookme.bean.Profile;
import com.dreamteam.lookme.communication.ILookAtMeCommunicationManager;
import com.dreamteam.lookme.communication.LookAtMeMessage;
import com.dreamteam.lookme.communication.LookAtMeMessageType;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.error.LookAtMeErrorManager;
import com.dreamteam.lookme.error.LookAtMeException;
import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

public class LookAtMeChordCommunicationManager implements ILookAtMeCommunicationManager {
	
	private static final String TAG = "[LOOKATME_CHORD]";
	private static final String TAGClass = "[LookAtMeChordCommunicationManager]";
	
	public static final String SOCIAL_CHANNEL_NAME = "com.dreamteam.lookme.SOCIAL_CHANNEL";

	private ChordManager chord;
	private IChordChannel publicChannel;
	private IChordChannel socialChannel;
	
	private List<Integer> availableWifiInterface;
	private int currentWifiInterface;
	
	private LookAtMeErrorManager errorManager;
	
	private Map<String, LookAtMeNode> socialNodeMap;

	public LookAtMeChordCommunicationManager(Context context) {
		Log.d(TAG, TAGClass + " : " + "LookAtMeChordCommunicationManager");
		chord = ChordManager.getInstance(context);
		errorManager = new LookAtMeErrorManager();
	}

	@Override
	public void startCommunication() throws LookAtMeException {
		Log.d(TAG, TAGClass + " : " + "startCommunication");
		int result = startChordCommunication();
		errorManager.checkError(result);
		
		publicChannel = joinPublicChannel();
		socialChannel = joinSocialChannel();
	}

	@Override
	public void stopCommunication() {
		Log.d(TAG, TAGClass + " : " + "stopCommunication");
		if (publicChannel != null) {
			chord.leaveChannel(ChordManager.PUBLIC_CHANNEL);
		}
		if (socialChannel != null) {
			chord.leaveChannel(SOCIAL_CHANNEL_NAME);
		}
		chord.stop();
	}

	@Override
	public List<LookAtMeNode> getSocialNodeList() {
		Log.d(TAG, TAGClass + " : " + "getSocialNodeList");
		if (socialNodeMap == null) {
			sendProfilePreviewRequestAll();
			socialNodeMap = new HashMap<String, LookAtMeNode>();
		}
		return (List<LookAtMeNode>) socialNodeMap.values();
	}
	
	private IChordChannel joinPublicChannel() {
		Log.d(TAG, TAGClass + " : " + "joinPublicChannel");
		return chord.joinChannel(ChordManager.PUBLIC_CHANNEL, new IChordChannelListener() {
			
			@Override
			public void onNodeLeft(String arg0, String arg1) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener public : onNodeLeft NOT IMPLEMENTED");
			}
			
			@Override
			public void onNodeJoined(String arg0, String arg1) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener public : onNodeJoined NOT IMPLEMENTED");
			}
			
			@Override
			public void onFileWillReceive(String arg0, String arg1, String arg2,
					String arg3, String arg4, String arg5, long arg6) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener public : onFileWillReceive NOT IMPLEMENTED");
			}
			
			@Override
			public void onFileSent(String arg0, String arg1, String arg2, String arg3,
					String arg4, String arg5) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener public : onFileSent NOT IMPLEMENTED");
			}
			
			@Override
			public void onFileReceived(String arg0, String arg1, String arg2,
					String arg3, String arg4, String arg5, long arg6, String arg7) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener public : onFileReceived NOT IMPLEMENTED");
			}
			
			@Override
			public void onFileFailed(String arg0, String arg1, String arg2,
					String arg3, String arg4, int arg5) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener public : onFileFailed");
			}
			
			@Override
			public void onFileChunkSent(String arg0, String arg1, String arg2,
					String arg3, String arg4, String arg5, long arg6, long arg7,
					long arg8) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener public : onFileChunkSent NOT IMPLEMENTED");
			}
			
			@Override
			public void onFileChunkReceived(String arg0, String arg1, String arg2,
					String arg3, String arg4, String arg5, long arg6, long arg7) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener public : onFileChunkReceived NOT IMPLEMENTED");
			}
			
			@Override
			public void onDataReceived(String arg0, String arg1, String arg2,
					byte[][] arg3) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener public : onDataReceived NOT IMPLEMENTED");
			}
		});
	}
	
	private IChordChannel joinSocialChannel() {
		Log.d(TAG, TAGClass + " : " + "joinSocialChannel");
		return chord.joinChannel(SOCIAL_CHANNEL_NAME, new IChordChannelListener() {
			
			@Override
			public void onNodeLeft(String arg0, String arg1) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener social : onNodeLeft");
				// remove node from map
				socialNodeMap.remove(arg0);
				// TODO: notify for update GUI
			}
			
			@Override
			public void onNodeJoined(String arg0, String arg1) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener social : onNodeJoined");
				// send a preview profile request
				sendProfilePreviewRequest(arg0);
				// GUI will be notified after receive his profile
			}
			
			@Override
			public void onFileWillReceive(String arg0, String arg1, String arg2,
					String arg3, String arg4, String arg5, long arg6) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener social : onFileWillReceive NOT IMPLEMENTED");
			}
			
			@Override
			public void onFileSent(String arg0, String arg1, String arg2, String arg3,
					String arg4, String arg5) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener social : onFileSent NOT IMPLEMENTED");
			}
			
			@Override
			public void onFileReceived(String arg0, String arg1, String arg2,
					String arg3, String arg4, String arg5, long arg6, String arg7) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener social : onFileReceived NOT IMPLEMENTED");
			}
			
			@Override
			public void onFileFailed(String arg0, String arg1, String arg2,
					String arg3, String arg4, int arg5) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener social : onFileFailed NOT IMPLEMENTED");
			}
			
			@Override
			public void onFileChunkSent(String arg0, String arg1, String arg2,
					String arg3, String arg4, String arg5, long arg6, long arg7,
					long arg8) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener social : onFileChunkSent NOT IMPLEMENTED");
			}
			
			@Override
			public void onFileChunkReceived(String arg0, String arg1, String arg2,
					String arg3, String arg4, String arg5, long arg6, long arg7) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener social : onFileChunkReceived NOT IMPLEMENTED");
			}
			
			@Override
			public void onDataReceived(String arg0, String arg1, String arg2,
					byte[][] arg3) {
				Log.d(TAG, TAGClass + " : " + "IChordChannelListener social : onDataReceived");
				// here can be received profiles, previews, etc., now we will consider only profile preview
				if (arg3.equals(LookAtMeMessageType.PREVIEW_REQUEST.name())) {
					// send my profile preview to arg0 node
					sendProfilePreviewResponse(arg0);
				}
				else if (arg3.equals(LookAtMeMessageType.PREVIEW.name())) {
					// get chord message from payload
					byte[] chordMessageByte = arg3[0];
					LookAtMeChordMessage chordMessage = LookAtMeChordMessage.obtainChordMessage(chordMessageByte, arg0);
					Profile profile = (Profile) chordMessage.getObject(LookAtMeMessage.PROFILE_KEY);
					LookAtMeNode node = new LookAtMeNode();
					node.setId(arg0);
					node.setProfile(profile);
					socialNodeMap.put(arg0, node);
					// TODO: notify for update GUI
				}
				
			}
		});
	}
	
	private int startChordCommunication() {
		Log.d(TAG, TAGClass + " : " + "startChordCommunication");
		availableWifiInterface = chord.getAvailableInterfaceTypes();
		if (availableWifiInterface == null || availableWifiInterface.size() == 0) {
			return LookAtMeErrorManager.ERROR_NO_INTERFACE_AVAILABLE;
		}
		if (availableWifiInterface.contains(ChordManager.INTERFACE_TYPE_WIFI)) {
			currentWifiInterface = ChordManager.INTERFACE_TYPE_WIFI;
		}
		else {
			currentWifiInterface = (availableWifiInterface.get(0)).intValue();
		}
		return chord.start(currentWifiInterface, new IChordManagerListener() {
			
			@Override
			public void onStarted(String arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onNetworkDisconnected() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private boolean sendProfilePreviewRequestAll() {
		Log.d(TAG, TAGClass + " : " + "sendProfilePreviewRequestAll");
		List<String> socialNodeList = socialChannel.getJoinedNodeList();
		for (String socialNodeId : socialNodeList) {
			if (!sendProfilePreviewRequest(socialNodeId)) {
				return false;
			}
		}
		return false;
	}
	
	private boolean sendProfilePreviewRequest(String nodeTo) {
		Log.d(TAG, TAGClass + " : " + "sendProfilePreviewRequest");
		return socialChannel.sendData(nodeTo, LookAtMeMessageType.PREVIEW_REQUEST.name(), null);
	}
	
	private boolean sendProfilePreviewResponse(String nodeTo) {
		Log.d(TAG, TAGClass + " : " + "sendProfilePreviewResponse");
		LookAtMeChordMessage message = new LookAtMeChordMessage(LookAtMeMessageType.PREVIEW);
		message.setSenderNodeName(chord.getName());
		message.setReceiverNodeName(nodeTo);
		message.putObject(LookAtMeMessage.PROFILE_KEY, null);//TODO: recuperare profile
		return socialChannel.sendData(nodeTo, LookAtMeMessageType.PREVIEW.name(), obtainPayload(message));
	}
	
	private byte[][] obtainPayload(LookAtMeChordMessage message) {
		byte[][] payload = new byte[1][1];
		payload[0] = message.getBytes();
		return payload;
	}

}
