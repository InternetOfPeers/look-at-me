package com.dreamteam.lookme.test;

import junit.framework.TestCase;

import com.dreamteam.lookme.bean.Profile;
import com.dreamteam.lookme.chord.message.ChordMessage;
import com.dreamteam.lookme.communication.ILookAtMeMessage;
import com.dreamteam.lookme.communication.LookAtMeMessageType;

public class ChordMessageTest extends TestCase {

	Profile profile;

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPutObject() {
		ILookAtMeMessage message = ChordMessage
				.obtainMessage(LookAtMeMessageType.PREVIEW);
		profile = new Profile();
		profile.setName("pippo");
		profile.setSurname("paperino");
		message.putObject("profile", profile);
		assertEquals(profile, message.getObject("profile"));
		assertEquals(((Profile) message.getObject("profile")).getName(),
				"pippo");
		assertEquals(((Profile) message.getObject("profile")).getSurname(),
				"paperino");

		profile = new Profile();
		profile.setName("pluto");
		profile.setSurname("topolino");
		message.putObject("profile", profile);
		assertEquals(((Profile) message.getObject("profile")).getName(),
				"pluto");
		assertEquals(((Profile) message.getObject("profile")).getSurname(),
				"topolino");
	}

}
