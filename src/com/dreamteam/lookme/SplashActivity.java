package com.dreamteam.lookme;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;

import com.dreamteam.lookme.constants.AppSettings;

public class SplashActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Imposto l'activity da caricare
		setContentView(R.layout.splash);

		// Mostra una immagine di splash differente a seconda dell'orientamento
		// del device
		Drawable immagine = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? getResources()
				.getDrawable(R.drawable.splashcreen) : getResources()
				.getDrawable(R.drawable.splashcreen_r);

		ImageView imageView = (ImageView) findViewById(R.id.image_logo);
		imageView.setImageDrawable(immagine);

		/*
		 * New Handler to start the Menu-Activity and close this Splash-Screen
		 * after some seconds.
		 */
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				/* Create an Intent that will start the Menu-Activity. */
				Intent mainIntent = new Intent(SplashActivity.this,
						SocialActivity.class);
				SplashActivity.this.startActivity(mainIntent);
				SplashActivity.this.finish();
			}
		}, AppSettings.SPLASH_DISPLAY_LENGHT);
	}

}
