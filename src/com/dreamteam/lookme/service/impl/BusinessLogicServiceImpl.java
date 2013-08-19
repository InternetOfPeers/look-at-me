package com.dreamteam.lookme.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.dreamteam.lookme.ChatConversation;
import com.dreamteam.lookme.bean.Interest;
import com.dreamteam.lookme.chord.CommunicationManager;
import com.dreamteam.lookme.chord.CustomException;
import com.dreamteam.lookme.chord.impl.CommunicationListenerImpl;
import com.dreamteam.lookme.chord.impl.CommunicationManagerImpl;
import com.dreamteam.lookme.constants.AppSettings;
import com.dreamteam.lookme.service.BusinessLogicService;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.FakeUser;
import com.dreamteam.util.FakeUserImpl;
import com.dreamteam.util.Log;

public class BusinessLogicServiceImpl extends Service implements BusinessLogicService {

	private static final String SERVICE_PREFIX = "com.dreamteam.lookme.service.BusinessLogicOperationsImpl.";
	private static final String SERVICE_START = SERVICE_PREFIX + "SERVICE_START";
	private static final String SERVICE_STOP = SERVICE_PREFIX + "SERVICE_STOP";

	private boolean isRunning;
	private CommunicationManager communicationManager;
	private FakeUser fakeUser;
	private Set<String> fakeUserNodeList = new HashSet<String>();

	/**
	 * 
	 * @param context
	 */
	@Override
	public void start(Context context) {
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
		// Se necessario creo un fake user
		if (AppSettings.fakeUsersEnabled) {
			for (int i = 0; i < AppSettings.fakeUsers; i++) {
				// Creo un fakeuser
				fakeUser = new FakeUserImpl(context);
				// Aggiungo un nodo per l'utente fittizio
				fakeUserNodeList.add(fakeUser.getNode().getId());
				Services.currentState.putSocialNodeInMap(fakeUser.getNode());
				// Aggiungo una conversazione fittizia all'inizio
				if (Services.currentState.getMyBasicProfile() != null)
					storeConversation(fakeUser.getConversation(Services.currentState.getMyBasicProfile().getId()));
			}
		}
	}

	/**
	 * 
	 * @param context
	 */
	@Override
	public void stop(Context context) {
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
		Log.d();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d();
		isRunning = true;
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d();
		return null;
	}

	@Override
	public void onDestroy() {
		Log.d();
		isRunning = false;
		super.onDestroy();
	}

	/**
	 * Poich� il costruttore viene referenziato da Android (anche se non
	 * avviato direttamente) non � possibile impostare il costruttore come
	 * privato. Ad ogni modo, per accedere correttamente all'istanza
	 * dall'applicazione bisogna utilizzare la factory apposita.
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
		Log.d();
		if (communicationManager.sendLike(nodeId)) {
			Services.currentState.addILikeToSet(nodeId);
		}
	}

	@Override
	public void requestFullProfile(String toNodeId) {
		communicationManager.requestFullProfile(toNodeId);
	}

	@Override
	public boolean startChat(String withNodeId) {
		return communicationManager.startChat(withNodeId);
	}

	@Override
	public void refreshSocialList() {
		communicationManager.requestAllProfiles();
	}

	@Override
	public boolean sendChatMessage(String toNode, String message) {
		return communicationManager.sendChatMessage(toNode, message);
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
	public FakeUser getFakeUser() {
		return fakeUser;
	}

	@Override
	public boolean isFakeUserNode(String nodeId) {
		return fakeUserNodeList.contains(nodeId);
	}

}
