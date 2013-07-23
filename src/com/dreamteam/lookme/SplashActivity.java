package com.dreamteam.lookme;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.dreamteam.lookme.constants.AppSettings;
import com.dreamteam.lookme.navigation.Nav;

public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Imposto l'activity da caricare
		setContentView(R.layout.splash);

		// Mostra una immagine di splash differente a seconda dell'orientamento
		// del device
		Drawable immagine = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? getResources().getDrawable(R.drawable.splashcreen)
				: getResources().getDrawable(R.drawable.splashcreen_r);

		ImageView imageView = (ImageView) findViewById(R.id.image_logo);
		imageView.setImageDrawable(immagine);

		final Activity activity = this;
		/*
		 * New Handler to start the Menu-Activity and close this Splash-Screen
		 * after some seconds.
		 */
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Nav.startActivity(activity, SocialActivity.class);
			}
		}, AppSettings.SPLASH_DISPLAY_LENGHT);
	}

}
