package com.dreamteam.lookme.test;

import junit.framework.TestCase;

import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.chord.CommonMessage;
import com.dreamteam.lookme.chord.Message;
import com.dreamteam.lookme.chord.MessageType;

public class ChordMessageTest extends TestCase {

	FullProfile profile;

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPutObject() {
		CommonMessage message = new Message(MessageType.BASIC_PROFILE);
		profile = new FullProfile();
		profile.setName("pippo");
		profile.setSurname("paperino");
		message.putObject(MessageType.FULL_PROFILE.toString(), profile);
		assertEquals(profile, message.getObject(MessageType.FULL_PROFILE.toString()));
		assertEquals(((FullProfile) message.getObject(MessageType.FULL_PROFILE.toString())).getName(), "pippo");
		assertEquals(((FullProfile) message.getObject(MessageType.FULL_PROFILE.toString())).getSurname(), "paperino");

		profile = new FullProfile();
		profile.setName("pluto");
		profile.setSurname("topolino");
		message.putObject(MessageType.FULL_PROFILE.toString(), profile);
		assertEquals(((FullProfile) message.getObject(MessageType.FULL_PROFILE.toString())).getName(), "pluto");
		assertEquals(((FullProfile) message.getObject(MessageType.FULL_PROFILE.toString())).getSurname(), "topolino");
	}

}
