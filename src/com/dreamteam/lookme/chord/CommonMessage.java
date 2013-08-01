package com.dreamteam.lookme.chord;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class CommonMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	private final MessageType type; /* Message type */
	private final Map<String, Object> payload; /* Data to transfer */
	private String senderNodeName; /* Name of sender node */
	private String receiverNodeName; /* Name of receiver node */

	public CommonMessage(MessageType type) {
		this.type = type;
		this.payload = new HashMap<String, Object>();
	}

	public void putInt(String key, int value) {
		payload.put(key, value);
	}

	public void putString(String key, String value) {
		payload.put(key, value);
	}

	public void putObject(String key, Object value) {
		payload.put(key, value);
	}

	public int getInt(String key) {
		return (Integer) payload.get(key);
	}

	public String getString(String key) {
		return (String) payload.get(key);
	}

	public Object getObject(String key) {
		return payload.get(key);
	}

	public MessageType getType() {
		return type;
	}

	public String getSenderNodeName() {
		return senderNodeName;
	}

	public void setSenderNodeName(String senderNodeName) {
		this.senderNodeName = senderNodeName;
	}

	public String getReceiverNodeName() {
		return receiverNodeName;
	}

	public void setReceiverNodeName(String receiverNodeName) {
		this.receiverNodeName = receiverNodeName;
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

}