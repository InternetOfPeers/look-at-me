package com.dreamteam.lookme.communication;

import com.dreamteam.lookme.chord.message.MessageType;

public interface ILookAtMeMessage {

	public abstract void putInt(String key, int value);

	public abstract void putString(String key, String value);

	public abstract void putObject(String key, Object value);

	public abstract int getInt(String key);

	public abstract String getString(String key);

	public abstract Object getObject(String key);

	public abstract MessageType getType();

	public abstract String getSenderNodeName();

	public abstract void setSenderNodeName(String senderNodeName);

	public abstract String getReceiverNodeName();

	public abstract void setReceiverNodeName(String receiverNodeName);

	/**
	 * Return message object as byte array
	 */
	public abstract byte[] getBytes();

}