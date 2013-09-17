package com.brainmote.lookatme.service.impl;

import java.util.Set;
import java.util.TreeSet;

import android.content.Context;

import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.FullProfile;
import com.brainmote.lookatme.bean.Statistics;
import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.db.DBOpenHelperImpl;
import com.brainmote.lookatme.service.ConversationStore;
import com.brainmote.lookatme.service.CurrentState;
import com.brainmote.lookatme.util.SocialNodeMap;

public class CurrentStateImpl implements CurrentState {

	private ConversationStore conversationsStore = new ConversationStoreImpl();

	private SocialNodeMap socialNodeMap = new SocialNodeMap();

	private Set<String> iLikeSet = new TreeSet<String>();

	private Node profileViewed;

	private Set<String> likedSet = new TreeSet<String>();

	private Context currentContext;

	private Set<Integer> interestSet = new TreeSet<Integer>();

	@Override
	public FullProfile getMyFullProfile() {
		return DBOpenHelperImpl.getInstance(getContext()).getMyFullProfile();
	}

	@Override
	public BasicProfile getMyBasicProfile() {
		// TODO getcontext() potrebbe tornare null (dopo un lungo inutilizzo?),
		// gestire la situazione di conseguenza
		return DBOpenHelperImpl.getInstance(getContext()).getMyBasicProfile();
	}

	@Override
	public ConversationStore getConversationsStore() {
		return conversationsStore;
	}

	@Override
	public SocialNodeMap getSocialNodeMap() {
		return socialNodeMap;
	}

	@Override
	public void putSocialNodeInMap(Node node) {
		socialNodeMap.put(node);
	}

	@Override
	public void removeSocialNodeFromMap(String nodeId) {
		socialNodeMap.remove(nodeId);
	}

	@Override
	public Set<String> getILikeSet() {
		return iLikeSet;
	}

	@Override
	public void addILikeToSet(String profileId) {
		iLikeSet.add(profileId);
	}

	@Override
	public void removeILikeFromSet(String profileId) {
		iLikeSet.remove(profileId);
	}

	@Override
	public Set<String> getLikedSet() {
		return likedSet;
	}

	@Override
	public void addLikedToSet(String profileId) {
		likedSet.add(profileId);
		// salvo nel DB
		DBOpenHelperImpl.getInstance(getContext()).addLike(profileId);
	}

	@Override
	public void removeLikedFromSet(String profileId) {
		likedSet.remove(profileId);
	}

	@Override
	public Node getProfileViewed() {
		return profileViewed;
	}

	@Override
	public void setProfileViewed(Node node) {
		profileViewed = node;
	}

	private CurrentStateImpl() {

	}

	public static class Factory {
		private static CurrentState instance;

		public static CurrentState getCurrentState() {
			return instance == null ? instance = new CurrentStateImpl() : instance;
		}
	}

	@Override
	public void setContext(Context context) {
		currentContext = context;
	}

	@Override
	public Context getContext() {
		return currentContext;
	}

	@Override
	public String getNickname(String nodeId) {
		return getSocialNodeMap().findNodeByNodeId(nodeId).getProfile().getNickname();
	}

	@Override
	public boolean checkLikeMatch(String profileId) {
		return getILikeSet().contains(profileId) && getLikedSet().contains(profileId);
	}

	@Override
	public void reset() {
		this.socialNodeMap = new SocialNodeMap();
		this.profileViewed = null;
		// this.iLikeSet = new TreeSet<String>();
		// this.likedSet = new TreeSet<String>();
		// this.interestList = new TreeSet<Interest>();
		// TODO: salvare le conversazioni appese?
	}

	@Override
	public Set<Integer> getInterestSet() {
		return interestSet;
	}

	@Override
	public void setInterestSet(Set<Integer> interestSet) {
		this.interestSet = interestSet;
	}
	
	@Override
	public void addInterestToSet(int interest) {
		this.interestSet.add(interest);
		System.out.println(interestSet);
	}
	
	@Override
	public void removeInterestFromSet(int interest) {
		this.interestSet.remove(interest);
		System.out.println(interestSet);
	}

	@Override
	public Statistics getStatistics() {
		return DBOpenHelperImpl.getInstance(getContext()).getStatistics();
	}

	@Override
	public void addVisitSet(String profileId) {
		// salvo nel DB
		DBOpenHelperImpl.getInstance(getContext()).addVisit(profileId);
	}

}
