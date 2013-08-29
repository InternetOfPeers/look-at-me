package com.brainmote.lookatme.chord.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.ChatConversationImpl;
import com.brainmote.lookatme.bean.FullProfile;
import com.brainmote.lookatme.bean.Profile;
import com.brainmote.lookatme.chord.ChordErrorManager;
import com.brainmote.lookatme.chord.CommunicationListener;
import com.brainmote.lookatme.chord.CommunicationManager;
import com.brainmote.lookatme.chord.CustomException;
import com.brainmote.lookatme.chord.ErrorManager;
import com.brainmote.lookatme.chord.Message;
import com.brainmote.lookatme.chord.MessageType;
import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.constants.AppSettings;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.CommonUtils;
import com.brainmote.lookatme.util.Log;
import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;
import com.samsung.chord.IChordChannelListener;
import com.samsung.chord.IChordManagerListener;

public class CommunicationManagerImpl implements CommunicationManager {

	public static final String chordFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChordTmp";

	private static final byte[][] EMPTY_PAYLOAD = new byte[0][0];

	private ChordManager chord;
	private IChordChannel socialChannel;

	private List<Integer> availableWifiInterface;
	private int currentWifiInterface;

	private ChordErrorManager errorManager;

	private CommunicationListener communicationListener;

	private Context context;
	private Looper looper;

	public CommunicationManagerImpl(Context context, CommunicationListener communicationListener) {
		Log.d();
		this.context = context;
		this.errorManager = new ChordErrorManager();
		this.communicationListener = communicationListener;
	}

	@Override
	public void startCommunication() throws CustomException {
		Log.d();
		chord = ChordManager.getInstance(context);
		// this.chord.setTempDirectory(chordFilePath);
		chord.setHandleEventLooper(looper);
		errorManager.checkError(startChord());
	}

	@Override
	public void stopCommunication() {
		Log.d();
		// Se chiudo i canali direttamente va in concurrent modification
		// exception per cui mi serve un astruttura di appoggio da cui prendere
		// i nomi dei canali
		List<String> joinedChannelName = new ArrayList<String>();
		for (IChordChannel channel : chord.getJoinedChannelList()) {
			joinedChannelName.add(channel.getName());
		}
		// Da capire perchè ritorna sempre il messaggio:
		// "can't find channel (com.brainmote.lookatme.SOCIAL_CHANNEL)"
		for (String channelName : joinedChannelName) {
			Log.d("leaving channel " + channelName);
			chord.leaveChannel(channelName);
		}
		chord.stop();
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
				Log.d("joinPublicChannel - NOT IMPLEMENTED");
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
				Log.d("joinPublicChannel - NOT IMPLEMENTED");
			}
		});
	}

	private IChordChannel joinSocialChannel() {
		Log.d();
		return chord.joinChannel(AppSettings.SOCIAL_CHANNEL_NAME, new IChordChannelListener() {

			@Override
			public void onNodeJoined(String nodeId, String arg1) {
				Log.i("Il nodo che è appena apparso: " + nodeId + " - " + arg1);
				communicationListener.onNodeJoined(nodeId);
			}

			@Override
			public void onNodeLeft(String nodeId, String arg1) {
				Log.i("Il nodo che è appena scomparso: " + nodeId + " - " + arg1);
				communicationListener.onNodeLeft(nodeId);
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
			public void onDataReceived(String senderNodeId, String arg1, String arg2, byte[][] arg3) {
				Log.d("joinSocialChannel -" + arg1 + " - " + arg2);
				// here can be received profiles, previews, etc.
				MessageType messageType = MessageType.valueOf(arg2);
				byte[] chordMessageByte = arg3[0];
				Message message = null;
				if (chordMessageByte != null && chordMessageByte.length > 0) {
					message = Message.obtainChordMessage(chordMessageByte, senderNodeId);
				}
				switch (messageType) {
				case BASIC_PROFILE_REQUEST:
					Log.d("Qualcuno ha chiesto il mio profilo...");
					// Mando il mio profilo solamente se già completo
					if (Services.currentState.getMyBasicProfile() != null) {
						sendBasicProfileResponse(senderNodeId);
					}
					break;
				case BASIC_PROFILE:
					Log.d("Ho ricevuto un basic profile dal nodo " + senderNodeId);
					BasicProfile basicProfile = (BasicProfile) message.getObject(MessageType.BASIC_PROFILE.toString());
					Node basicNode = new Node();
					basicNode.setId(senderNodeId);
					basicNode.setProfile(basicProfile);
					communicationListener.onBasicProfileNodeReceived(basicNode);
					break;
				case FULL_PROFILE_REQUEST:
					// send my full profile to arg0 node
					sendFullProfileResponse(senderNodeId);
					Services.currentState.addVisitSet(Services.currentState.getSocialNodeMap().getProfileIdByNodeId(senderNodeId));
					break;
				case FULL_PROFILE:
					FullProfile fullProfile = (FullProfile) message.getObject(MessageType.FULL_PROFILE.toString());
					Node fullNode = new Node();
					fullNode.setId(senderNodeId);
					fullNode.setProfile(fullProfile);
					communicationListener.onFullProfileNodeReceived(fullNode);
					break;
				case PROFILE_UPDATE:
					Log.d("Ho ricevuto l'aggiornamento di un profilo");
					BasicProfile updatedProfile = (BasicProfile) message.getObject(MessageType.PROFILE_UPDATE.toString());
					Node updatedNode = new Node();
					updatedNode.setId(senderNodeId);
					updatedNode.setProfile(updatedProfile);
					communicationListener.onBasicProfileNodeReceived(updatedNode);
					break;
				case START_CHAT_MESSAGE:
					Log.d("Ricevuta richiesta di start chat");
					String myId = Services.currentState.getMyBasicProfile().getId();
					if (Services.currentState.getSocialNodeMap().containsNode(senderNodeId)) {
						String profileId = Services.currentState.getSocialNodeMap().getProfileIdByNodeId(senderNodeId);
						String chatChannelName = CommonUtils.getConversationId(myId, profileId);
						joinChatChannel(chatChannelName);
					} else {
						Log.d("PROFILO DI DESTINAZIONE NON PRESENTE IN TABELLA");
					}
					break;
				case LIKE:
					Log.d("Ho ricevuto un like");
					communicationListener.onLikeReceived(senderNodeId);
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
				Log.d("joinChatChannel - NOT IMPLEMENTED");
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
			public void onDataReceived(String fromNodeId, String arg1, String arg2, byte[][] arg3) {
				Log.d("joinChatChannel");
				// here can be received only chat messages
				MessageType messageType = MessageType.valueOf(arg2);
				switch (messageType) {
				case CHAT_MESSAGE:
					byte[] chordMessageByte = arg3[0];
					Message message = null;
					if (chordMessageByte != null && chordMessageByte.length > 0) {
						message = Message.obtainChordMessage(chordMessageByte, fromNodeId);
					}
					String chatMessage = (String) message.getObject(MessageType.CHAT_MESSAGE.toString());
					communicationListener.onChatMessageReceived(fromNodeId, chatMessage);
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
			return ErrorManager.ERROR_NO_INTERFACE_AVAILABLE;
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
				joinPublicChannel();
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
	public void requestAllProfiles() {
		if (socialChannel != null) {
			socialChannel.sendDataToAll(MessageType.BASIC_PROFILE_REQUEST.name(), EMPTY_PAYLOAD);
		}
	}

	@Override
	public boolean notifyMyProfileIsUpdated() {
		Message message = obtainMyProfileMessage(Services.currentState.getMyBasicProfile(), MessageType.PROFILE_UPDATE, null);
		if (message != null && socialChannel != null) {
			return socialChannel.sendDataToAll(MessageType.PROFILE_UPDATE.toString(), obtainPayload(message));
		} else {
			return false;
		}
	}

	private boolean sendBasicProfileResponse(String nodeTo) {
		Message message = obtainMyProfileMessage(Services.currentState.getMyBasicProfile(), MessageType.BASIC_PROFILE, nodeTo);
		if (message != null) {
			return socialChannel.sendData(nodeTo, MessageType.BASIC_PROFILE.toString(), obtainPayload(message));
		} else {
			return false;
		}
	}

	private boolean sendFullProfileResponse(String nodeTo) {
		Message message = obtainMyProfileMessage(Services.currentState.getMyFullProfile(), MessageType.FULL_PROFILE, nodeTo);
		if (message != null) {
			return socialChannel.sendData(nodeTo, MessageType.FULL_PROFILE.toString(), obtainPayload(message));
		} else {
			return false;
		}
	}

	private Message obtainMyProfileMessage(Profile myProfile, MessageType type, String receiverNodeName) {
		Message message = new Message(type);
		message.setSenderNodeName(chord.getName());
		message.setReceiverNodeName(receiverNodeName); // this maybe null
		message.putObject(type.toString(), myProfile);
		return message;
	}

	private byte[][] obtainPayload(Message message) {
		byte[][] payload = new byte[1][1];
		payload[0] = message.getBytes();
		return payload;
	}

	@Override
	public boolean requestFullProfile(String nodeTo) {
		return socialChannel.sendData(nodeTo, MessageType.FULL_PROFILE_REQUEST.name(), EMPTY_PAYLOAD);
	}

	@Override
	public boolean sendLike(String nodeTo) {
		return socialChannel.sendData(nodeTo, MessageType.LIKE.name(), EMPTY_PAYLOAD);
	}

	@Override
	public boolean startChat(String withNode) {
		String myProfileId = Services.currentState.getMyBasicProfile().getId();
		BasicProfile otherProfile = (BasicProfile) Services.currentState.getSocialNodeMap().findNodeByNodeId(withNode).getProfile();
		String otherProfileId = otherProfile.getId();
		String conversationId = CommonUtils.getConversationId(myProfileId, otherProfileId);
		ChatConversation conversation = Services.currentState.getConversationsStore().get(conversationId);
		// Se una conversazione con lo stesso id non è mai stata iniziata, la
		// crea
		if (conversation == null) {
			conversationId = CommonUtils.getConversationId(myProfileId, otherProfileId);
			Services.businessLogic.storeConversation(new ChatConversationImpl(conversationId, otherProfile));
		}
		// Effettua il join al canale prescelto per la chat
		IChordChannel chatChannel = chord.getJoinedChannel(conversationId);
		if (chatChannel == null) {
			Log.d("il device non era in join del chat channel " + conversationId + ", riprovo ora.");
			chatChannel = joinChatChannel(conversationId);
		}
		// Manda all'utente un messaggio affinchè si agganci automaticamente
		// al canale per chattare
		return socialChannel.sendData(withNode, MessageType.START_CHAT_MESSAGE.toString(), EMPTY_PAYLOAD);
	}

	@Override
	public boolean sendChatMessage(ChatConversation conversation, String text) {
		// Verifico lo stato della conversazione corrente
		if (!checkAndJoinChatConversation(conversation)) {
			return false;
		}
		IChordChannel privateChannel = chord.getJoinedChannel(conversation.getId());
		String toNodeId = getNodeIdFromConversation(conversation);
		// Preparo il messaggio chord da inviare
		Message chordMessage = new Message(MessageType.CHAT_MESSAGE);
		chordMessage.setSenderNodeName(chord.getName());
		chordMessage.setReceiverNodeName(toNodeId);
		chordMessage.putString(MessageType.CHAT_MESSAGE.toString(), text);
		return privateChannel.sendData(toNodeId, MessageType.CHAT_MESSAGE.toString(), obtainPayload(chordMessage));
	}

	@Override
	public boolean checkAndJoinChatConversation(ChatConversation conversation) {
		boolean chatConversationReady = true;
		String toNodeId = getNodeIdFromConversation(conversation);
		// Verifico che l'utente con il quale voglio parlare esista
		if (isNodeAlive(toNodeId)) {
			// Verifico di essere connesso al canale privato della chat
			IChordChannel privateChannel = chord.getJoinedChannel(conversation.getId());
			if (privateChannel == null) {
				Log.w("Non sono in join nel canale privato " + conversation.getId() + ", rieseguo una richiesta di chat.");
				startChat(toNodeId);
				chatConversationReady = false;
			}
			// Se esiste verifico che sia correttamente in join sul canale
			if (!getActiveNodeListInChannel(conversation.getId()).contains(toNodeId)) {
				Log.w("Il nodo " + toNodeId + " con cui voglio parlare non è connesso al canale privato " + conversation.getId() + ", mando un invito al join");
				startChat(toNodeId);
				chatConversationReady = false;
			}
		}
		// Restituisce lo stato della conversazione
		return chatConversationReady;
	}

	@Override
	public String getNodeIdFromConversation(ChatConversation conversation) {
		return Services.currentState.getSocialNodeMap().getNodeIdByProfileId(Services.businessLogic.getProfileIdFromConversationId(conversation.getId()));
	}

	@Override
	public boolean isNodeAlive(String nodeId) {
		return getActiveNodeList().contains(nodeId);
	}

	@Override
	public List<String> getActiveNodeListInChannel(String channelId) {
		IChordChannel channel = chord.getJoinedChannel(channelId);
		if (channel == null)
			return new ArrayList<String>();
		return channel.getJoinedNodeList();
	}

	@Override
	public List<String> getActiveNodeList() {
		return getActiveNodeListInChannel(AppSettings.SOCIAL_CHANNEL_NAME);
	}

}
