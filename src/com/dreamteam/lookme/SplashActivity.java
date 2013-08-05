package com.dreamteam.lookme;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.dreamteam.lookme.bean.BasicProfile;
import com.dreamteam.lookme.bean.FullProfile;
import com.dreamteam.lookme.constants.AppSettings;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;
import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;

public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Salvo un contesto di utilità
		Services.currentState.setContext(getApplicationContext());

		Services.currentState.setInterestList(Services.businessLogic.getFullInterestList());

		// carico il mio profilo sia basic che full nel repository statico
		DBOpenHelper dbOpenHelper = DBOpenHelperImpl.getInstance(getApplicationContext());
		try {
			if (dbOpenHelper.isProfileCompiled()) {
				FullProfile myFullProfile = dbOpenHelper.getMyFullProfile();
				BasicProfile myBasicProfile = dbOpenHelper.getMyBasicProfile();
				Services.currentState.setMyBasicProfile(myBasicProfile);
				Services.currentState.setMyFullProfile(myFullProfile);
			}
		} catch (Exception e) {
			Log.e("Errore nel recupero del profilo");
		}

		// Imposto l'activity da caricare
		setContentView(R.layout.splashscreen);

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
