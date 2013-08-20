package com.brainmote.lookatme.db;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.brainmote.lookatme.bean.BasicProfile;
import com.brainmote.lookatme.bean.ChatMessage;
import com.brainmote.lookatme.bean.Conversation;
import com.brainmote.lookatme.bean.FullProfile;
import com.brainmote.lookatme.bean.Interest;
import com.brainmote.lookatme.bean.ProfileImage;

public interface DBOpenHelper {

	public static final String DATABASE_NAME = "look_me.db";
	public static final String TABLE_PROFILES = "profiles_tb";

	public static final String TABLE_PROFILES_COLUMN_ID = "id";
	public static final String TABLE_PROFILES_COLUMN_NAME = "name";
	public static final String TABLE_PROFILES_COLUMN_SURNAME = "surname";
	public static final String TABLE_PROFILES_COLUMN_NICKNAME = "nickname";
	public static final String TABLE_PROFILES_COLUMN_IMAGE = "image";

	public static final String TABLE_PROFILES_COLUMN_AGE = "age";
	public static final String TABLE_PROFILES_COLUMN_GENDER = "gender";
	public static final String TABLE_PROFILES_COLUMN_BIRTHDATE_YEAR = "birthdate_year";
	public static final String TABLE_PROFILES_COLUMN_BIRTHDATE_MONTH = "birthdate_month";
	public static final String TABLE_PROFILES_COLUMN_BIRTHDATE_DAY = "birthdate_day";
	public static final String TABLE_PROFILES_COLUMN_BIRTH_COUNTRY = "birth_country";
	public static final String TABLE_PROFILES_COLUMN_BIRTH_CITY = "birth_city";
	public static final String TABLE_PROFILES_COLUMN_PRIMARY_LANGUAGE = "primary_language";
	public static final String TABLE_PROFILES_COLUMN_LIVING_COUNTRY = "living_country";
	public static final String TABLE_PROFILES_COLUMN_LIVING_CITY = "living_city";
	public static final String TABLE_PROFILES_COLUMN_JOB = "job";
	public static final String TABLE_PROFILES_COLUMN_MY_DESCRIPTION = "my_description";
	public static final String TABLE_PROFILES_COLUMN_MOTTO = "motto";

	// LOVE GAME
	public static final String TABLE_PROFILES_COLUMN_SEXUAL_PREFERENCES = "sexual_preferences";
	public static final String TABLE_PROFILES_COLUMN_STATUS = "status";
	public static final String TABLE_PROFILES_COLUMN_BODY = "body";
	public static final String TABLE_PROFILES_COLUMN_HAIR = "hair";
	public static final String TABLE_PROFILES_COLUMN_EYES = "eyes";

	public static final String TABLE_IMAGES = "images_tb";
	public static final String TABLE_IMAGES_COLUMN_ID = "id";
	public static final String TABLE_IMAGES_COLUMN_PROFILE_ID = "profile_id";
	public static final String TABLE_IMAGES_COLUMN_IMAGE = "image";
	public static final String TABLE_IMAGES_COLUMN_IS_MAIN_PHOTO = "main_photo";

	public static final String TABLE_INTERESTS = "interests_tb";
	public static final String TABLE_INTERESTS_COLUMN_ID = "id";
	public static final String TABLE_INTERESTS_COLUMN_PROFILE_ID = "profile_id";
	public static final String TABLE_INTERESTS_COLUMN_DESCRIPTION = "image";

	public static final String TABLE_CONVERSATIONS = "conversations_tb";
	public static final String TABLE_CONVERSATIONS_COLUMN_ID = "id";
	public static final String TABLE_CONVERSATIONS_COLUMN_PROFILE_ID = "profile_id";
	public static final String TABLE_CONVERSATIONS_COLUMN_CONVERSATION_DATE = "date";

	public static final String TABLE_MESSAGES = "messages_tb";
	public static final String TABLE_MESSAGES_COLUMN_CONVERSATION_ID = "conversation_id";
	public static final String TABLE_MESSAGES_COLUMN_DATA = "data";
	public static final String TABLE_MESSAGES_COLUMN_TO_NICKNAME = "to_nickname";
	public static final String TABLE_MESSAGES_COLUMN_FROM_NICKNAME = "from_nickname";
	public static final String TABLE_MESSAGES_COLUMN_MESSAGE_DATE = "date";
	public static final String TABLE_MESSAGES_COLUMN_IS_MINE = "is_mine";

	public SQLiteDatabase getWritableDatabase();

	public void close();

	/**
	 * Save the profile if not exist in database otherwise update.
	 * 
	 * @param profile
	 *            the FullProfile to be saved
	 * @return void
	 * @throws java.lang.Exception
	 */
	public FullProfile saveOrUpdateProfile(FullProfile profile) throws Exception;

	/**
	 * Return All the Profiles present in the database
	 * 
	 * @return List<BasicProfile>
	 * @throws java.lang.Exception
	 */
	public List<BasicProfile> getProfiles() throws Exception;

	/**
	 * Delete All the Profiles present in the database
	 * 
	 * @return void
	 * @throws java.lang.Exception
	 */
	public void deleteProfiles() throws Exception;

	/**
	 * Delete a specific Profile present in the database
	 * 
	 * @param profileID
	 *            id of the Profile to be deleted
	 * @return void
	 * @throws java.lang.Exception
	 */
	public void deleteProfile(String profileID) throws Exception;

	/**
	 * Return the FullProfile specified by id
	 * 
	 * @param profileID
	 *            id of the FullProfile to be returned
	 * @return FullProfile
	 * @throws java.lang.Exception
	 */
	public FullProfile getFullProfile(String profileID) throws Exception;

	/**
	 * Return the BasicProfile specified by id
	 * 
	 * @param profileID
	 *            id of the FullProfile to be returned
	 * @return BasicProfile
	 * @throws java.lang.Exception
	 */
	public BasicProfile getBasicProfile(String profileID) throws Exception;

	/**
	 * Return the BasicProfile of the device owner
	 * 
	 * @return BasicProfile
	 * @throws java.lang.Exception
	 */
	public BasicProfile getMyBasicProfile();

	/**
	 * Return the FullProfile of the device owner
	 * 
	 * @return FullProfile
	 * @throws java.lang.Exception
	 */
	public FullProfile getMyFullProfile();

	public void saveOrUpdateImages(FullProfile profile) throws Exception;

	/**
	 * Save the ProfileImage if not exist in database otherwise update.
	 * 
	 * @param profileImage
	 *            the ProfileImage to be saved
	 * @return void
	 * @throws java.lang.Exception
	 */
	public void saveOrUpdateImage(ProfileImage profileImage) throws Exception;

	/**
	 * Save the ProfileImage if not exist in database otherwise update.
	 * 
	 * @param profileId
	 *            the ProfileImage to be saved
	 * @return List<ProfileImage>
	 * @throws java.lang.Exception
	 */
	public List<ProfileImage> getProfileImages(String profileId) throws Exception;

	/**
	 * Return the ProfileImage set as Main by the device owner
	 * 
	 * @param profileId
	 *            the Profile id to load the main image
	 * @return ProfileImage
	 * @throws java.lang.Exception
	 */
	public ProfileImage getProfileMainImage(String profileId) throws Exception;

	/**
	 * Return the ProfileImage specified by an id
	 * 
	 * @param profileImageId
	 *            the ProfileImageId to load
	 * @return ProfileImage
	 * @throws java.lang.Exception
	 */
	public ProfileImage getProfileImage(long profileImageId) throws Exception;

	// public void saveOrUpdateTag(ProfileTag profileTag) throws Exception;

	public boolean isProfileCompiled();

	/**
	 * Delete a specific ProfileImage present in the database
	 * 
	 * @param profileImageID
	 *            id of the Profile to be deleted
	 * @return void
	 * @throws java.lang.Exception
	 */
	public void deleteImage(long profileImageId) throws Exception;

	public void saveInterest(Interest profileTag);

	public List<Interest> getInterests() throws Exception;

	public void deleteInterest(int interestId);

	public void saveInterests(List<Interest> interestsList);

	public List<Conversation> getConversations();

	public void saveOrUpdateConversation(long conversationID, List<ChatMessage> chatMessageList, BasicProfile profile) throws Exception;

}