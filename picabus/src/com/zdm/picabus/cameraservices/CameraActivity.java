package com.zdm.picabus.cameraservices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.zdm.picabus.imageprocessing.ImageProcessBackgroundTask;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;

public class CameraActivity extends Activity {
	/** Called when the activity is first created. */

	private static final int CAMERA_PIC_REQUEST = 1337;
	private static final String imageFilePath = Environment
			.getExternalStorageDirectory() + "/tmp_image.jpg";
	private Context con;
	private ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		con = this;
		this.pd = new ProgressDialog(this);

		File file = new File(Environment.getExternalStorageDirectory(),
				"/tmp_image.jpg");
		// Uri imageFileUri = Uri.parse(imageFilePath);
		Uri outputFileUri = Uri.fromFile(file);

		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		// cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
		// imageFileUri);

		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
		// startActivityForResult(cameraIntent, 1); //TAKE_PICTURE = 1
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == CAMERA_PIC_REQUEST) {

			if (resultCode == RESULT_OK) {

				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					try {

						Bitmap thumbnail = null;
						Options options = new BitmapFactory.Options();
						options.inScaled = false;

						thumbnail = BitmapFactory.decodeFile(imageFilePath,
								options);

						thumbnail.setDensity(DisplayMetrics.DENSITY_XHIGH);

						// create a matrix for the manipulation
						Matrix matrix = new Matrix();
						// rotate the Bitmap
						matrix.postRotate(90);

						// recreate the new Bitmap
						Bitmap rotatedImg = Bitmap.createBitmap(thumbnail, 0,
								0, thumbnail.getWidth(), thumbnail.getHeight(),
								matrix, true);

						FileOutputStream out = new FileOutputStream(
								Environment.getExternalStorageDirectory()
										+ "/cameraAct.png");
						rotatedImg.compress(Bitmap.CompressFormat.PNG, 90, out);
						thumbnail.recycle();
						thumbnail = null;

						// Send image to open cv and get result
						ImageProcessBackgroundTask imageProcessBackgroundTask = new ImageProcessBackgroundTask(
								con, pd, rotatedImg, matrix);
						imageProcessBackgroundTask.execute();

					} catch (FileNotFoundException e) {
						Log.e("Image operations", "file " + imageFilePath
								+ " not found");
					}

				}

			} else if (resultCode == RESULT_CANCELED) {
				finish();
			}
		} else {
			finish();
		}
	}

}
