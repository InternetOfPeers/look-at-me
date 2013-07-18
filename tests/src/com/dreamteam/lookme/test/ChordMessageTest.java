package com.dreamteam.lookme.test;

import junit.framework.TestCase;

import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.Profile;
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
		LookAtMeMessage message = new LookAtMeChordMessage(
				LookAtMeMessageType.PREVIEW);
		profile = new FullProfile();
		profile.setName("pippo");
		profile.setSurname("paperino");
		message.putObject(LookAtMeMessage.PROFILE_KEY, profile);
		assertEquals(profile, message.getObject(LookAtMeMessage.PROFILE_KEY));
		assertEquals(
				((FullProfile) message.getObject(LookAtMeMessage.PROFILE_KEY))
						.getName(),
				"pippo");
		assertEquals(
				((FullProfile) message.getObject(LookAtMeMessage.PROFILE_KEY))
						.getSurname(),
				"paperino");

		profile = new FullProfile();
		profile.setName("pluto");
		profile.setSurname("topolino");
		message.putObject(LookAtMeMessage.PROFILE_KEY, profile);
		assertEquals(
				((FullProfile) message.getObject(LookAtMeMessage.PROFILE_KEY))
						.getName(),
				"pluto");
		assertEquals(
				((FullProfile) message.getObject(LookAtMeMessage.PROFILE_KEY))
						.getSurname(),
				"topolino");
	}

}
