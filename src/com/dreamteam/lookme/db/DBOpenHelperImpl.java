package com.dreamteam.lookme.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.Profile;
import com.dreamteam.lookme.bean.ProfileImage;
import com.google.common.base.Optional;

public class DBOpenHelperImpl extends SQLiteOpenHelper implements DBOpenHelper {


	public static final int DATABASE_VERSION = 1;
	private SQLiteDatabase database;

	private TelephonyManager tm;

	private static DBOpenHelperImpl mInstance = null;
	
	private static final String TB_PROFILES_SELECT_ALL_FIELDS="SELECT " + TABLE_PROFILES_COLUMN_ID + ", " + TABLE_PROFILES_COLUMN_NAME + ", " + TABLE_PROFILES_COLUMN_SURNAME + ", "
			+ TABLE_PROFILES_COLUMN_NICKNAME + ", " + TABLE_PROFILES_COLUMN_AGE + ", "+ TABLE_PROFILES_COLUMN_GENDER + ", " + TABLE_PROFILES_COLUMN_STATUS + ", " + TABLE_PROFILES_COLUMN_BIRTHDATE_YEAR + ", "
			+ TABLE_PROFILES_COLUMN_BIRTHDATE_MONTH + ", "+ TABLE_PROFILES_COLUMN_BIRTHDATE_DAY + ", "+ TABLE_PROFILES_COLUMN_BIRTH_COUNTRY + ", "+ TABLE_PROFILES_COLUMN_BIRTH_CITY + ", "
			+ TABLE_PROFILES_COLUMN_PRIMARY_LANGUAGE + ", "+ TABLE_PROFILES_COLUMN_LIVING_COUNTRY + ", "+ TABLE_PROFILES_COLUMN_LIVING_CITY + ", "+ TABLE_PROFILES_COLUMN_JOB + ", "
			+ TABLE_PROFILES_COLUMN_MY_DESCRIPTION + ", "+ TABLE_PROFILES_COLUMN_MOTTO		
			+ " FROM "
			+ TABLE_PROFILES+ " ";

	private String DEVICE_ID = null;

	public static DBOpenHelper getInstance(Context ctx) {

		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (mInstance == null) {
			mInstance = new DBOpenHelperImpl(ctx.getApplicationContext());
		}
		return mInstance;
	}

	private DBOpenHelperImpl(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		DEVICE_ID = info.getMacAddress();
		if (DEVICE_ID == null)
			DEVICE_ID = tm.getDeviceId();

		database = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_PROFILES + "(" + TABLE_PROFILES_COLUMN_ID + " TEXT PRIMARY KEY, " + TABLE_PROFILES_COLUMN_NICKNAME + " TEXT NOT NULL, "
			    + TABLE_PROFILES_COLUMN_NAME + " TEXT, "+ TABLE_PROFILES_COLUMN_SURNAME + " TEXT, " + TABLE_PROFILES_COLUMN_AGE + " INTEGER, "
				+ TABLE_PROFILES_COLUMN_GENDER + " TEXT, " + TABLE_PROFILES_COLUMN_BIRTHDATE_YEAR + " TEXT, " + TABLE_PROFILES_COLUMN_BIRTHDATE_MONTH + " TEXT, "
				+ TABLE_PROFILES_COLUMN_BIRTHDATE_DAY + " TEXT, " + TABLE_PROFILES_COLUMN_BIRTH_COUNTRY + " TEXT, " + TABLE_PROFILES_COLUMN_BIRTH_CITY + " TEXT, "
				+ TABLE_PROFILES_COLUMN_PRIMARY_LANGUAGE + " TEXT, " + TABLE_PROFILES_COLUMN_LIVING_COUNTRY + " TEXT, " + TABLE_PROFILES_COLUMN_LIVING_CITY + " TEXT, "
				+ TABLE_PROFILES_COLUMN_JOB + " TEXT, " + TABLE_PROFILES_COLUMN_MY_DESCRIPTION + " TEXT, " + TABLE_PROFILES_COLUMN_STATUS + " TEXT, " + TABLE_PROFILES_COLUMN_MOTTO + " TEXT ); ");
		
		db.execSQL("CREATE TABLE " + TABLE_IMAGES + "(" + TABLE_IMAGES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_IMAGES_COLUMN_PROFILE_ID + " TEXT NOT NULL, "
			    + TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO + " TEXT, "+ TABLE_PROFILES_COLUMN_IMAGE + " BLOB ); ");		
		
		db.execSQL("CREATE TABLE " + TABLE_TAGS + "(" + TABLE_TAGS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_TAGS_COLUMN_PROFILE_ID + " TEXT NOT NULL, "
			    + TABLE_TAGS_COLUMN_DESCRIPTION+ " TEXT ); ");			
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			
//			db.execSQL("ALTER TABLE " + DBOpenHelper.TABLE_PROFILES + " ADD COLUMN " + TABLE_PROFILES_COLUMN_AGE + " int");
//			List<FullProfile> profilesPresent = getOnUpgradeAllFullProfiles(db);
//			List<ProfileImage> profileImagesPresent = getOnUpgradeAllImages(db);
//			db.execSQL("DROP TABLE IF EXIST " + TABLE_PROFILES+";");
//			db.execSQL("DROP TABLE IF EXIST " + TABLE_IMAGES+";");
//			db.execSQL("DROP TABLE IF EXIST " + TABLE_TAGS+";");
//			onCreate(db);
//			 Iterator<FullProfile> iter = profilesPresent.iterator();
//			 while (iter.hasNext())
//				 saveOrUpdateProfile(iter.next());
//			 Iterator<ProfileImage> iterImage = profileImagesPresent.iterator();
//			 while (iterImage.hasNext())
//				 saveOrUpdateImage(iterImage.next());			 
		} catch (Exception e) {
			Log.e("db", "error onUpgrade : " + e.getMessage());
		}

	}

	@Override
	public void close() {

		this.close();

		if (database != null) {
			database.close();
		}
	}

	@Override
	public FullProfile saveOrUpdateProfile(FullProfile profile) throws Exception {
		FullProfile oldContact = getFullProfile(profile.getId());

		ContentValues contentValues = new ContentValues();
		contentValues.put(TABLE_PROFILES_COLUMN_NAME, profile.getName());
		contentValues.put(TABLE_PROFILES_COLUMN_SURNAME, profile.getSurname());
		contentValues.put(TABLE_PROFILES_COLUMN_NICKNAME, profile.getNickname());
		contentValues.put(TABLE_PROFILES_COLUMN_ID, profile.getId());		
		contentValues.put(TABLE_PROFILES_COLUMN_AGE, profile.getGender());
		contentValues.put(TABLE_PROFILES_COLUMN_GENDER, profile.getGender());
		contentValues.put(TABLE_PROFILES_COLUMN_BIRTHDATE_DAY, profile.getBirthdateDay());
		contentValues.put(TABLE_PROFILES_COLUMN_BIRTHDATE_MONTH, profile.getBirthdateDay());
		contentValues.put(TABLE_PROFILES_COLUMN_BIRTHDATE_YEAR, profile.getBirthdateYear());
		contentValues.put(TABLE_PROFILES_COLUMN_BIRTH_COUNTRY, profile.getBirthCountry());
		contentValues.put(TABLE_PROFILES_COLUMN_BIRTH_CITY, profile.getBirthCity());
		contentValues.put(TABLE_PROFILES_COLUMN_PRIMARY_LANGUAGE, profile.getPrimaryLanguage());
		contentValues.put(TABLE_PROFILES_COLUMN_LIVING_COUNTRY, profile.getLivingCountry());
		contentValues.put(TABLE_PROFILES_COLUMN_LIVING_CITY, profile.getLivingCity());
		contentValues.put(TABLE_PROFILES_COLUMN_JOB, profile.getJob());
		contentValues.put(TABLE_PROFILES_COLUMN_JOB, profile.getMyDescription());
		contentValues.put(TABLE_PROFILES_COLUMN_JOB, profile.getMotto());
		contentValues.put(TABLE_PROFILES_COLUMN_STATUS, profile.getStatus());

		if (oldContact == null) {
			database.insert(TABLE_PROFILES, null, contentValues);

			
		} else {
			database.update(TABLE_PROFILES, contentValues, TABLE_PROFILES_COLUMN_ID + "=?", new String[] { "" + profile.getId() });
		}
		
		if(profile.getProfileImages()!=null&&!profile.getProfileImages().isEmpty())
			saveOrUpdateImage(profile.getProfileImages().get(0));
		else Log.e("db","problem saving profile,this profile has no foto profileId:"+profile.getId());

		return getFullProfile(profile.getId());

	}

	@Override
	public List<BasicProfile> getProfiles() throws Exception {
		Cursor cursor = null;
		List<BasicProfile> returnList = new ArrayList<BasicProfile>();
		try {
			cursor = database.rawQuery("SELECT " + TABLE_PROFILES_COLUMN_ID + ", " + TABLE_PROFILES_COLUMN_NAME + ", " + TABLE_PROFILES_COLUMN_SURNAME + " FROM "
					+ TABLE_PROFILES, null);

			if (cursor.moveToFirst()) {
				do {
					BasicProfile tempProfile = new BasicProfile();
					tempProfile.setId(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_ID)));
//					tempProfile.setName(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_NAME)));
//					tempProfile.setSurname(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_SURNAME)));
					tempProfile.setNickname(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_NICKNAME)));
//					tempProfile.setImage(cursor.getBlob(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_IMAGE)));
					returnList.add(tempProfile);
				} while (cursor.moveToNext());

			}
		} catch (Exception e) {
			Log.e("db", "error on getting all Profiles: " + e.getMessage());
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}

		return returnList;
	}
	
	
	private List<FullProfile> getOnUpgradeAllFullProfiles(SQLiteDatabase db) throws Exception {
		Cursor cursor = null;
		List<FullProfile> returnList = new ArrayList<FullProfile>();
		try {			
			cursor = db.rawQuery(TB_PROFILES_SELECT_ALL_FIELDS, null);

			if (cursor.moveToFirst()) {
				do {
					FullProfile tempProfile = new FullProfile();
					valorizeProfile(tempProfile, cursor);
					valorizeFullProfile(tempProfile, cursor);
					returnList.add(tempProfile);
				} while (cursor.moveToNext());

			}
		} catch (Exception e) {
			Log.e("db", "error on pugrading DB while getting all Profiles: " + e.getMessage());
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}

		return returnList;
	}	

	@Override
	public void deleteProfiles() throws Exception {
		Cursor cursor = null;
		try {
			cursor = database.rawQuery("DELETE FROM " + TABLE_PROFILES, null);

		} catch (Exception e) {
			Log.e("db", "error on deleting all profiles: " + e.getMessage());
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}

	}

	@Override
	public void deleteProfile(String profileID) throws Exception {
		Cursor cursor = null;
		try {
			cursor = database.rawQuery("DELETE FROM " + TABLE_PROFILES + " WHERE " + TABLE_PROFILES_COLUMN_ID + "=?", new String[] { "" + profileID });

		} catch (Exception e) {
			Log.e("db", "error deleting profile: " + e.getMessage()+" profile Id: "+profileID);
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}

	}

	public FullProfile getMyFullProfile() throws Exception {
		try {
			return getFullProfile(DEVICE_ID);
		} catch (Exception e) {
			Log.e("db", "error on getting getContact: " + e.getMessage());
		}
		Log.d("db", "my contact not found,deviceID: " + tm.getDeviceId());
		return null;

	}

	@Override
	public FullProfile getFullProfile(String contactID) throws Exception {
		Cursor cursor = null;
		FullProfile tempProfile = null;
		try {

			cursor = database.rawQuery(TB_PROFILES_SELECT_ALL_FIELDS + " WHERE " + TABLE_PROFILES_COLUMN_ID + "=?",
					new String[] { "" + contactID });

			if (cursor.moveToFirst()) {
				do {
					tempProfile = new FullProfile();
					//TODO: set the  tag list
					valorizeProfile(tempProfile, cursor);
					valorizeFullProfile(tempProfile, cursor);
					return tempProfile;
				} while (cursor.moveToNext());

			}
		} catch (Throwable e) {
			Log.e("db", "error on getting getContact: " + e.getMessage());
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}
		Log.d("db", "contact not found,contactID: " + contactID);
		return tempProfile;
	}
	
	public BasicProfile getMyBasicProfile() throws Exception {
		try {
			return getBasicProfile(DEVICE_ID);
		} catch (Exception e) {
			Log.e("db", "error on getting getFullProfile: " + e.getMessage());
		}
		Log.d("db", "Full Profile not found,deviceID: " + tm.getDeviceId());
		return null;

	}

	@Override
	public BasicProfile getBasicProfile(String contactID) throws Exception {
		Cursor cursor = null;
		BasicProfile tempProfile = null;
		try {

			cursor = database.rawQuery("SELECT " + TABLE_PROFILES_COLUMN_ID + ", " + TABLE_PROFILES_COLUMN_NICKNAME + ", "
					+ TABLE_PROFILES_COLUMN_GENDER + ", " + TABLE_PROFILES_COLUMN_AGE  					
					+ " FROM " + TABLE_PROFILES + " WHERE " + TABLE_PROFILES_COLUMN_ID + "=?",
					new String[] { "" + contactID });

			if (cursor.moveToFirst()) {
				do {
					tempProfile = new BasicProfile();
					valorizeProfile(tempProfile, cursor);
					tempProfile.setMainProfileImage(getProfileMainImage(contactID));
					return tempProfile;
				} while (cursor.moveToNext());

			}
		} catch (Throwable e) {
			Log.e("db", "error on getting getBasicProfile: " + e.getMessage());
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}
		Log.d("db", "basicProfile not found,contactID: " + contactID);
		return tempProfile;
	}	
	
	
	private List<ProfileImage> getOnUpgradeAllImages(SQLiteDatabase db) throws Exception {		
		Cursor cursor = null;
		ProfileImage profileImage = new ProfileImage();
		List<ProfileImage> returnList= new ArrayList<ProfileImage>();
		try {

			cursor = db.rawQuery("SELECT " + TABLE_IMAGES_COLUMN_ID + ", " + TABLE_IMAGES_COLUMN_PROFILE_ID + ", "
					+ TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO + ", " + TABLE_IMAGES_COLUMN_IMAGE  					
					+ " FROM " + TABLE_IMAGES ,
					null);

			if (cursor.moveToFirst()) {
				do {
					profileImage.setId(cursor.getLong(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_ID)));
					profileImage.setProfileId(cursor.getString(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_PROFILE_ID)));
					profileImage.setMainImage(cursor.getString(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO))!=null?true:false);
					profileImage.setImage(cursor.getBlob(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_IMAGE)));
					returnList.add(profileImage);
				} while (cursor.moveToNext());

			}
		} catch (Throwable e) {
			Log.e("db", "error on upgrading DB while loading allImages : " + e.getMessage());
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}	
		return returnList;
	}

	@Override
	public List<ProfileImage> getProfileImages(String profileId) throws Exception {		
		Cursor cursor = null;
		ProfileImage profileImage = new ProfileImage();
		List<ProfileImage> returnList= new ArrayList<ProfileImage>();
		try {

			cursor = database.rawQuery("SELECT " + TABLE_IMAGES_COLUMN_ID + ", " + TABLE_IMAGES_COLUMN_PROFILE_ID + ", "
					+ TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO + ", " + TABLE_IMAGES_COLUMN_IMAGE  					
					+ " FROM " + TABLE_IMAGES + " WHERE " + TABLE_IMAGES_COLUMN_PROFILE_ID + "=?",
					new String[] { "" + profileId });

			if (cursor.moveToFirst()) {
				do {
					profileImage.setId(cursor.getLong(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_ID)));
					profileImage.setProfileId(cursor.getString(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_PROFILE_ID)));
					profileImage.setMainImage(cursor.getString(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO))!=null?true:false);
					profileImage.setImage(cursor.getBlob(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_IMAGE)));
					returnList.add(profileImage);
				} while (cursor.moveToNext());

			}
		} catch (Throwable e) {
			Log.e("db", "error on loading allImages : " + e.getMessage()+" profile ID:"+ profileId);
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}	
		return returnList;
	}

	@Override
	public ProfileImage getProfileImage(long profileImageId) throws Exception {		
		Cursor cursor = null;
		ProfileImage profileImage = new ProfileImage();
		try {

			cursor = database.rawQuery("SELECT " + TABLE_IMAGES_COLUMN_ID + ", " + TABLE_IMAGES_COLUMN_PROFILE_ID + ", "
					+ TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO + ", " + TABLE_IMAGES_COLUMN_IMAGE  					
					+ " FROM " + TABLE_IMAGES + " WHERE " + TABLE_IMAGES_COLUMN_ID + "=?",
					new String[] { "" + profileImageId });

			if (cursor.moveToFirst()) {
				do {
					profileImage.setId(cursor.getLong(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_ID)));
					profileImage.setProfileId(cursor.getString(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_PROFILE_ID)));
					profileImage.setMainImage(cursor.getString(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO))!=null?true:false);
					profileImage.setImage(cursor.getBlob(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_IMAGE)));
					return profileImage;
				} while (cursor.moveToNext());

			}
		} catch (Throwable e) {
			Log.e("db", "error on loading specific Image : " + e.getMessage()+" image ID:"+ profileImageId);
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}	
		return null;
	}	

	@Override
	public ProfileImage getProfileMainImage(String profileId) throws Exception {		
		Cursor cursor = null;
		ProfileImage profileImage = new ProfileImage();
		try {

//			 TABLE_IMAGES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_IMAGES_COLUMN_PROFILE_ID + " TEXT NOT NULL, "
//					    + TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO + " TEXT, "+ TABLE_PROFILES_COLUMN_IMAGE + " BLOB ); ");		
			
			cursor = database.rawQuery("SELECT " + TABLE_IMAGES_COLUMN_ID + ", " + TABLE_IMAGES_COLUMN_PROFILE_ID + ", "
					+ TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO + ", " + TABLE_IMAGES_COLUMN_IMAGE  					
					+ " FROM " + TABLE_IMAGES + " WHERE " + TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO + " IS NOT NULL" + " AND " + TABLE_IMAGES_COLUMN_PROFILE_ID + "=?",
					new String[] { "" + profileId });

			if (cursor.moveToFirst()) {
				do {
					profileImage.setId(cursor.getLong(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_ID)));
					profileImage.setProfileId(cursor.getString(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_PROFILE_ID)));
					profileImage.setMainImage(cursor.getString(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO))!=null?true:false);
					profileImage.setImage(cursor.getBlob(cursor.getColumnIndex(TABLE_IMAGES_COLUMN_IMAGE)));
					return profileImage;
				} while (cursor.moveToNext());

			}
		} catch (Throwable e) {
			Log.e("db", "error on loading profile main image : " + e.getMessage()+" profile ID:"+ profileId);
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}	
		return null;
	}

	@Override
	public void saveOrUpdateImages(FullProfile profile) throws Exception {		
		List<ProfileImage> profileImages= profile.getProfileImages();		
		Iterator <ProfileImage>iter = profileImages.iterator();
		while(iter.hasNext())
		{
			saveOrUpdateImage(iter.next());
		}
	}

	@Override
	public void saveOrUpdateImage(ProfileImage profileImage) throws Exception {
		ProfileImage proImage =getProfileImage(profileImage.getId());
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(TABLE_IMAGES_COLUMN_PROFILE_ID, profileImage.getProfileId());
		contentValues.put(TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO, profileImage.isMainImage()?"X":null);
		contentValues.put(TABLE_IMAGES_COLUMN_IMAGE, profileImage.getImage());
		if(proImage==null)
		{
			database.insert(TABLE_IMAGES, null, contentValues);
		}else
		{
			database.update(TABLE_IMAGES, contentValues, TABLE_IMAGES_COLUMN_ID + "=?", new String[] { "" + profileImage.getId() });
		}		
	}
	
//	@Override
//	public void saveOrUpdateTag(ProfileTag profileTag) throws Exception {
//
//	}	
	
	private static Profile valorizeProfile(Profile profile,Cursor cursor)
	{
		profile.setId(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_ID)));
		profile.setNickname(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_NICKNAME)));
		profile.setGender(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_GENDER)));
		profile.setAge(cursor.getInt(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_AGE)));
		return profile;
	}
	
	private  FullProfile valorizeFullProfile(FullProfile tempProfile,Cursor cursor) throws Exception
	{
		tempProfile.setName(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_NAME)));
		tempProfile.setSurname(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_SURNAME)));
		tempProfile.setStatus(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_STATUS)));
		tempProfile.setBirthdateYear(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_BIRTHDATE_YEAR)));
		tempProfile.setBirthdateMonth(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_BIRTHDATE_MONTH)));
		tempProfile.setBirthdateDay(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_BIRTHDATE_DAY)));
		tempProfile.setBirthCountry(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_BIRTH_COUNTRY)));
		tempProfile.setBirthCity(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_BIRTH_CITY)));
		tempProfile.setPrimaryLanguage(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_PRIMARY_LANGUAGE)));
		tempProfile.setLivingCountry(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_LIVING_COUNTRY)));
		tempProfile.setLivingCity(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_LIVING_CITY)));
		tempProfile.setJob(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_JOB)));
		tempProfile.setMyDescription(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_MY_DESCRIPTION)));
		tempProfile.setMotto(cursor.getString(cursor.getColumnIndex(TABLE_PROFILES_COLUMN_MOTTO)));		
		tempProfile.setProfileImages(getProfileImages(tempProfile.getId()));
		return tempProfile;
	}	
	
	/**
	 * TOIMPROVE Rendere il metodo più preciso per la decisione se il profilo è
	 * sufficientemente completo per poter continuare ad utilizzare l'app
	 */
	@Override
	public boolean isProfileCompiled() {
		try{
			Optional<FullProfile> optional = Optional.fromNullable(getMyFullProfile());
			return optional.isPresent();
		}catch(Exception e)
		{
			Log.e("DB", "error on isProfileCompiled,error is: "+ e.getMessage());
		}
		return false;

	}

}
