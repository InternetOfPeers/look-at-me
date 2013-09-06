package com.brainmote.lookatme;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.brainmote.lookatme.bean.ProfileImage;
import com.brainmote.lookatme.util.ImageUtil;
import com.brainmote.lookatme.util.Log;

public class EditProfileActivity extends CommonActivity {

	protected static final int PHOTO_PICKED = 0;

	private EditProfileFragment editProfileFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		editProfileFragment = (EditProfileFragment) getFragmentManager().findFragmentById(R.id.fragment_edit_profile);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
	}

	public void onSaveButtonPressed(View view) {
		editProfileFragment.saveProfile();
	}

	public void onSelectPictureButtonPressed(View view) {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			startActivityForResult(Intent.createChooser(intent, getString(R.string.edit_profile_add_image_select_picture)), PHOTO_PICKED);
		} catch (Exception e) {
			Log.e("Errore durante la selezione dell'immagine");
			e.printStackTrace();
		}

	}
	
	public void onTakePictureButtonPressed(View view) {
		try {
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(Intent.createChooser(intent, getString(R.string.edit_profile_add_image_select_picture)), PHOTO_PICKED);
		} catch (Exception e) {
			Log.e("Errore durante la selezione dell'immagine");
			e.printStackTrace();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("Activity result started with " + requestCode);
		super.onActivityResult(requestCode, resultCode, data);
		// Verifoc se l'utente ha annullato l'inserimento di una nuova immagine
		if (requestCode != PHOTO_PICKED || resultCode != Activity.RESULT_OK || data == null)
			return;
		
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String filePath = cursor.getString(columnIndex);
		cursor.close();
		// verifico che il percorso del file sia un percorso valido
		if (!ImageUtil.isFilePathValid(filePath)) {
			Toast.makeText(getApplicationContext(), R.string.edit_profile_message_not_valid_image_path, Toast.LENGTH_LONG).show();
			return;
		}
		Bitmap photo = ImageUtil.loadBitmap(filePath);

		if (photo == null) {
			Toast.makeText(getApplicationContext(), R.string.edit_profile_message_unable_to_load_image, Toast.LENGTH_SHORT).show();
			return;
		}
		editProfileFragment.addProfileImage(photo);
	}

	public void setMainProfileImage(ProfileImage photo) {
		editProfileFragment.setMainProfileImage(photo);
	}

}
