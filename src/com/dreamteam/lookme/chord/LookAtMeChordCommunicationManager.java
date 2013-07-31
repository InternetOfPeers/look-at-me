/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.chord;

import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.Profile;
import com.dreamteam.lookme.communication.ILookAtMeCommunicationListener;
import com.dreamteam.lookme.communication.ILookAtMeCommunicationManager;
import com.dreamteam.lookme.communication.LookAtMeCommunicationRepository;
import com.dreamteam.lookme.communication.LookAtMeMessageType;
import com.dreamteam.lookme.communication.LookAtMeNode;
import com.dreamteam.lookme.constants.AppSettings;
import com.dreamteam.lookme.error.LookAtMeErrorManager;
import com.dreamteam.lookme.error.LookAtMeException;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.Log;
import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

public class LookAtMeChordCommunicationManager implements ILookAtMeCommunicationManager {

	public static final String chordFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChordTmp";

	private static final byte[][] EMPTY_PAYLOAD = new byte[0][0];

	private ChordManager chord;
	private IChordChannel publicChannel;
	private IChordChannel socialChannel;

	private List<Integer> availableWifiInterface;
	private int currentWifiInterface;

	private LookAtMeChordErrorManager errorManager;

	private ILookAtMeCommunicationListener communicationListener;

	private Context context;
	private Looper looper;

	public LookAtMeChordCommunicationManager(Context context, Looper looper, ILookAtMeCommunicationListener communicationListener) {
		Log.d();
		this.context = context;
		this.errorManager = new LookAtMeChordErrorManager();
		this.communicationListener = communicationListener;
	}

	@Override
	public void startCommunication() throws LookAtMeException {
		Log.d();
		chord = ChordManager.getInstance(context);
		// this.chord.setTempDirectory(chordFilePath);
		chord.setHandleEventLooper(looper);
		errorManager.checkError(startChord());
		// notify started communication
		communicationListener.onCommunicationStarted();
	}

	@Override
	public void stopCommunication() {
		Log.d();
		if (publicChannel != null) {
			chord.leaveChannel(ChordManager.PUBLIC_CHANNEL);
		}
		for (IChordChannel channel : chord.getJoinedChannelList()) {
			chord.leaveChannel(channel.getName());
		}
		chord.stop();
		// notify stopped communication
		communicationListener.onCommunicationStopped();
	}

	private IChordChannel joinPublicChannel() {
		Log.d();
		return chord.joinChannel(ChordManager.PUBLIC_CHANNEL, new IChordChannelListener() {

			@Override
			public void onNodeLeft(String arg0, String arg1) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onNodeJoined(String arg0, String arg1) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileWillReceive(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, long arg6) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileSent(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileReceived(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, long arg6, String arg7) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileFailed(String arg0, String arg1, String arg2, String arg3, String arg4, int arg5) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileChunkSent(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, long arg6, long arg7, long arg8) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileChunkReceived(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, long arg6, long arg7) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onDataReceived(String arg0, String arg1, String arg2, byte[][] arg3) {
				Log.d("NOT IMPLEMENTED");
			}
		});
	}

	private IChordChannel joinSocialChannel() {
		Log.d();
		return chord.joinChannel(AppSettings.SOCIAL_CHANNEL_NAME, new IChordChannelListener() {

			@Override
			public void onNodeLeft(String arg0, String arg1) {
				Log.d();
				communicationListener.onNodeLeft(arg0);
			}

			@Override
			public void onNodeJoined(String arg0, String arg1) {
				Log.d();
				// send a preview profile request silently
				sendBasicProfileRequest(arg0);
				// it will be notified after receive his profile
			}

			@Override
			public void onFileWillReceive(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, long arg6) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileSent(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileReceived(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, long arg6, String arg7) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileFailed(String arg0, String arg1, String arg2, String arg3, String arg4, int arg5) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileChunkSent(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, long arg6, long arg7, long arg8) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileChunkReceived(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, long arg6, long arg7) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onDataReceived(String arg0, String arg1, String arg2, byte[][] arg3) {
				Log.d();
				// here can be received profiles, previews, etc.
				LookAtMeMessageType messageType = LookAtMeMessageType.valueOf(arg2);
				byte[] chordMessageByte = arg3[0];
				LookAtMeChordMessage message = null;
				if (chordMessageByte != null && chordMessageByte.length > 0) {
					message = LookAtMeChordMessage.obtainChordMessage(chordMessageByte, arg0);
				}
				switch (messageType) {
				case BASIC_PROFILE_REQUEST:
					// send my basic profile to arg0 node
					sendBasicProfileResponse(arg0);
					break;
				case BASIC_PROFILE:
					BasicProfile basicProfile = (BasicProfile) message.getObject(LookAtMeMessageType.BASIC_PROFILE.toString());
					LookAtMeNode basicNode = new LookAtMeNode();
					basicNode.setId(arg0);
					basicNode.setProfile(basicProfile);
					communicationListener.onBasicProfileNodeReceived(basicNode);
					break;
				case FULL_PROFILE_REQUEST:
					// send my full profile to arg0 node
					sendFullProfileResponse(arg0);
					break;
				case FULL_PROFILE:
					FullProfile fullProfile = (FullProfile) message.getObject(LookAtMeMessageType.FULL_PROFILE.toString());
					LookAtMeNode fullNode = new LookAtMeNode();
					fullNode.setId(arg0);
					fullNode.setProfile(fullProfile);
					communicationListener.onFullProfileNodeReceived(fullNode);
					break;
				case PROFILE_UPDATE:
					// TODO
					// BasicProfile updatedProfile = (BasicProfile)
					// message.getObject(LookAtMeMessageType.PROFILE_UPDATE.toString());
					// LookAtMeNode updatedNode = new LookAtMeNode();
					// updatedNode.setId(arg0);
					// updatedNode.setProfile(updatedProfile);
					// communicationListener.onSocialNodeUpdated(updatedNode);
					break;
				case START_CHAT_MESSAGE:
					String chatChannelName = CommonUtils.generateChannelName(arg0, LookAtMeCommunicationRepository.getInstance().getMyBasicProfile().getId());
					joinChatChannel(chatChannelName);
					communicationListener.onStartChatMessageReceived(arg0, chatChannelName);
					break;
				case LIKE:
					communicationListener.onLikeReceived(arg0);
					break;
				default:
					break;
				}
			}
		});
	}

	private IChordChannel joinChatChannel(String channelName) {
		Log.d();
		return chord.joinChannel(channelName, new IChordChannelListener() {

			@Override
			public void onNodeLeft(String arg0, String arg1) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onNodeJoined(String arg0, String arg1) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileWillReceive(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, long arg6) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileSent(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileReceived(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, long arg6, String arg7) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileFailed(String arg0, String arg1, String arg2, String arg3, String arg4, int arg5) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileChunkSent(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, long arg6, long arg7, long arg8) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onFileChunkReceived(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, long arg6, long arg7) {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onDataReceived(String arg0, String arg1, String arg2, byte[][] arg3) {
				Log.d();
				// here can be received only chat messages
				LookAtMeMessageType messageType = LookAtMeMessageType.valueOf(arg2);
				switch (messageType) {
				case CHAT_MESSAGE:
					byte[] chordMessageByte = arg3[0];
					LookAtMeChordMessage message = null;
					if (chordMessageByte != null && chordMessageByte.length > 0) {
						message = LookAtMeChordMessage.obtainChordMessage(chordMessageByte, arg0);
					}
					String chatMessage = (String) message.getObject(LookAtMeMessageType.CHAT_MESSAGE.toString());
					communicationListener.onChatMessageReceived(arg0, chatMessage);
					break;
				default:
					break;
				}
			}
		});
	}

	private int startChord() {
		Log.d();
		// trying to use INTERFACE_TYPE_WIFI, otherwise get the first
		// available interface
		availableWifiInterface = chord.getAvailableInterfaceTypes();
		if (availableWifiInterface == null || availableWifiInterface.size() == 0) {
			return LookAtMeErrorManager.ERROR_NO_INTERFACE_AVAILABLE;
		}
		if (availableWifiInterface.contains(ChordManager.INTERFACE_TYPE_WIFI)) {
			currentWifiInterface = ChordManager.INTERFACE_TYPE_WIFI;
		} else {
			currentWifiInterface = (availableWifiInterface.get(0)).intValue();
		}
		Log.d("connecting with interface " + currentWifiInterface);
		return chord.start(currentWifiInterface, new IChordManagerListener() {

			@Override
			public void onStarted(String arg0, int arg1) {
				Log.d();
				publicChannel = joinPublicChannel();
				socialChannel = joinSocialChannel();
				Log.d("now chord is joined to " + chord.getJoinedChannelList().size() + " channels");
			}

			@Override
			public void onNetworkDisconnected() {
				Log.d("NOT IMPLEMENTED");
			}

			@Override
			public void onError(int arg0) {
				Log.d("NOT IMPLEMENTED");
			}
		});
	}

	@Override
	public boolean sendBasicProfileRequestAll() {
		Log.d();
		List<String> socialNodeList = socialChannel.getJoinedNodeList();
		Log.d("there are " + socialNodeList.size() + " nodes joined to social channel");
		return socialChannel.sendDataToAll(LookAtMeMessageType.BASIC_PROFILE_REQUEST.name(), EMPTY_PAYLOAD);
	}

	@Override
	public boolean sendBasicProfileAll() {
		// TODO
		Log.d();
		return false;
		// LookAtMeChordMessage message = obtainMyProfileMessage(BASIC_PROFILE,
		// LookAtMeMessageType.PROFILE_UPDATE, null);
		// if (message != null) {
		// return
		// socialChannel.sendDataToAll(LookAtMeMessageType.PROFILE_UPDATE.toString(),
		// obtainPayload(message));
		// } else {
		// return false;
		// }
	}

	private boolean sendBasicProfileRequest(String nodeTo) {
		Log.d();
		return socialChannel.sendData(nodeTo, LookAtMeMessageType.BASIC_PROFILE_REQUEST.name(), EMPTY_PAYLOAD);
	}

	private boolean sendBasicProfileResponse(String nodeTo) {
		Log.d();
		LookAtMeChordMessage message = obtainMyProfileMessage(LookAtMeCommunicationRepository.getInstance().getMyBasicProfile(), LookAtMeMessageType.BASIC_PROFILE, nodeTo);
		if (message != null) {
			return socialChannel.sendData(nodeTo, LookAtMeMessageType.BASIC_PROFILE.toString(), obtainPayload(message));
		} else {
			return false;
		}
	}

	public boolean sendFullProfileResponse(String nodeTo) {
		Log.d();
		LookAtMeChordMessage message = obtainMyProfileMessage(LookAtMeCommunicationRepository.getInstance().getMyFullProfile(), LookAtMeMessageType.FULL_PROFILE, nodeTo);
		if (message != null) {
			return socialChannel.sendData(nodeTo, LookAtMeMessageType.FULL_PROFILE.toString(), obtainPayload(message));
		} else {
			return false;
		}
	}

	private LookAtMeChordMessage obtainMyProfileMessage(Profile myProfile, LookAtMeMessageType type, String receiverNodeName) {
		Log.d();
		LookAtMeChordMessage message = new LookAtMeChordMessage(type);
		message.setSenderNodeName(chord.getName());
		message.setReceiverNodeName(receiverNodeName); // this maybe null
		message.putObject(type.toString(), myProfile);
		return message;
	}

	private byte[][] obtainPayload(LookAtMeChordMessage message) {
		byte[][] payload = new byte[1][1];
		payload[0] = message.getBytes();
		return payload;
	}

	@Override
	public boolean sendFullProfileRequest(String nodeTo) {
		Log.d();
		return socialChannel.sendData(nodeTo, LookAtMeMessageType.FULL_PROFILE_REQUEST.name(), EMPTY_PAYLOAD);
	}

	@Override
	public boolean sendLike(String nodeTo) {
		Log.d();
		return socialChannel.sendData(nodeTo, LookAtMeMessageType.LIKE.name(), EMPTY_PAYLOAD);
	}

	@Override
	public boolean sendStartChatMessage(String nodeTo) {
		Log.d();
		;
		return socialChannel.sendData(nodeTo, LookAtMeMessageType.START_CHAT_MESSAGE.toString(), EMPTY_PAYLOAD);
	}

	@Override
	public boolean sendChatMessage(String nodeTo, String message, String channelName) {
		Log.d();
		LookAtMeChordMessage chordMessage = new LookAtMeChordMessage(LookAtMeMessageType.CHAT_MESSAGE);
		chordMessage.setSenderNodeName(chord.getName());
		chordMessage.setReceiverNodeName(nodeTo);
		chordMessage.putString(LookAtMeMessageType.CHAT_MESSAGE.toString(), message);
		IChordChannel chatChannel = chord.getJoinedChannel(channelName);
		return chatChannel.sendData(nodeTo, LookAtMeMessageType.CHAT_MESSAGE.toString(), obtainPayload(chordMessage));
	}

}
