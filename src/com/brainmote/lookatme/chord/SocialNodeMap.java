package com.brainmote.lookatme.chord;

import java.util.List;

public interface SocialNodeMap {

	public abstract void put(Node node);

	public abstract void remove(String nodeId);

	public abstract void remove(Node node);

	public abstract int size();

	public abstract boolean containsNode(String nodeId);

	public abstract boolean containsProfile(String profileId);

	public abstract Node findNodeByNodeId(String nodeId);

	public abstract Node findNodeByProfileId(String profileId);

	public abstract String getNodeIdByProfileId(String profileId);

	public abstract String getProfileIdByNodeId(String nodeId);

	public abstract List<Node> getNodeList();

}