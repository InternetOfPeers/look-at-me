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

public class ChordMessage implements Serializable {

	private static final long serialVersionUID = -2069620040307561844L;
	
	private final MessageType mType;			/* Message type 		 */
	private final Map<String, Object> mPayload;	/* Data to transfer		 */
	private String mSenderNodeName;				/* Name of sender node 	 */
	private String mReceiverNodeName;			/* Name of receiver node */
	
	private ChordMessage(MessageType type) {
		this.mType = type;
		mPayload = new HashMap<String, Object>();
	}
	
	public static ChordMessage obtainMessage(MessageType type) {
		return new ChordMessage(type);
	}

	public void putInt(String key, int value) {
		mPayload.put(key, value);
	}

	public void putString(String key, String value) {
		mPayload.put(key, value);
	}

	public void putObject(String key, Object value) {
		mPayload.put(key, value);
	}

	public int getInt(String key) {
		return (Integer) mPayload.get(key);
	}

	public String getString(String key) {
		return (String) mPayload.get(key);
	}

	public Object getObject(String key) {
		return mPayload.get(key);
	}

	public MessageType getType() {
		return mType;
	}
	
	public String getSenderNodeName() {
		return mSenderNodeName;
	}

	public void setSenderNodeName(String senderNodeName) {
		mSenderNodeName = senderNodeName;
	}
	
	public String getReceiverNodeName() {
		return mReceiverNodeName;
	}

	public void setReceiverNodeName(String receiverNodeName) {
		mReceiverNodeName = receiverNodeName;
	}
	
	/**
	 * Return message object as byte array
	 */
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
	public static ChordMessage obtainChordMessage(byte[] data, String senderNodeName) {
		final ByteArrayInputStream in = new ByteArrayInputStream(data);
		final ObjectInputStream is;
		ChordMessage message = null;

		try {
			is = new ObjectInputStream(in);
			message = (ChordMessage) is.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		message.mSenderNodeName = senderNodeName;
		return message;
	}

}
