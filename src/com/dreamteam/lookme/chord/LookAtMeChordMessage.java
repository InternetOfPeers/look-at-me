/**
 * Author: Carlo Tassi
 */
package com.dreamteam.lookme.chord;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.dreamteam.lookme.communication.LookAtMeMessage;
import com.dreamteam.lookme.communication.LookAtMeMessageType;

public class LookAtMeChordMessage extends LookAtMeMessage {

	private static final long serialVersionUID = 1L;

	public LookAtMeChordMessage(LookAtMeMessageType type) {
		super(type);
	}
	
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
	 * Recreates LookAtMeChordMessage from the byte array and sender node name
	 */
	public static LookAtMeChordMessage obtainChordMessage(byte[] data, String senderNodeName) {
		final ByteArrayInputStream in = new ByteArrayInputStream(data);
		final ObjectInputStream is;
		LookAtMeChordMessage message = null;

		try {
			is = new ObjectInputStream(in);
			message = (LookAtMeChordMessage) is.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		message.setSenderNodeName(senderNodeName);
		return message;
	}

}
