package com.dreamteam.lookme.test;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.ImageView;

import com.dreamteam.lookme.MainActivity;
import com.dreamteam.lookme.bean.Profile;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	
	DBOpenHelper dbOpenHelper;
	

	public MainActivityTest() {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		dbOpenHelper = new DBOpenHelperImpl(getActivity());
		
//		dbOpenHelper.getWritableDatabase().beginTransaction();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		dbOpenHelper.close(dbOpenHelper);
//		dbOpenHelper.getWritableDatabase().endTransaction(); 
	}

	public void testActivity() {
		MainActivity activity = getActivity();
		assertNotNull(activity);
	}
	
	
	public void testDB() {
		try{
			dbOpenHelper.getWritableDatabase().beginTransaction();
			File imgFile = new  File("/sdcard/Images/test_image.jpg");
			Bitmap myBitmap =null;
			if(imgFile.exists()){

			    myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());			    
			    
			}
			Profile profileToBeSaved = new Profile();
			profileToBeSaved.setName("Riccardo");
			profileToBeSaved.setSurname("Alfrilli");
			profileToBeSaved.setNickname("AlfaOmega83");
			profileToBeSaved = dbOpenHelper.saveOrUpdateProfile(profileToBeSaved);
			Profile profileSaved=dbOpenHelper.getProfile(profileToBeSaved.getId());
			assertEquals(profileToBeSaved.getId(), profileSaved.getId());
			assertEquals(profileToBeSaved.getName(), profileSaved.getName());
			assertEquals(profileToBeSaved.getSurname(), profileSaved.getSurname());
			assertEquals(profileToBeSaved.getNickname(), profileSaved.getNickname());
			assertEquals(profileToBeSaved.getImage(), profileSaved.getImage());
			
		}catch(Exception e){
			Log.e("test unit", "error on testDB error:" + e.getMessage());
			assertEquals(true, false);
		}finally
		{
			dbOpenHelper.getWritableDatabase().endTransaction();
		}

		
	}
	
	

}
