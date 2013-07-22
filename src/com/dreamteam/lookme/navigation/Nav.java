package com.dreamteam.lookme.navigation;

import android.app.Activity;
import android.content.Intent;

import com.dreamteam.lookme.MessagesActivity;
import com.dreamteam.lookme.ProfileActivity;
import com.dreamteam.lookme.SocialActivity;

public class Nav {

	public static void startActivity(Activity currentActivity, Class<? extends Activity> activityClass) {
		if (activityClass != null) {
			currentActivity.startActivity(new Intent(currentActivity, activityClass).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
			currentActivity.finish();
		}
	}

	public static Class<? extends Activity> getActivityFromMenuPosition(int position) {
		switch (position) {
		case 0:
			return ProfileActivity.class;
		case 1:
			return SocialActivity.class;
		case 2:
			return MessagesActivity.class;
		default:
			break;
		}
		return null;
	}
}
