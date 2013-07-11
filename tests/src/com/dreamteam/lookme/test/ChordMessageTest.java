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
		message.putObject(ILookAtMeMessage.PROFILE_KEY, profile);
		assertEquals(profile, message.getObject(ILookAtMeMessage.PROFILE_KEY));
		assertEquals(
				((Profile) message.getObject(ILookAtMeMessage.PROFILE_KEY))
						.getName(),
				"pippo");
		assertEquals(
				((Profile) message.getObject(ILookAtMeMessage.PROFILE_KEY))
						.getSurname(),
				"paperino");

		profile = new Profile();
		profile.setName("pluto");
		profile.setSurname("topolino");
		message.putObject(ILookAtMeMessage.PROFILE_KEY, profile);
		assertEquals(
				((Profile) message.getObject(ILookAtMeMessage.PROFILE_KEY))
						.getName(),
				"pluto");
		assertEquals(
				((Profile) message.getObject(ILookAtMeMessage.PROFILE_KEY))
						.getSurname(),
				"topolino");
	}

}
