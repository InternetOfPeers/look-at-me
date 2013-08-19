package com.dreamteam.util;

import static com.dreamteam.util.ImageUtil.bitmapToByteArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.dreamteam.lookme.ChatConversation;
import com.dreamteam.lookme.bean.ChatConversationImpl;
import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.bean.ProfileImage;
import com.dreamteam.lookme.chord.Node;

public class FakeUserImpl implements FakeUser {

	final int id = 99998999 + new Random().nextInt(1000);
	final String FAKE_NUMBER = String.valueOf(id);
	FullProfile profile;
	String profileId = FAKE_NUMBER;
	String nickname = "Lucy";
	String gender = "Female";
	int age = 26;
	List<String> tags = null;
	long profileImageId = id;
	Node node;

	public FakeUserImpl(Context context) {
		profile = new FullProfile();
		profile.setId(profileId);
		profile.setNickname(nickname);
		profile.setGender(gender);
		profile.setAge(age);
		profile.setTags(tags);
		profile.setMainProfileImage(createProfileImage(context, "fake_user_main_profile_image.jpg"));
		List<ProfileImage> profileImages = new ArrayList<ProfileImage>();
		profileImages.add(createProfileImage(context, "fake_user_profile_image_1.jpg"));
		profileImages.add(createProfileImage(context, "fake_user_profile_image_2.jpg"));
		profileImages.add(createProfileImage(context, "fake_user_profile_image_3.jpg"));
		profile.setProfileImages(profileImages);
		node = new Node();
		node.setId(FAKE_NUMBER);
		node.setProfile(profile);
	}

	private ProfileImage createProfileImage(Context context, String imageFile) {
		ProfileImage profileImage = new ProfileImage();
		profileImage.setId(profileImageId);
		profileImage.setProfileId(profileId);
		InputStream is = null;
		try {
			is = context.getAssets().open(imageFile);
			profileImage.setImage(bitmapToByteArray(BitmapFactory.decodeStream(is)));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return profileImage;
	}

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public ChatConversation getConversation(String myProfileId) {
		return new ChatConversationImpl(CommonUtils.getConversationId(myProfileId, profile.getId()), profile.getNickname(), profile.getAge(), getNode().getId(), profile
				.getMainProfileImage().getImageBitmap());
	}

}
