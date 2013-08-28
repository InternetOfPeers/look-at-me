package com.brainmote.lookatme.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.service.Services;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class SocialNodeMap {
	
	private Map<String, Node> nodeMap; // <nodeId, nodeObj>
	private BiMap<String, String> idMap; // <profileId, nodeId>
	
	public SocialNodeMap() {
		nodeMap = new HashMap<String, Node>();
		idMap = HashBiMap.create();
	}
	
	public void put(Node node) {
		if (!nodeMap.containsKey(node.getId()) && idMap.containsKey(node.getProfile().getId())) {
			Log.d("Il nodo con profile id " + node.getProfile().getId() + " ha cambiato il suo id!!!");
			// rimuovo il vecchio nodo prima di inserire nuovo
			Node removed = nodeMap.remove(idMap.get(node.getProfile().getId()));
			idMap.remove(removed.getProfile().getId());
			// aggiorno le strutture like e liked con il nuovo id
			if (Services.currentState.getILikeSet().contains(removed.getId())) {
				Services.currentState.removeILikeFromSet(removed.getId());
				Services.currentState.addILikeToSet(node.getId());
			}
			if (Services.currentState.getLikedSet().contains(removed.getId())) {
				Services.currentState.removeLikedFromSet(removed.getId());
				Services.currentState.addLikedToSet(node.getId());
			}
		}
		nodeMap.put(node.getId(), node);
		idMap.put(node.getProfile().getId(), node.getId());
	}
	
	public void remove(String nodeId) {
		Node removed = nodeMap.remove(nodeId);
		idMap.remove(removed.getProfile().getId());
	}
	
	public int size() {
		return nodeMap.size();
	}
	
	public boolean containsNode(String nodeId) {
		return nodeMap.containsKey(nodeId);
	}
	
	public boolean containsProfile(String profileId) {
		return idMap.containsKey(profileId);
	}
	
	public Node findNodeByNodeId(String nodeId) {
		return nodeMap.get(nodeId);
	}
	
	public Node findNodeByProfileId(String profileId) {
		return nodeMap.get(idMap.get(profileId));
	}
	
	public String getNodeIdByProfileId(String profileId) {
		return idMap.get(profileId);
	}
	
	public String getProfileIdByNodeId(String nodeId) {
		return idMap.inverse().get(nodeId);
	}
	
	public List<Node> getNodeList() {
		return new ArrayList<Node>(nodeMap.values());
	}

}
