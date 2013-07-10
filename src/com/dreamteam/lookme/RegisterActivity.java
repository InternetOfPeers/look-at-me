package com.dreamteam.lookme;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.lookme.bean.Profile;
import com.dreamteam.lookme.db.DBOpenHelper;
import com.dreamteam.lookme.db.DBOpenHelperImpl;

public class RegisterActivity  extends Activity {
	private static int RESULT_LOAD_IMAGE = 1;
	
	private static final int PICK_IMAGE = 1;
	DBOpenHelper dbOpenHelper;
	 String imageFilePath=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbOpenHelper = new DBOpenHelperImpl(this);
        // Set View to register.xml
        setContentView(R.layout.register);
 
        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
 
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
                                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });
    }
    
    public void onRegister(View view)
    {
    	try{
        	TextView nameScreen = (TextView) findViewById(R.id.reg_name);
        	Profile profile = new Profile();
        	profile.setName(nameScreen.getText().toString());
        	
        	TextView surnameScreen = (TextView) findViewById(R.id.reg_surname);
        	profile.setSurname(surnameScreen.getText().toString());

        	TextView usernameScreen = (TextView) findViewById(R.id.reg_username);
        	profile.setUsername(usernameScreen.getText().toString());   
        	
        	ImageView imageView = (ImageView) findViewById(R.id.imgView);

        	dbOpenHelper.saveOrUpdateProfile(profile);    		
    	}catch(Exception e)
    	{
    		Log.e("REGISTER", "errore during registration! error: "+ e.getMessage());
    	}

    	
    }
    
    public void onChooseImage(View view)
    {
    	try{
    	    
    	    Intent intent = new Intent();
    	    intent.setType("image/*");
    	    intent.setAction(Intent.ACTION_GET_CONTENT);
    	    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    	}catch(Exception e)
    	{
    		Log.e("REGISTER", "errore during registration! error: "+ e.getMessage());
    	}

    	
    } 
    

    	@Override
    	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    		Context context = getApplicationContext();
    		CharSequence text = "";
    		byte[] image = null;
    		try {
    			super.onActivityResult(requestCode, resultCode, data);

    			if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
    					&& null != data) {
    				Uri selectedImage = data.getData();

    				String[] filePathColumn = { MediaStore.Images.Media.DATA };

    				Cursor cursor = getContentResolver().query(selectedImage,
    						filePathColumn, null, null, null);
    				cursor.moveToFirst();

    				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
    				String picturePath = cursor.getString(columnIndex);

    				image = getImageFromPicturePath(picturePath);
    				ImageView imageView = (ImageView) findViewById(R.id.imgView);
    				imageView.setImageBitmap(BitmapFactory.decodeByteArray(image,
    						0, image.length));

    				cursor.close();
    				text = "YOU NEED TO RESTART THE APPLICATION! ";
    			}

    		} catch (Exception e) {
    			Log.e("changing image",
    					"error changing image, error: " + e.toString());
    			text = "Unable to load image ";

    		}
    		int duration = Toast.LENGTH_LONG;

    		Toast toast = Toast.makeText(context, text, duration);
    		toast.show();

    	}
    
    
	public static byte[] getImageFromPicturePath(String picturePath) {
		try {
			Bitmap b = BitmapLoader.loadBitmap(picturePath, 512, 256);
			b = getResizedBitmap(b, 256, 256);

			return bitmapToByteArray(b);

		} catch (Exception e) {
			Log.e("image", e.getMessage());
		}
		return null;
	}
	
	public static byte[] bitmapToByteArray(Bitmap bitmap) {
		int size = bitmap.getWidth() * bitmap.getHeight() * 2;
		ByteArrayOutputStream outStream;
		while (true) {
			outStream = new ByteArrayOutputStream(size);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 0, outStream))
				break;
			size = size * 3 / 2;
		}
		return outStream.toByteArray();
	}
	
	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, newWidth,
				newHeight, false);

		return resizedBitmap;

	}	
	
	

}
class BitmapLoader {
	public static int getScale(int originalWidth, int originalHeight,
			final int requiredWidth, final int requiredHeight) {
		// a scale of 1 means the original dimensions
		// of the image are maintained
		int scale = 1;

		// calculate scale only if the height or width of
		// the image exceeds the required value.
		if ((originalWidth > requiredWidth)
				|| (originalHeight > requiredHeight)) {
			// calculate scale with respect to
			// the smaller dimension
			if (originalWidth < originalHeight)
				scale = Math.round((float) originalWidth / requiredWidth);
			else
				scale = Math.round((float) originalHeight / requiredHeight);

		}

		return scale;
	}

	public static BitmapFactory.Options getOptions(String filePath,
			int requiredWidth, int requiredHeight) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		// setting inJustDecodeBounds to true
		// ensures that we are able to measure
		// the dimensions of the image,without
		// actually allocating it memory
		options.inJustDecodeBounds = true;

		// decode the file for measurement
		BitmapFactory.decodeFile(filePath, options);

		// obtain the inSampleSize for loading a
		// scaled down version of the image.
		// options.outWidth and options.outHeight
		// are the measured dimensions of the
		// original image
		options.inSampleSize = getScale(options.outWidth, options.outHeight,
				requiredWidth, requiredHeight);

		// set inJustDecodeBounds to false again
		// so that we can now actually allocate the
		// bitmap some memory
		options.inJustDecodeBounds = false;

		return options;

	}

	public static Bitmap loadBitmap(String filePath, int requiredWidth,
			int requiredHeight) {

		BitmapFactory.Options options = getOptions(filePath, requiredWidth,
				requiredHeight);

		return BitmapFactory.decodeFile(filePath, options);
	}
}

