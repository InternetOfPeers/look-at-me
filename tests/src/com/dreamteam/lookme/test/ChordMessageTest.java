package com.dreamteam.lookme.test;

import junit.framework.TestCase;

import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.chord.LookAtMeChordMessage;
import com.dreamteam.lookme.communication.LookAtMeMessage;
import com.dreamteam.lookme.communication.LookAtMeMessageType;

public class ChordMessageTest extends TestCase {

	FullProfile profile;

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPutObject() {
		LookAtMeMessage message = new LookAtMeChordMessage(LookAtMeMessageType.BASIC_PROFILE);
		profile = new FullProfile();
		profile.setName("pippo");
		profile.setSurname("paperino");
		message.putObject(LookAtMeMessageType.FULL_PROFILE.toString(), profile);
		assertEquals(profile, message.getObject(LookAtMeMessageType.FULL_PROFILE.toString()));
		assertEquals(((FullProfile) message.getObject(LookAtMeMessageType.FULL_PROFILE.toString())).getName(), "pippo");
		assertEquals(((FullProfile) message.getObject(LookAtMeMessageType.FULL_PROFILE.toString())).getSurname(), "paperino");

		profile = new FullProfile();
		profile.setName("pluto");
		profile.setSurname("topolino");
		message.putObject(LookAtMeMessageType.FULL_PROFILE.toString(), profile);
		assertEquals(((FullProfile) message.getObject(LookAtMeMessageType.FULL_PROFILE.toString())).getName(), "pluto");
		assertEquals(((FullProfile) message.getObject(LookAtMeMessageType.FULL_PROFILE.toString())).getSurname(), "topolino");
	}

}
