package com.dreamteam.lookme.test;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.TextView;

import com.dreamteam.lookme.RegisterActivity;
import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.Profile;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.jayway.android.robotium.solo.Solo;

public class DBTest extends ActivityInstrumentationTestCase2<RegisterActivity> {

	private Solo solo;

	DBOpenHelper dbOpenHelper;

	public DBTest() {
		super(RegisterActivity.class);
	}

	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		dbOpenHelper = DBOpenHelperImpl.getInstance(getActivity());
	}

	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
		// dbOpenHelper.close();
	}

	@UiThreadTest
	public void testRegistration() {

		solo.assertCurrentActivity("Expected Register Activity",
				"RegisterActivity");
		int i = 0;

		TextView name = (TextView) getActivity().findViewById(
				com.dreamteam.lookme.R.id.reg_name);
		TextView surname = (TextView) getActivity().findViewById(
				com.dreamteam.lookme.R.id.reg_surname);
		TextView nickname = (TextView) getActivity().findViewById(
				com.dreamteam.lookme.R.id.reg_nickname);
		Button submit = (Button) getActivity().findViewById(
				com.dreamteam.lookme.R.id.btnRegister);

		String oldName = name.getText() != null ? name.getText().toString()
				: null;
		String oldSurname = surname.getText() != null ? surname.getText()
				.toString() : null;
		String oldUsername = nickname.getText() != null ? nickname.getText()
				.toString() : null;

		name.setText("pippo");
		surname.setText("pluto");
		nickname.setText("paperino");

		submit.performClick();

		solo.sleep(3000);

		//TOFIX Test non coerente. Cosa si sta testando effettivamente?
		assertEquals(name.getText().toString(), oldName);
		assertEquals(surname.getText().toString(), oldSurname);
		assertEquals(nickname.getText().toString(), oldUsername);

		name.setText(oldName);
		surname.setText(oldSurname);
		nickname.setText(oldUsername);

		submit.performClick();

		solo.sleep(3000);

	}

	public void testDB() {
		try {
			// TODO test sulle immagini
			dbOpenHelper.getWritableDatabase().beginTransaction();
			File imgFile = new File("/sdcard/Images/test_image.jpg");
			Bitmap myBitmap = null;
			if (imgFile.exists()) {
				myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			}
			FullProfile profileToBeSaved = new FullProfile();
			profileToBeSaved.setId("1");
			profileToBeSaved.setName("Riccardo");
			profileToBeSaved.setSurname("Alfrilli");
			profileToBeSaved.setNickname("AlfaOmega83");
			profileToBeSaved = dbOpenHelper
					.saveOrUpdateProfile(profileToBeSaved);
			FullProfile profileSaved = dbOpenHelper
					.getFullProfile(profileToBeSaved.getId());
			assertEquals(profileToBeSaved.getId(), profileSaved.getId());
			assertEquals(profileToBeSaved.getName(), profileSaved.getName());
			assertEquals(profileToBeSaved.getSurname(),
					profileSaved.getSurname());
			assertEquals(profileToBeSaved.getNickname(),
					profileSaved.getNickname());
			assertEquals(profileToBeSaved.getId(), profileSaved.getId());
		} catch (Exception e) {
			fail("error on testDB error:" + e.getMessage());
		} finally {
			dbOpenHelper.getWritableDatabase().endTransaction();
		}
	}

}
