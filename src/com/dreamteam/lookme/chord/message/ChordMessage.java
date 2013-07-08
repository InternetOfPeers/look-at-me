/**
 * Author: Carlo Tassi
 * 
 * Message object over chord for Look@me App
 */
package com.dreamteam.lookme.chord.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.dreamteam.lookme.communication.ILookAtMeMessage;
import com.dreamteam.lookme.communication.LookAtMeMessageType;

public class ChordMessage implements Serializable, ILookAtMeMessage {

	private static final long serialVersionUID = -2069620040307561844L;

	private final LookAtMeMessageType type; /* Message type */
	private final Map<String, Object> payload; /* Data to transfer */
	private String senderNodeName; /* Name of sender node */
	private String receiverNodeName; /* Name of receiver node */

	private ChordMessage(LookAtMeMessageType type) {
		this.type = type;
		payload = new HashMap<String, Object>();
	}

	public static ILookAtMeMessage obtainMessage(LookAtMeMessageType type) {
		return new ChordMessage(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.chord.message.ILookAtMeMessage#putInt(java.lang.
	 * String, int)
	 */
	@Override
	public void putInt(String key, int value) {
		payload.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.chord.message.ILookAtMeMessage#putString(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public void putString(String key, String value) {
		payload.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.chord.message.ILookAtMeMessage#putObject(java.lang
	 * .String, java.lang.Object)
	 */
	@Override
	public void putObject(String key, Object value) {
		payload.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.chord.message.ILookAtMeMessage#getInt(java.lang.
	 * String)
	 */
	@Override
	public int getInt(String key) {
		return (Integer) payload.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.chord.message.ILookAtMeMessage#getString(java.lang
	 * .String)
	 */
	@Override
	public String getString(String key) {
		return (String) payload.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.chord.message.ILookAtMeMessage#getObject(java.lang
	 * .String)
	 */
	@Override
	public Object getObject(String key) {
		return payload.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#getType()
	 */
	@Override
	public LookAtMeMessageType getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.chord.message.ILookAtMeMessage#getSenderNodeName()
	 */
	@Override
	public String getSenderNodeName() {
		return senderNodeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.chord.message.ILookAtMeMessage#setSenderNodeName
	 * (java.lang.String)
	 */
	@Override
	public void setSenderNodeName(String senderNodeName) {
		this.senderNodeName = senderNodeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.chord.message.ILookAtMeMessage#getReceiverNodeName()
	 */
	@Override
	public String getReceiverNodeName() {
		return receiverNodeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.chord.message.ILookAtMeMessage#setReceiverNodeName
	 * (java.lang.String)
	 */
	@Override
	public void setReceiverNodeName(String receiverNodeName) {
		this.receiverNodeName = receiverNodeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#getBytes()
	 */
	@Override
	public byte[] getBytes() {
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		final ObjectOutputStream os;

		try {
			os = new ObjectOutputStream(out);
			os.writeObject(this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return out.toByteArray();
	}

	/**
	 * Recreates message object from the byte array
	 */
	public static ILookAtMeMessage obtainChordMessage(byte[] data,
			String senderNodeName) {
		final ByteArrayInputStream in = new ByteArrayInputStream(data);
		final ObjectInputStream is;
		ChordMessage message = null;

		try {
			is = new ObjectInputStream(in);
			message = (ChordMessage) is.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		message.senderNodeName = senderNodeName;
		return message;
	}

}
