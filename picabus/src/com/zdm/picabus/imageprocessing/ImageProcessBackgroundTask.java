package com.zdm.picabus.imageprocessing;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.zdm.picabus.cameraservices.CameraActivity;
import com.zdm.picabus.utilities.ErrorsHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Class is used for getting user's facebook profile image from network, using
 * async task
 * 
 */
public class ImageProcessBackgroundTask extends
		AsyncTask<String, Integer, List<Integer>> {

	private ProgressDialog waitSpinner;
	private Context context;
	String imageFilePath;

	/**
	 * Constructor
	 * 
	 * @param c
	 *            - context
	 * @param matrix
	 * @param rotatedImg
	 */
	public ImageProcessBackgroundTask(Context c, ProgressDialog waitSpinner,
			String imageFilePath) {
		this.context = c;
		this.waitSpinner = waitSpinner;
		this.imageFilePath = imageFilePath;
	}

	@Override
	protected void onPostExecute(List<Integer> linesList) {

		waitSpinner.dismiss();

		// null check - if OPEN CV result is null
		if (linesList == null) {
			ErrorsHandler.createNullLinesListErrorAlert(context);
		} else {
			// open results page
			Intent intent = new Intent(
					"com.zdm.picabus.logic.BusLinesListActivity");
			intent.putIntegerArrayListExtra("linesList",
					(ArrayList<Integer>) linesList);
			context.startActivity(intent);
			((CameraActivity) context).finish();
		}
	}

	@Override
	public List<Integer> doInBackground(String... params) {

		Bitmap rotatedImg = null;
		Matrix matrix = null;
		List<Integer> linesList = null;

		// setting the name for this thread for monitoring
		Thread.currentThread().setName("Image Processing Task");

		try {

			Bitmap thumbnail = null;
			Options options = new BitmapFactory.Options();
			options.inScaled = false;

			thumbnail = BitmapFactory.decodeFile(imageFilePath, options);

			thumbnail.setDensity(DisplayMetrics.DENSITY_XHIGH);

			// create a matrix for the manipulation
			matrix = new Matrix();
			// rotate the Bitmap
			matrix.postRotate(90);

			// recreate the new Bitmap
			rotatedImg = Bitmap.createBitmap(thumbnail, 0, 0,
					thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
			
			if (thumbnail != null) {
				thumbnail.recycle();
				thumbnail = null;
			}

			FileOutputStream out = new FileOutputStream(
					Environment.getExternalStorageDirectory()
							+ "/cameraAct.png");
			rotatedImg.compress(Bitmap.CompressFormat.PNG, 90, out);
			
			
			
			linesList = costumizeImg.processImage(context);
			return linesList;

		} catch (FileNotFoundException e) {
			Log.e("Image operations", "file " + imageFilePath + " not found");
			return linesList; // will be null on that case
		}

		finally {
			// release memory
			if (rotatedImg != null) {
				rotatedImg.recycle();
				rotatedImg = null;
			}
			if (matrix != null) {
				matrix.reset();
				matrix = null;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPreExecute()
	 */
	@Override
	protected void onPreExecute() { // note: this method is executed by the UI
									// thread
		super.onPreExecute();
		if (waitSpinner != null) {
			waitSpinner = ProgressDialog.show(context, "Processing Image",
					"Please wait...", true);
		}
	}

}
