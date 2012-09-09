package com.zdm.picabus.cameraservices;

import java.io.File;
import com.zdm.picabus.imageprocessing.ImageProcessBackgroundTask;
import com.zdm.picabus.utilities.ErrorsHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

public class CameraActivity extends Activity {
	/** Called when the activity is first created. */

	private static final int CAMERA_PIC_REQUEST = 1337;
	private static final String imageFilePath = Environment
			.getExternalStorageDirectory() + "/tmp_image.jpg";
	private Context c;
	private ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		c = this;
		this.pd = new ProgressDialog(this);

		File file = new File(Environment.getExternalStorageDirectory(),
				"/tmp_image.jpg");

		Uri outputFileUri = Uri.fromFile(file);

		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAMERA_PIC_REQUEST) {

			if (resultCode == RESULT_OK) {

				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					
					// Send image to open cv background task
					ImageProcessBackgroundTask imageProcessBackgroundTask = new ImageProcessBackgroundTask(
							c, pd, imageFilePath);
					imageProcessBackgroundTask.execute();
					
				}

				else{
					ErrorsHandler.createCameraError(c); //test that
				}
			} else if (resultCode == RESULT_CANCELED) {
				finish();
			}
		} else {
			finish();
		}
	}

}
