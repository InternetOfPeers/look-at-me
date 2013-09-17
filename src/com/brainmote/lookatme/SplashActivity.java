package com.brainmote.lookatme;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.brainmote.lookatme.constants.AppSettings;
import com.brainmote.lookatme.service.Services;
import com.brainmote.lookatme.util.Nav;

public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Salvo un contesto di utilità
		Services.currentState.setContext(getApplicationContext());
		// Services.currentState.setInterestList(Services.businessLogic.getFullInterestList());
		// Imposto l'activity da caricare
		setContentView(R.layout.activity_splashscreen);
		// Mostra una immagine di splash differente a seconda dell'orientamento
		// del device
		Drawable immagine = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? getResources().getDrawable(R.drawable.splashscreen)
				: getResources().getDrawable(R.drawable.splashscreen_r);
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
				Nav.startActivity(activity, NearbyActivity.class);
			}
		}, AppSettings.SPLASH_DISPLAY_LENGHT);
		// Start del servizio di business logic
		Services.businessLogic.start(Services.currentState.getContext());
	}

}
