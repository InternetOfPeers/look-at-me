package com.brainmote.lookatme.chord.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.chord.SocialNodeMap;
import com.brainmote.lookatme.util.Log;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class SocialNodeMapImpl implements SocialNodeMap {

	private Map<String, Node> nodeMap; // <nodeId, nodeObj>
	private BiMap<String, String> idMap; // <profileId, nodeId>

	public SocialNodeMapImpl() {
		nodeMap = new HashMap<String, Node>();
		idMap = HashBiMap.create();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.brainmote.lookatme.util.SocialNodeMap#put(com.brainmote.lookatme.
	 * chord.Node)
	 */
	@Override
	public void put(Node node) {
		if (!nodeMap.containsKey(node.getId()) && idMap.containsKey(node.getProfile().getId())) {
			Log.d("Il nodo con profile id " + node.getProfile().getId() + " ha cambiato il suo id!!!");
			// rimuovo il vecchio nodo prima di inserire nuovo
			Node removed = nodeMap.remove(idMap.get(node.getProfile().getId()));
			idMap.remove(removed.getProfile().getId());
		}
		nodeMap.put(node.getId(), node);
		idMap.put(node.getProfile().getId(), node.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.brainmote.lookatme.util.SocialNodeMap#remove(java.lang.String)
	 */
	@Override
	public void remove(String nodeId) {
		Node removed = nodeMap.remove(nodeId);
		if (removed != null)
			idMap.remove(removed.getProfile().getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.brainmote.lookatme.util.SocialNodeMap#remove(com.brainmote.lookatme
	 * .chord.Node)
	 */
	@Override
	public void remove(Node node) {
		if (node != null)
			remove(node.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.brainmote.lookatme.util.SocialNodeMap#size()
	 */
	@Override
	public int size() {
		return nodeMap.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.brainmote.lookatme.util.SocialNodeMap#containsNode(java.lang.String)
	 */
	@Override
	public boolean containsNode(String nodeId) {
		return nodeMap.containsKey(nodeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.brainmote.lookatme.util.SocialNodeMap#containsProfile(java.lang.String
	 * )
	 */
	@Override
	public boolean containsProfile(String profileId) {
		return idMap.containsKey(profileId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.brainmote.lookatme.util.SocialNodeMap#findNodeByNodeId(java.lang.
	 * String)
	 */
	@Override
	public Node findNodeByNodeId(String nodeId) {
		return nodeMap.get(nodeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.brainmote.lookatme.util.SocialNodeMap#findNodeByProfileId(java.lang
	 * .String)
	 */
	@Override
	public Node findNodeByProfileId(String profileId) {
		return nodeMap.get(idMap.get(profileId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.brainmote.lookatme.util.SocialNodeMap#getNodeIdByProfileId(java.lang
	 * .String)
	 */
	@Override
	public String getNodeIdByProfileId(String profileId) {
		return idMap.get(profileId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.brainmote.lookatme.util.SocialNodeMap#getProfileIdByNodeId(java.lang
	 * .String)
	 */
	@Override
	public String getProfileIdByNodeId(String nodeId) {
		return idMap.inverse().get(nodeId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.brainmote.lookatme.util.SocialNodeMap#getNodeList()
	 */
	@Override
	public List<Node> getNodeList() {
		return new ArrayList<Node>(nodeMap.values());
	}

}
