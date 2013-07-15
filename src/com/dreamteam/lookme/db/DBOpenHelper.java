package com.dreamteam.lookme.db;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.dreamteam.lookme.bean.Profile;

public interface DBOpenHelper {

	public static final int DATABASE_VERSION = 3;
	public static final String DATABASE_NAME = "look_me.db";
	public static final String TABLE_PROFILES = "profiles_tb";	
	public static final String TABLE_PROFILES_COLUMN_ID = "id";
	public static final String TABLE_PROFILES_COLUMN_NAME = "name";
	public static final String TABLE_PROFILES_COLUMN_SURNAME = "surname";
	public static final String TABLE_PROFILES_COLUMN_NICKNAME = "nickname";
	public static final String TABLE_PROFILES_COLUMN_IMAGE = "image";
	public static final String TABLE_PROFILES_COLUMN_DEVICE_ID = "device_id";
	
	public static final String TABLE_IMAGES = "images_tb";
	public static final String TABLE_IMAGES_COLUMN_ID = "id";
	public static final String TABLE_IMAGES_COLUMN_IMAGE = "image";
	public static final String TABLE_IMAGES_COLUMN_CHOSED = "chosed";
	
	

	public SQLiteDatabase getWritableDatabase();

	public void close();

	public Profile saveOrUpdateProfile(Profile profile) throws Exception;

	public List<Profile> getProfiles() throws Exception;

	public void deleteProfiles() throws Exception;

	public void deleteProfile(long profileID) throws Exception;

	public Profile getProfile(long profileID) throws Exception;
	
	public Profile getMyProfile() throws Exception;

}