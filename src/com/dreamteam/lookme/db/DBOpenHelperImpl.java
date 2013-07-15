package com.dreamteam.lookme.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.dreamteam.lookme.bean.Profile;

public class DBOpenHelperImpl extends SQLiteOpenHelper implements DBOpenHelper {

	private SQLiteDatabase database;

	private TelephonyManager tm;

	private static DBOpenHelperImpl mInstance = null;

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
		tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		database = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_PROFILES + "("
				+ TABLE_PROFILES_COLUMN_ID + " INTEGER PRIMARY KEY, "
				+ TABLE_PROFILES_COLUMN_NAME + " TEXT, "
				+ TABLE_PROFILES_COLUMN_SURNAME + " TEXT, "
				+ TABLE_PROFILES_COLUMN_NICKNAME + " TEXT, "
				+ TABLE_PROFILES_COLUMN_DEVICE_ID + " TEXT, "
				+ TABLE_PROFILES_COLUMN_IMAGE + " BLOB ); ");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			List<Profile> profilesPresent = getProfiles();
			db.execSQL("DROP TABLE IF EXIST " + TABLE_PROFILES);
			onCreate(db);
			// Iterator<Profile> iter = profilesPresent.iterator();
			// while (iter.hasNext())
			// saveOrUpdateProfile(iter.next());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dreamteam.lookme.db.DBOpenHelper#saveOrUpdateContact(com.dreamteam
	 * .lookme.db.Contact)
	 */
	@Override
	public Profile saveOrUpdateProfile(Profile profile) throws Exception {
		Profile oldContact = getProfile(profile.getId());

		if (oldContact == null) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(TABLE_PROFILES_COLUMN_ID, profile.getId());
			contentValues.put(TABLE_PROFILES_COLUMN_NAME, profile.getName());
			contentValues.put(TABLE_PROFILES_COLUMN_SURNAME,
					profile.getSurname());
			contentValues.put(TABLE_PROFILES_COLUMN_NICKNAME,
					profile.getNickname());
			contentValues.put(TABLE_PROFILES_COLUMN_DEVICE_ID,
					profile.getDeviceId());
			contentValues.put(TABLE_PROFILES_COLUMN_IMAGE, profile.getImage());

			profile.setId(database.insert(TABLE_PROFILES, null, contentValues));
		} else {
			ContentValues contentValues = new ContentValues();
			// contentValues.put(TABLE_PROFILES_COLUMN_ID, profile.getId());
			contentValues.put(TABLE_PROFILES_COLUMN_NAME, profile.getName());
			contentValues.put(TABLE_PROFILES_COLUMN_SURNAME,
					profile.getSurname());
			contentValues.put(TABLE_PROFILES_COLUMN_NICKNAME,
					profile.getNickname());
			contentValues.put(TABLE_PROFILES_COLUMN_DEVICE_ID,
					profile.getDeviceId());
			contentValues.put(TABLE_PROFILES_COLUMN_IMAGE, profile.getImage());

			database.update(TABLE_PROFILES, contentValues,
					TABLE_PROFILES_COLUMN_ID + "=" + profile.getId(), null);

			// database.update(TABLE_PROFILES, contentValues,
			// TABLE_PROFILES_COLUMN_ID,
			// new String[]{""+profile.getId()});
		}

		return getProfile(profile.getId());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.db.DBOpenHelper#getContacts()
	 */
	@Override
	public List<Profile> getProfiles() throws Exception {
		Cursor cursor = null;
		List<Profile> returnList = new ArrayList<Profile>();
		try {
			cursor = database.rawQuery(
					"SELECT " + TABLE_PROFILES_COLUMN_ID + ", "
							+ TABLE_PROFILES_COLUMN_NAME + ", "
							+ TABLE_PROFILES_COLUMN_SURNAME + " FROM "
							+ TABLE_PROFILES, null);

			if (cursor.moveToFirst()) {
				do {
					Profile tempProfile = new Profile();
					tempProfile.setId(cursor.getInt(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_ID)));
					tempProfile.setName(cursor.getString(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_NAME)));
					tempProfile.setSurname(cursor.getString(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_SURNAME)));
					tempProfile.setNickname(cursor.getString(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_NICKNAME)));
					tempProfile.setImage(cursor.getBlob(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_IMAGE)));
					returnList.add(tempProfile);
				} while (cursor.moveToNext());

			}
		} catch (Exception e) {
			Log.e("db", "error on getting levelReached: " + e.getMessage());
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}

		return returnList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.db.DBOpenHelper#deleteContacts()
	 */
	@Override
	public void deleteProfiles() throws Exception {
		Cursor cursor = null;
		try {
			cursor = database.rawQuery("DELETE FROM " + TABLE_PROFILES, null);

		} catch (Exception e) {
			Log.e("db", "error on getting levelReached: " + e.getMessage());
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.db.DBOpenHelper#deleteContacts()
	 */
	@Override
	public void deleteProfile(long profileID) throws Exception {
		Cursor cursor = null;
		try {
			cursor = database.rawQuery("DELETE FROM " + TABLE_PROFILES
					+ " WHERE ID=?", new String[] { "" + profileID });

		} catch (Exception e) {
			Log.e("db", "error on getting levelReached: " + e.getMessage());
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}

	}

	public Profile getMyProfile() throws Exception {
		Cursor cursor = null;
		try {
			Profile tempProfile = new Profile();
			cursor = database.rawQuery("SELECT " + TABLE_PROFILES_COLUMN_ID
					+ ", " + TABLE_PROFILES_COLUMN_NAME + ", "
					+ TABLE_PROFILES_COLUMN_SURNAME + ", "
					+ TABLE_PROFILES_COLUMN_NICKNAME + ", "
					+ TABLE_PROFILES_COLUMN_IMAGE + " FROM " + TABLE_PROFILES
					+ " WHERE ID=?", new String[] { "0" });

			if (cursor.moveToFirst()) {
				do {
					tempProfile.setId(cursor.getInt(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_ID)));
					tempProfile.setName(cursor.getString(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_NAME)));
					tempProfile.setSurname(cursor.getString(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_SURNAME)));
					tempProfile.setNickname(cursor.getString(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_NICKNAME)));
					tempProfile.setImage(cursor.getBlob(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_IMAGE)));
					return tempProfile;
				} while (cursor.moveToNext());

			}
		} catch (Exception e) {
			Log.e("db", "error on getting getContact: " + e.getMessage());
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}
		Log.d("db", "my contact not found,deviceID: " + tm.getDeviceId());
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dreamteam.lookme.db.DBOpenHelper#getContact(long)
	 */
	@Override
	public Profile getProfile(long contactID) throws Exception {
		Cursor cursor = null;
		try {
			Profile tempProfile = new Profile();
			cursor = database.rawQuery("SELECT " + TABLE_PROFILES_COLUMN_ID
					+ ", " + TABLE_PROFILES_COLUMN_NAME + ", "
					+ TABLE_PROFILES_COLUMN_SURNAME + ", "
					+ TABLE_PROFILES_COLUMN_NICKNAME + ", "
					+ TABLE_PROFILES_COLUMN_IMAGE + " FROM " + TABLE_PROFILES
					+ " WHERE ID=?", new String[] { "" + contactID });

			if (cursor.moveToFirst()) {
				do {
					tempProfile.setId(cursor.getInt(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_ID)));
					tempProfile.setName(cursor.getString(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_NAME)));
					tempProfile.setSurname(cursor.getString(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_SURNAME)));
					tempProfile.setNickname(cursor.getString(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_NICKNAME)));
					tempProfile.setImage(cursor.getBlob(cursor
							.getColumnIndex(TABLE_PROFILES_COLUMN_IMAGE)));
					return tempProfile;
				} while (cursor.moveToNext());

			}
		} catch (Exception e) {
			Log.e("db", "error on getting getContact: " + e.getMessage());
		} finally {
			if (!cursor.isClosed())
				cursor.close();
		}
		Log.d("db", "contact not found,contactID: " + contactID);
		return null;
	}

}
