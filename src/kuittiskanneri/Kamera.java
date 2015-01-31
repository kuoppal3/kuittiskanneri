package kuittiskanneri;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class Kamera extends Activity {
	//static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int PIC_CROP = 2;
	
	private Context _context;
	private Activity _activity;
	private File _photoFile;	
	Uri _picUri;
	String _mCurrentPhotoPath;

	
	public Kamera(Context context, Activity activity)
	{
		_context = context;
		_activity = activity;
	}
	
	public Intent dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    
	    if (takePictureIntent.resolveActivity(_activity.getPackageManager()) != null) {
	        try {
	            _photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	        	Log.w("Tiedostovirhe", "Tiedoston nimeäminen ei onnistunut");
	        }
	        // Continue only if the File was successfully created
	        if (_photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(_photoFile));
	            //_activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	        }
	    }
	    
        return takePictureIntent;
	}

	File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = _context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    _mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	//******************* KUVAN CROPPAUS ******************* 

	private void performCrop(){
	    //call the standard crop action intent (the user device may not support it)
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		    //indicate image type and Uri
		Uri picUri = Uri.fromFile(_photoFile);
		cropIntent.setDataAndType(picUri, "image/*");
		    //set crop properties
		cropIntent.putExtra("crop", "true");
		    //indicate aspect of desired crop
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		    //indicate output X and Y
		cropIntent.putExtra("outputX", 256);
		cropIntent.putExtra("outputY", 256);
		    //retrieve data on return
		cropIntent.putExtra("return-data", true);
		    //start the activity - we handle returning in onActivityResult
		//startActivityForResult(cropIntent, PIC_CROP);
	}
	
	public File palautaKuvatiedosto()
	{
		return _photoFile;
	}
}
