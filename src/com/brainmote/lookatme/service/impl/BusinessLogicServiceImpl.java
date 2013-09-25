package com.brainmote.lookatme.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.ChatMessage;
import com.brainmote.lookatme.bean.Interest;
import com.brainmote.lookatme.chord.CommunicationManager;
import com.brainmote.lookatme.chord.CustomException;
import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.chord.impl.CommunicationListenerImpl;
import com.brainmote.lookatme.chord.impl.CommunicationManagerImpl;
import com.brainmote.lookatme.constants.AppSettings;
import com.brainmote.lookatme.fake.FakeUser;
import com.brainmote.lookatme.fake.FakeUserCarlo;
import com.brainmote.lookatme.fake.FakeUserGenericImpl;
import com.brainmote.lookatme.fake.FakeUserGiuseppe;
import com.brainmote.lookatme.fake.FakeUserRiccardo;
import com.brainmote.lookatme.fake.FakeUserStefano;
import com.brainmote.lookatme.service.BusinessLogicService;
import com.brainmote.lookatme.service.Event;
import com.brainmote.lookatme.service.EventType;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.Log;

public class BusinessLogicServiceImpl extends Service implements BusinessLogicService {

	private static final String SERVICE_PREFIX = "com.brainmote.lookatme.service.BusinessLogicServiceImpl.";
	private static final String SERVICE_START = SERVICE_PREFIX + "SERVICE_START";
	private static final String SERVICE_STOP = SERVICE_PREFIX + "SERVICE_STOP";

	private boolean isRunning;
	private CommunicationManager communicationManager;
	private Map<String, FakeUser> fakeUsers = new HashMap<String, FakeUser>();

	/**
	 * 
	 * @param context
	 */
	@Override
	public void start(Context context) {
		Log.i();
		context.startService(new Intent(SERVICE_START));
		isRunning = true;
		try {
			if (communicationManager == null) {
				communicationManager = new CommunicationManagerImpl(context, new CommunicationListenerImpl());
			}
			communicationManager.startCommunication();
		} catch (CustomException e) {
			e.printStackTrace();
		}
		// Verifico se è attiva l'opzione dei credits in app
		if (isCreditsInAppEnabled(context)) {
			addFakeUser(new FakeUserGiuseppe(context));
			addFakeUser(new FakeUserCarlo(context));
			addFakeUser(new FakeUserStefano(context));
			addFakeUser(new FakeUserRiccardo(context));
			// Se necessario creo ulteriori fake user
			if (AppSettings.needMoreFakeUsers) {
				for (int i = 0; i < AppSettings.moreFakeUsers; i++) {
					addFakeUser(new FakeUserGenericImpl(context));
				}
			}
		}

	}

	/**
	 * 
	 * @param fakeUser
	 */
	private void addFakeUser(FakeUser fakeUser) {
		fakeUsers.put(fakeUser.getNode().getId(), fakeUser);
		Services.currentState.putSocialNodeInMap(fakeUser.getNode());
		// Aggiungo una conversazione fittizia all'inizio
		if (Services.currentState.getMyBasicProfile() != null)
			storeConversation(fakeUser.getConversation(Services.currentState.getMyBasicProfile().getId()));
	}

	/**
	 * 
	 * @param context
	 */
	@Override
	public void stop(Context context) {
		Log.i();
		fakeUsers.clear();
		if (communicationManager != null)
			communicationManager.stopCommunication();
		context.stopService(new Intent(SERVICE_STOP));
		isRunning = false;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void onCreate() {
		Log.i();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i();
		isRunning = true;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.i();
		return null;
	}

	@Override
	public void onDestroy() {
		Log.i();
		isRunning = false;
		super.onDestroy();
	}

	/**
	 * Poichè il costruttore viene referenziato da Android (anche se non avviato
	 * direttamente) non è possibile impostare il costruttore come privato. Ad
	 * ogni modo, per accedere correttamente all'istanza dall'applicazione
	 * bisogna utilizzare la factory apposita.
	 */
	public BusinessLogicServiceImpl() {
	}

	public static class Factory {
		private static BusinessLogicServiceImpl instance;

		public static BusinessLogicServiceImpl getBusinessLogicService() {
			if (instance == null) {
				instance = new BusinessLogicServiceImpl();
				instance.onCreate();
			}
			return instance;
		}
	}

	@Override
	public void sendLike(String nodeId) {
		// Verifica che il nodo sia attivo e che non sia un fake user
		if (!isFakeUserNode(nodeId) && isNodeAlive(nodeId)) {
			communicationManager.sendLike(nodeId);
		}
		String profileId = Services.currentState.getSocialNodeMap().getProfileIdByNodeId(nodeId);
		Services.currentState.addILikeToSet(profileId);
	}

	@Override
	public void requestFullProfile(String toNodeId) {
		// Verifica che il nodo non sia un fake user
		if (!isFakeUserNode(toNodeId)) {
			communicationManager.requestFullProfile(toNodeId);
		}
	}

	@Override
	public void startChat(String withNodeId) {
		// Verifica che il nodo sia attivo e che non sia un fake user
		if (!isFakeUserNode(withNodeId) && isNodeAlive(withNodeId)) {
			communicationManager.startChat(withNodeId);
		}
	}

	@Override
	public void refreshSocialList() {
		communicationManager.requestAllProfiles();
	}

	@Override
	public void notifyMyProfileIsUpdated() {
		communicationManager.notifyMyProfileIsUpdated();
	}

	@Override
	public Set<Interest> getFullInterestList() {
		Set<Interest> interestList = new TreeSet<Interest>();
		interestList.add(new Interest(1, "INTEREST 1", false));
		interestList.add(new Interest(2, "INTEREST 2", false));
		interestList.add(new Interest(3, "INTEREST 3", false));
		interestList.add(new Interest(4, "INTEREST 4", false));
		interestList.add(new Interest(5, "INTEREST 5", false));
		return interestList;
	}

	@Override
	public void storeConversation(ChatConversation conversation) {
		Services.currentState.getConversationsStore().put(conversation.getId(), conversation);
	}

	@Override
	public FakeUser getFakeUser(String nodeId) {
		return fakeUsers.get(nodeId);
	}

	@Override
	public boolean isFakeUserNode(String nodeId) {
		return fakeUsers.containsKey(nodeId);
	}

	@Override
	public boolean isMyProfileComplete() {
		return Services.currentState.getMyBasicProfile() != null;
	}

	@Override
	public boolean isNodeAlive(String nodeId) {
		// Se l'utente è fake non è sicuramente alive
		if (isFakeUserNode(nodeId))
			return false;
		// Verifico se l'utente è attualmente alive
		return communicationManager != null ? communicationManager.isNodeAlive(nodeId) : false;
	}

	@Override
	public boolean sendChatMessage(ChatConversation conversation, String messageText) {
		// Verifico che alla conversazione corrisponda un nodo che conosco e che
		// sia inserito nella mappa
		String toNode = communicationManager.getNodeIdFromConversation(conversation);
		if (toNode == null)
			return false;
		// Verifico che il nodo sia tutt'ora attivo o che sia un fake
		if (!isNodeAlive(toNode) && !isFakeUserNode(toNode))
			return false;
		// Invio il messaggio se non è un utente fake. A differenza della
		// vecchia versione Chord, non ho
		// più un parametro di ritorno che mi conferma l'avvenuta ricezione.
		// TODO Da verificare se il metodo va in eccezione in caso di mancata
		// consegna oppure se proprio non si può sapere se un messaggio è stato
		// correttamente consegnato (sarebbe molto strano)
		if (!isFakeUserNode(toNode)) {
			communicationManager.sendChatMessage(conversation, messageText);
		}
		// Aggiorno la conversation e la memorizzo
		Services.businessLogic.storeConversation(conversation.addMessage(new ChatMessage(messageText, true)));
		return true;
	}

	@Override
	public String getProfileIdFromConversationId(String conversationId) {
		String[] nodes = conversationId.split("_");
		if (!Services.currentState.getMyBasicProfile().getId().equals(nodes[0]))
			return nodes[0];
		else
			return nodes[1];
	}

	@Override
	public void joinConversation(ChatConversation conversation) {
		communicationManager.checkAndJoinChatConversation(conversation);
	}

	@Override
	public boolean isCreditsInAppEnabled(Context context) {
		return context.getSharedPreferences(AppSettings.USER_PREFERENCES, MODE_PRIVATE).getBoolean(AppSettings.IN_APP_CREDITS, false);
	}

	@Override
	public void initFakeUsersConversations() {
		for (FakeUser fakeUser : fakeUsers.values()) {
			Services.businessLogic.storeConversation(fakeUser.getConversation(Services.currentState.getMyBasicProfile().getId()));
		}
	}

	@Override
	public void setResponseIfConversationIsFake(final ChatConversation conversation) {
		final String nodeId = communicationManager.getNodeIdFromConversation(conversation);
		if (isFakeUserNode(nodeId)) {
			// Mostra una risposta casuale dopo un tempo casuale
			new Handler().postDelayed(new Runnable() {
				public void run() {
					try {
						if (nodeId == null)
							return;
						FakeUser fakeUser = getFakeUser(nodeId);
						if (fakeUser == null || conversation == null)
							return;
						Node node = Services.currentState.getSocialNodeMap().findNodeByNodeId(nodeId);
						BasicProfile otherProfile = (BasicProfile) node.getProfile();
						String otherNickName = otherProfile.getNickname();
						String message = fakeUser.getNextAnswer();
						conversation.addMessage(new ChatMessage(message, false));
						Services.businessLogic.storeConversation(conversation);
						Services.notification.chatMessage(Services.currentState.getContext(), otherNickName, nodeId, message, conversation.getId());
						Services.event.post(new Event(EventType.CHAT_MESSAGE_RECEIVED, nodeId));
					} catch (Exception e) {
						// Essendo una facezia, qualunque eccezione si verifichi
						// ignoro quanto stavo tentando di fare :)
						Log.d("Facezia non riuscita: " + e.toString());
					}
				}
			}, AppSettings.FAKE_USER_CHAT_RESPONSE_TIME + new Random().nextInt(AppSettings.FAKE_USER_CHAT_RESPONSE_TIME_OFFSET + 1));
		}
	}

}
