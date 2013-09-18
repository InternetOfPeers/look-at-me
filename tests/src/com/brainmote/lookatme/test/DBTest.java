package com.brainmote.lookatme.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;

import com.brainmote.lookatme.SplashActivity;
import com.brainmote.lookatme.bean.FullProfile;
import com.brainmote.lookatme.bean.ProfileImage;
import com.brainmote.lookatme.db.DBOpenHelper;
import com.brainmote.lookatme.db.DBOpenHelperImpl;
import com.brainmote.lookatme.enumattribute.Interest;
import com.brainmote.lookatme.util.ImageUtil;
import com.jayway.android.robotium.solo.Solo;

public class DBTest extends ActivityInstrumentationTestCase2<SplashActivity> {

	private Solo solo;

	DBOpenHelper dbOpenHelper;

	public DBTest() {
		super(SplashActivity.class);
	}

	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		dbOpenHelper = DBOpenHelperImpl.getInstance(getActivity());
	}

	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
		// dbOpenHelper.close();
	}

	// @UiThreadTest
	// public void testRegistration() {
	//
	// solo.assertCurrentActivity("Expected Register Activity",
	// "RegisterActivity");
	// int i = 0;
	//
	// TextView name = (TextView)
	// getActivity().fincom.brainmote.lookatmeote.lookatme.R.id.reg_name);
	// TextView surname = (TextView)
	// getActivity().fcom.brainmote.lookatmenmote.lookatme.R.id.reg_surname);
	// TextView nickname = (TextView)
	// getActivity()com.brainmote.lookatmeainmote.lookatme.R.id.reg_nickname);
	// Button submit = (Button)
	// getActivitycom.brainmote.lookatmebrainmote.lookatme.R.id.btnRegister);
	//
	// String oldName = name.getText() != null ? name.getText().toString() :
	// null;
	// String oldSurname = surname.getText() != null ?
	// surname.getText().toString() : null;
	// String oldUsername = nickname.getText() != null ?
	// nickname.getText().toString() : null;
	//
	// name.setText("pippo");
	// surname.setText("pluto");
	// nickname.setText("paperino");
	//
	// submit.performClick();
	//
	// solo.sleep(3000);
	//
	// // TOFIX Test non coerente. Cosa si sta testando effettivamente?
	// assertEquals(name.getText().toString(), oldName);
	// assertEquals(surname.getText().toString(), oldSurname);
	// assertEquals(nickname.getText().toString(), oldUsername);
	//
	// name.setText(oldName);
	// surname.setText(oldSurname);
	// nickname.setText(oldUsername);
	//
	// submit.performClick();
	//
	// solo.sleep(3000);
	//
	// }

	public void testDB() {
		try {
			dbOpenHelper.getWritableDatabase().beginTransaction();
			File imgFile = new File("/sdcard/Images/test_image.jpg");
			Bitmap photo = null;
			if (imgFile.exists()) {
				photo = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
			}
			ProfileImage profileImage = new ProfileImage();
			profileImage.setImage(ImageUtil.bitmapToByteArray(photo));
			profileImage.setProfileId("1");
			List<ProfileImage> profileImageList = new ArrayList<ProfileImage>();
			profileImageList.add(profileImage);
			FullProfile profileToBeSaved = new FullProfile();
			profileToBeSaved.setId("1");
			profileToBeSaved.setName("Riccardo");
			profileToBeSaved.setSurname("Alfrilli");
			profileToBeSaved.setNickname("AlfaOmega83");
			profileToBeSaved.setMainProfileImage(profileImage);
			profileToBeSaved.setProfileImages(profileImageList);
			profileToBeSaved = dbOpenHelper.saveOrUpdateProfile(profileToBeSaved);
			FullProfile profileSaved = dbOpenHelper.getFullProfile(profileToBeSaved.getId());
			assertEquals(profileToBeSaved.getId(), profileSaved.getId());
			assertEquals(profileToBeSaved.getName(), profileSaved.getName());
			assertEquals(profileToBeSaved.getSurname(), profileSaved.getSurname());
			assertEquals(profileToBeSaved.getNickname(), profileSaved.getNickname());
			assertEquals(profileToBeSaved.getId(), profileSaved.getId());
		} catch (Exception e) {
			fail("error on testDB error:" + e.getMessage());
		} finally {
			dbOpenHelper.getWritableDatabase().endTransaction();
		}
	}

	public void testInterest() {
		try {
			dbOpenHelper.getWritableDatabase().beginTransaction();
			Set<Integer> interestSet = new TreeSet<Integer>();
			interestSet.add(Interest.Jazz.getValue());
			interestSet.add(Interest.Running.getValue());
			interestSet.add(Interest.Beach_volley.getValue());
			dbOpenHelper.saveInterests(interestSet);
		} catch (Exception e) {
			fail("error on testDB error:" + e.getMessage());
		} finally {
			dbOpenHelper.getWritableDatabase().endTransaction();
		}
	}

	// public void testInterest() {
	// try {
	// dbOpenHelper.getWritableDatabase().beginTransaction();
	// Interest interest = new Interest(2, "prova2", false);
	// dbOpenHelper.saveInterest(interest);
	// dbOpenHelper.getWritableDatabase().beginTransaction();
	// List<Interest> list = dbOpenHelper.loadInterests();
	// assertNotNull(list);
	// assertEquals(list.isEmpty(),false);
	// assertEquals(list.get(0).getId(), interest.getId());
	// assertEquals(list.get(0).getDesc(), interest.getDesc());
	//
	// } catch (Exception e) {
	// fail("error on testDB error:" + e.getMessage());
	// } finally {
	// dbOpenHelper.getWritableDatabase().endTransaction();
	// }
	// }

}
