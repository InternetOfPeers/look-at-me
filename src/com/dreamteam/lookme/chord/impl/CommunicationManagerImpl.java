package com.dreamteam.lookme.chord.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;

import com.dreamteam.lookme.ChatConversation;
import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.ChatConversationImpl;
import com.dreamteam.lookme.bean.ChatMessage;
import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.Profile;
import com.dreamteam.lookme.chord.ChordErrorManager;
import com.dreamteam.lookme.chord.CommunicationListener;
import com.dreamteam.lookme.chord.CommunicationManager;
import com.dreamteam.lookme.chord.CustomException;
import com.dreamteam.lookme.chord.ErrorManager;
import com.dreamteam.lookme.chord.Message;
import com.dreamteam.lookme.chord.MessageType;
import com.dreamteam.lookme.chord.Node;
import com.dreamteam.lookme.constants.AppSettings;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.CommonUtils;
import com.dreamteam.util.Log;
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
		// notify started communication
		communicationListener.onCommunicationStarted();
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
		// "can't find channel (com.dreamteam.lookme.SOCIAL_CHANNEL)"
		for (String channelName : joinedChannelName) {
			Log.d("leaving channel " + channelName);
			chord.leaveChannel(channelName);
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
			public void onNodeLeft(String nodeId, String arg1) {
				Log.d();
				communicationListener.onNodeLeft(nodeId);
			}

			@Override
			public void onNodeJoined(String arg0, String arg1) {
				Log.d("joinSocialChannel");
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
				Log.d("joinSocialChannel");
				// here can be received profiles, previews, etc.
				MessageType messageType = MessageType.valueOf(arg2);
				byte[] chordMessageByte = arg3[0];
				Message message = null;
				if (chordMessageByte != null && chordMessageByte.length > 0) {
					message = Message.obtainChordMessage(chordMessageByte, arg0);
				}
				switch (messageType) {
				case BASIC_PROFILE_REQUEST:
					// send my basic profile to arg0 node only if exists
					if (Services.currentState.getMyBasicProfile() != null) {
						sendBasicProfileResponse(arg0);
					}
					break;
				case BASIC_PROFILE:
					BasicProfile basicProfile = (BasicProfile) message.getObject(MessageType.BASIC_PROFILE.toString());
					Node basicNode = new Node();
					basicNode.setId(arg0);
					basicNode.setProfile(basicProfile);
					communicationListener.onBasicProfileNodeReceived(basicNode);
					break;
				case FULL_PROFILE_REQUEST:
					// send my full profile to arg0 node
					sendFullProfileResponse(arg0);
					break;
				case FULL_PROFILE:
					FullProfile fullProfile = (FullProfile) message.getObject(MessageType.FULL_PROFILE.toString());
					Node fullNode = new Node();
					fullNode.setId(arg0);
					fullNode.setProfile(fullProfile);
					communicationListener.onFullProfileNodeReceived(fullNode);
					break;
				case PROFILE_UPDATE:
					BasicProfile updatedProfile = (BasicProfile) message.getObject(MessageType.PROFILE_UPDATE.toString());
					Node updatedNode = new Node();
					updatedNode.setId(arg0);
					updatedNode.setProfile(updatedProfile);
					communicationListener.onBasicProfileNodeReceived(updatedNode);
					break;
				case START_CHAT_MESSAGE:
					String myId = Services.currentState.getMyBasicProfile().getId();
					Node nodeTo = Services.currentState.getSocialNodeMap().get(arg0);
					if (nodeTo != null) {
						String profileId = Services.currentState.getSocialNodeMap().get(arg0).getProfile().getId();
						String chatChannelName = CommonUtils.getConversationId(myId, profileId);
						joinChatChannel(chatChannelName);
					} else {
						android.util.Log.d("START CHAT MESSAGE", "PROFILO DI DESTINAZIONE NON PRESENTE IN TABELLA");
					}

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
			public void onDataReceived(String fromNode, String arg1, String arg2, byte[][] arg3) {
				Log.d("joinChatChannel");
				// here can be received only chat messages
				MessageType messageType = MessageType.valueOf(arg2);
				switch (messageType) {
				case CHAT_MESSAGE:
					byte[] chordMessageByte = arg3[0];
					Message message = null;
					if (chordMessageByte != null && chordMessageByte.length > 0) {
						message = Message.obtainChordMessage(chordMessageByte, fromNode);
					}
					String chatMessage = (String) message.getObject(MessageType.CHAT_MESSAGE.toString());
					communicationListener.onChatMessageReceived(fromNode, chatMessage);
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
	public boolean requestAllProfiles() {
		Log.d();
		if (isSocialChannelReady()) {
			List<String> socialNodeList = socialChannel.getJoinedNodeList();
			Log.d("there are " + socialNodeList.size() + " nodes joined to social channel");
			return socialChannel.sendDataToAll(MessageType.BASIC_PROFILE_REQUEST.name(), EMPTY_PAYLOAD);
		} else
			return false;
	}

	private boolean isSocialChannelReady() {
		Log.d();
		return socialChannel != null;
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

	private boolean sendBasicProfileRequest(String nodeTo) {
		return socialChannel.sendData(nodeTo, MessageType.BASIC_PROFILE_REQUEST.name(), EMPTY_PAYLOAD);
	}

	private boolean sendBasicProfileResponse(String nodeTo) {
		Message message = obtainMyProfileMessage(Services.currentState.getMyBasicProfile(), MessageType.BASIC_PROFILE, nodeTo);
		if (message != null) {
			return socialChannel.sendData(nodeTo, MessageType.BASIC_PROFILE.toString(), obtainPayload(message));
		} else {
			return false;
		}
	}

	@Override
	public boolean sendFullProfileResponse(String nodeTo) {
		Message message = obtainMyProfileMessage(Services.currentState.getMyFullProfile(), MessageType.FULL_PROFILE, nodeTo);
		if (message != null) {
			return socialChannel.sendData(nodeTo, MessageType.FULL_PROFILE.toString(), obtainPayload(message));
		} else {
			return false;
		}
	}

	private Message obtainMyProfileMessage(Profile myProfile, MessageType type, String receiverNodeName) {
		Log.d();
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
	public boolean startChat(String toNode) {
		Log.d();
		String myProfileId = Services.currentState.getMyBasicProfile().getId();
		BasicProfile otherProfile = (BasicProfile) Services.currentState.getSocialNodeMap().get(toNode).getProfile();
		String otherProfileId = otherProfile.getId();
		String conversationId = CommonUtils.getConversationId(myProfileId, otherProfileId);
		ChatConversation conversation = Services.currentState.getConversationsStore().get(conversationId);
		// Se una conversazione con lo stesso id non è mai stata iniziata, la
		// crea
		if (conversation == null) {
			conversationId = CommonUtils.getConversationId(myProfileId, otherProfileId);
			Services.businessLogic.storeConversation(new ChatConversationImpl(conversationId, otherProfile.getNickname(), otherProfile.getAge(), toNode, otherProfile
					.getMainProfileImage().getImageBitmap()));
		}
		// Effettua il join al canale prescelto per la chat
		IChordChannel chatChannel = chord.getJoinedChannel(conversationId);
		if (chatChannel == null) {
			Log.d("il device non era in join del chat channel " + conversationId + ", riprovo ora.");
			chatChannel = joinChatChannel(conversationId);
		}
		// Manda all'utente un messaggio affinché si agganci automaticamente al
		// canale per chattare
		return socialChannel.sendData(toNode, MessageType.START_CHAT_MESSAGE.toString(), EMPTY_PAYLOAD);
	}

	@Override
	public boolean sendChatMessage(String toNode, String text) {
		Log.d();
		// Preparo la conversation ed il messaggio
		Profile myProfile = Services.currentState.getMyBasicProfile();

		// TODO QUI VA IN NULLPOINTER EXCEPTION DOPO UN TEMPO DI INUTILIZZO
		BasicProfile otherProfile = (BasicProfile) Services.currentState.getSocialNodeMap().get(toNode).getProfile();

		String conversationId = CommonUtils.getConversationId(myProfile.getId(), otherProfile.getId());
		IChordChannel chatChannel = chord.getJoinedChannel(conversationId);
		// Per sicurezza effetto questo controllo. ATTENZIONE: se entra nell'if
		// significa che il join viene fatto in questo momento, e che quindi il
		// messaggio che stiamo mandando proprio ora non verrà ricevuto perché
		// chord non fa in tempo a recepire i nodi presenti nel channel, e
		// quindi considera inesistente il nodo al quale stiamo mandando questo
		// messaggio. Serve quindi trovare un'altro modo per gestire questa
		// eventualità
		if (chatChannel == null) {
			Log.d("ATTENZIONE! ATTENZIONE! Il device non era in join del chat channel " + conversationId + ", riprovo ora ma il nodo non riceverà il messaggio attuale: \""
					+ text + "\"");
			chatChannel = joinChatChannel(conversationId);
		}
		ChatConversation conversation = Services.currentState.getConversationsStore().get(conversationId);
		conversation.addMessage(new ChatMessage(myProfile.getNickname(), otherProfile.getNickname(), text, true));
		// Memorizzo la conversation ed invio il messaggio
		Services.businessLogic.storeConversation(conversation);
		// Preparo il messaggio chord
		Message chordMessage = new Message(MessageType.CHAT_MESSAGE);
		chordMessage.setSenderNodeName(chord.getName());
		chordMessage.setReceiverNodeName(toNode);
		chordMessage.putString(MessageType.CHAT_MESSAGE.toString(), text);
		return chatChannel.sendData(toNode, MessageType.CHAT_MESSAGE.toString(), obtainPayload(chordMessage));

	}

}
