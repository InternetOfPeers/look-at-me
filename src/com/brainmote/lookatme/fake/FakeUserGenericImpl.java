package com.brainmote.lookatme.fake;

import static com.brainmote.lookatme.util.ImageUtil.bitmapToByteArray;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.brainmote.lookatme.ChatConversation;
import com.brainmote.lookatme.bean.ChatConversationImpl;
import com.brainmote.lookatme.bean.FullProfile;
import com.brainmote.lookatme.bean.ProfileImage;
import com.brainmote.lookatme.chord.Node;
import com.brainmote.lookatme.enumattribute.Interest;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.ImageUtil;

public class FakeUserGenericImpl implements FakeUser {

	final int id = 99998999 + new Random().nextInt(1001);
	final String FAKE_NUMBER = String.valueOf(id);
	FullProfile profile;
	String profileId = FAKE_NUMBER;
	String nickname = "Lulu";
	String name = "Lucy";
	String surname = "Taylor";
	String gender = "Female";
	int age = 26;
	Set<Integer> tags = null;
	long profileImageId = id;
	Node node;
	String livingCountry = "ITALY";
	String primaryLanguage = "ITALIAN";
	String status = "Sono molto felice!";
	List<String> answers;
	int answer = 0;

	public FakeUserGenericImpl(Context context) {
		profile = new FullProfile();
		profile.setId(profileId);
		profile.setNickname(nickname);
		profile.setName(name);
		profile.setSurname(surname);
		profile.setGender(gender);
		profile.setAge(age);
		profile.setPrimaryLanguage(primaryLanguage);
		profile.setLivingCountry(livingCountry);
		profile.setStatus(status);
		profile.setMainProfileImage(createProfileImage(context, "fake_user_main_profile_image.jpg"));
		List<ProfileImage> profileImages = new ArrayList<ProfileImage>();
		profileImages.add(createProfileImage(context, "fake_user_profile_image_1.jpg"));
		profileImages.add(createProfileImage(context, "fake_user_profile_image_2.jpg"));
		profileImages.add(createProfileImage(context, "fake_user_profile_image_3.jpg"));
		profile.setProfileImages(profileImages);
		tags = new TreeSet<Integer>();
		tags.add(Interest.Jazz.getValue());
		tags.add(Interest.Running.getValue());
		tags.add(Interest.Beach_volley.getValue());
		profile.setInterestSet(tags);
		node = new Node();
		node.setId(FAKE_NUMBER);
		node.setProfile(profile);
		answers = new ArrayList<String>();
		answers.add("Hi!");
		answers.add("Scooby-dooby-doo!");
		answers.add("Tanto va la gatta la largo che ci lascia lo zampino.");
	}

	protected ProfileImage createProfileImage(Context context, String imageFile) {
		ProfileImage profileImage = new ProfileImage();
		profileImage.setId(profileImageId);
		profileImage.setProfileId(profileId);
		InputStream is = null;
		try {
			is = context.getAssets().open(imageFile);
			Bitmap BitmapImageScaled = ImageUtil.scaleImage(BitmapFactory.decodeStream(is), ImageUtil.DEFAULT_SIZE_IN_DP);
			profileImage.setImage(bitmapToByteArray(BitmapImageScaled));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
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
		return new ChatConversationImpl(Services.currentState.getConversationsStore().calculateConversationId(myProfileId, profile.getId()), profile);
	}

	@Override
	public String getNextAnswer() {
		String message = answers.get(answer++);
		if (answer > answers.size() - 1)
			answer = 0;
		return message;
	}
}
