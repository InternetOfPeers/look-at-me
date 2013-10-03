package com.brainmote.lookatme;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.brainmote.lookatme.bean.ProfileImage;
import com.brainmote.lookatme.util.ImageUtil;
import com.brainmote.lookatme.util.Log;

public class EditProfileActivity extends CommonActivity {

	protected static final int PHOTO_PICKED = 0;
	private static final int CAMERA_REQUEST = 1888; 
	private File imageFromCamera;
	private EditProfileFragment editProfileFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		initDrawerMenu(savedInstanceState, this.getClass(), true);
		editProfileFragment = (EditProfileFragment) getFragmentManager().findFragmentById(R.id.fragment_edit_profile);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.edit_profile, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Verifica quale elemento è stato premuto
		switch (item.getItemId()) {
		case R.id.action_save:
			editProfileFragment.saveProfile(true, true);
			return true;
		}
		return super.onOptionsItemSelected(item);
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

	public void saveProfile() {
		editProfileFragment.saveProfile(true, false);
	}

	public void onTakePictureButtonPressed(View view) {
		try {
			
			//ContentValues values = new ContentValues();
			//values.put(Media.TITLE, "image");
			//Uri tempPhotoUri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
			//intent.putExtra(MediaStore.EXTRA_OUTPUT, tempPhotoUri);
			
			imageFromCamera = createImageFile();
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFromCamera));
            startActivityForResult(cameraIntent, CAMERA_REQUEST); 
             //startActivityForResult(cameraIntent, CAMERA_REQUEST); 
             
			//startActivityForResult(Intent.createChooser(cameraIntent, getString(R.string.edit_profile_add_image_select_picture)), CAMERA_REQUEST);
		} catch (Exception e) {
			Log.e("Errore durante la selezione dell'immagine");
			e.printStackTrace();
		}
	}

	

	public File getAlbumDir()
    {

        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ), 
                "BAC/"
            ); 
         // Create directories if needed
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        return storageDir;
    }
    private File createImageFile() throws IOException {
        // Create an image file name

        String imageFileName =getAlbumDir().toString() +"/image.jpg";
        File image = new File(imageFileName);
        return image;
    }



	public void imageSelectedFromGallery(int requestCode, int resultCode, Intent data) {
		Log.d("Activity result started with " + requestCode);
		super.onActivityResult(requestCode, resultCode, data);
		// Verifico se l'utente ha annullato l'inserimento di una nuova immagine
		
			
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String filePath = cursor.getString(columnIndex);
		cursor.close();
		// verifico che il percorso del file sia un percorso valido
		if (filePath == null) {
			showDialog(getString(R.string.message_warning), getString(R.string.edit_profile_message_unable_to_load_image));
			return;
		}
		if (!ImageUtil.isFilePathValid(filePath)) {
			showDialog(getString(R.string.message_warning), getString(R.string.edit_profile_message_not_valid_image_path));
			return;
		}
		Bitmap photo = ImageUtil.loadBitmap(filePath);
		if (photo == null) {
			showDialog(getString(R.string.message_warning), getString(R.string.edit_profile_message_unable_to_load_image));
			return;
		}
		editProfileFragment.addProfileImage(photo);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		Log.d("Activity result started with " + requestCode);
		
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  

            imageSelectedFromCamera();
        }
		else if (requestCode == PHOTO_PICKED && resultCode == Activity.RESULT_OK && data != null){
			imageSelectedFromGallery(requestCode,resultCode,data);
		}
		
    }

	private void imageSelectedFromCamera() {
		
		Bitmap photo = ImageUtil.loadBitmap(imageFromCamera.getAbsolutePath());
		//Bitmap photo = BitmapFactory.decodeFile(imageFromCamera.getAbsolutePath());
		if (photo == null) {
			showDialog(getString(R.string.message_warning), getString(R.string.edit_profile_message_unable_to_load_image));
			return;
		}
		editProfileFragment.addProfileImage(photo);
	} 

	protected void setMainProfileImage(ProfileImage photo) {
		editProfileFragment.setMainProfileImage(photo);
	}

}
