package com.dreamteam.lookme;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dreamteam.lookme.service.Services;
import com.dreamteam.util.Log;
import com.dreamteam.util.Nav;

public class NearbyActivity extends CommonActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
		// Controllo che l'utente abbia compilato almeno i campi obbilgatori
		// del profilo
		if (Services.currentState.getMyBasicProfile() == null) {
			// L'utente deve compilare il profilo prima di iniziare
			Log.d("It's the first time this app run!");
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.first_time_dialog);
			dialog.setTitle("First launch");
			Button dialogButton = (Button) dialog.findViewById(R.id.buttonBegin);
			// if button is clicked, close the custom dialog
			dialogButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Nav.startActivity(NearbyActivity.this, EditProfileActivity.class);
					dialog.dismiss();
				}
			});
			dialog.show();
		}
	}

	// protected void onCreateOld(Bundle savedInstanceState) {
	// Log.d();
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.activity_nearby);
	// socialListFragment = (NearbyListFragment)
	// getFragmentManager().findFragmentById(R.id.fragment_list);
	// // socialProfileFragment = (ProfileActivit)
	// // getFragmentManager().findFragmentById(R.id.fragment_profile);
	// if
	// (Optional.fromNullable(extras.getString(NotificationService.NODE_KEY_ID)).isPresent())
	// {
	// // Recupero il profilo se ancora esistente il nodo corrispondente
	// // TODO Poiché il profile full non viene memorizzato nello state,
	// // non posso recuperare faclimente il profilo come credevo
	// // inizialmente: va fatta una richiesta ad hoc
	// // FullProfile profile = (FullProfile)
	// //
	// Services.currentState.getSocialNodeMap().get(extras.getString(Notify.NODE_KEY_ID)).getProfile();
	// // FullProfile profile = null;
	// // Carica la schermata visualizzando il profilo dell'utente che
	// // corrisponde al nodo passato
	// // setFragment(SOCIAL_PROFILE_FRAGMENT, profile);
	//
	// setFragment(SOCIAL_PROFILE_FRAGMENT);
	// Services.businessLogic.requestFullProfile(extras.getString(NotificationService.NODE_KEY_ID));
	// // entro in attesa
	// // ProgressDialog loadingDialog = new ProgressDialog(this);
	// // loadingDialog.setTitle("Loading profile");
	// // loadingDialog.show();
	//
	// } else {
	// // carica l'activity l'ultimo fragment impostato
	// setFragment(currentFragment);
	// }
	// // Inizializzazione del menu
	// initDrawerMenu(savedInstanceState, this.getClass(), true);

	// }

	// protected void setFragment(int fragment) {
	// FullProfile profile = Services.currentState.getProfileViewed() != null ?
	// (FullProfile) Services.currentState.getProfileViewed().getProfile() :
	// null;
	// setFragment(fragment, profile);
	// }

	// protected void setFragment(int fragment, FullProfile profile) {
	// Log.d("" + fragment);
	// Log.d("changing fragment from " + currentFragment + " to " + fragment);
	// currentFragment = fragment;
	// fragmentTransaction = getFragmentManager().beginTransaction();
	// switch (fragment) {
	// case SOCIAL_LIST_FRAGMENT:
	// fragmentTransaction.show(socialListFragment);
	// fragmentTransaction.hide(socialProfileFragment);
	// break;
	// case SOCIAL_PROFILE_FRAGMENT:
	// // necessario per impostare il profilo richiesto nella vista
	// socialProfileFragment.prepareProfileAttributes(profile);
	// fragmentTransaction.hide(socialListFragment);
	// fragmentTransaction.show(socialProfileFragment);
	// break;
	// default:
	// fragmentTransaction.show(socialListFragment);
	// fragmentTransaction.hide(socialProfileFragment);
	// break;
	// }
	// this.fragmentTransaction.commit();
	// }

}