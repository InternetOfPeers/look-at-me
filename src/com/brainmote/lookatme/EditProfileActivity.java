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

import com.brainmote.lookatme.util.CommonUtils;
import com.brainmote.lookatme.util.ImageUtil;
import com.brainmote.lookatme.util.Log;

public class EditProfileActivity extends CommonActivity {
	
	protected static final int PHOTO_PICKED = 0;
	
	private EditProfileFragment editProfileFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_edit_profile);
		editProfileFragment = (EditProfileFragment) getFragmentManager().findFragmentById(R.id.fragment_new_edit_profile);
		initDrawerMenu(savedInstanceState, this.getClass(), false);
	}
	
	public void onSaveButtonPressed(View view) {
		editProfileFragment.saveProfile();
	}
	
	public void onAddImageButtonPressed(View view) {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			startActivityForResult(Intent.createChooser(intent, getString(R.string.edit_profile_add_image_select_picture)), PHOTO_PICKED);
		} catch (Exception e) {
			Log.e("errore during registration! error: " + e.getMessage());
			e.printStackTrace();
		}

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d();
		if (requestCode == PHOTO_PICKED && resultCode == Activity.RESULT_OK && null != data) {
			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String filePath = cursor.getString(columnIndex);
				cursor.close();

				Bitmap photo = ImageUtil.loadBitmap(filePath);
				
				editProfileFragment.addProfileImage(photo);

			} catch (OutOfMemoryError e) {
				Log.d("Out of memory error... cleaning memory");
				Toast.makeText(getApplicationContext(), R.string.edit_profile_message_unable_to_load_image, Toast.LENGTH_LONG).show();
				CommonUtils.cleanMem();
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("error changing image, error: " + e.toString());
				Toast.makeText(getApplicationContext(), R.string.edit_profile_message_unable_to_load_image, Toast.LENGTH_LONG).show();
			}
		}
	}

}
