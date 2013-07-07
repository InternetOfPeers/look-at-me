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

public class ChordMessage implements Serializable, ILookAtMeMessage {

	private static final long serialVersionUID = -2069620040307561844L;
	
	private final MessageType mType;			/* Message type 		 */
	private final Map<String, Object> mPayload;	/* Data to transfer		 */
	private String mSenderNodeName;				/* Name of sender node 	 */
	private String mReceiverNodeName;			/* Name of receiver node */
	
	private ChordMessage(MessageType type) {
		this.mType = type;
		mPayload = new HashMap<String, Object>();
	}
	
	public static ILookAtMeMessage obtainMessage(MessageType type) {
		return new ChordMessage(type);
	}

	/* (non-Javadoc)
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#putInt(java.lang.String, int)
	 */
	@Override
	public void putInt(String key, int value) {
		mPayload.put(key, value);
	}

	/* (non-Javadoc)
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#putString(java.lang.String, java.lang.String)
	 */
	@Override
	public void putString(String key, String value) {
		mPayload.put(key, value);
	}

	/* (non-Javadoc)
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#putObject(java.lang.String, java.lang.Object)
	 */
	@Override
	public void putObject(String key, Object value) {
		mPayload.put(key, value);
	}

	/* (non-Javadoc)
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String key) {
		return (Integer) mPayload.get(key);
	}

	/* (non-Javadoc)
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#getString(java.lang.String)
	 */
	@Override
	public String getString(String key) {
		return (String) mPayload.get(key);
	}

	/* (non-Javadoc)
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#getObject(java.lang.String)
	 */
	@Override
	public Object getObject(String key) {
		return mPayload.get(key);
	}

	/* (non-Javadoc)
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#getType()
	 */
	@Override
	public MessageType getType() {
		return mType;
	}
	
	/* (non-Javadoc)
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#getSenderNodeName()
	 */
	@Override
	public String getSenderNodeName() {
		return mSenderNodeName;
	}

	/* (non-Javadoc)
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#setSenderNodeName(java.lang.String)
	 */
	@Override
	public void setSenderNodeName(String senderNodeName) {
		mSenderNodeName = senderNodeName;
	}
	
	/* (non-Javadoc)
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#getReceiverNodeName()
	 */
	@Override
	public String getReceiverNodeName() {
		return mReceiverNodeName;
	}

	/* (non-Javadoc)
	 * @see com.dreamteam.lookme.chord.message.ILookAtMeMessage#setReceiverNodeName(java.lang.String)
	 */
	@Override
	public void setReceiverNodeName(String receiverNodeName) {
		mReceiverNodeName = receiverNodeName;
	}
	
	/* (non-Javadoc)
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
	public static ILookAtMeMessage obtainChordMessage(byte[] data, String senderNodeName) {
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
