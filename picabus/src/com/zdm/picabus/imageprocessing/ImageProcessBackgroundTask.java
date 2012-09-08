package com.zdm.picabus.imageprocessing;

import java.util.ArrayList;
import java.util.List;

import com.zdm.picabus.utilities.ErrorsHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;

/**
 * Class is used for getting user's facebook profile image from network, using
 * async task
 * 
 */
public class ImageProcessBackgroundTask extends
		AsyncTask<String, Integer, List<Integer>> {

	private ProgressDialog waitSpinner;
	private Context context;
	private Matrix matrix;
	private Bitmap rotatedImg;

	/**
	 * Constructor
	 * 
	 * @param c
	 *            - context
	 * @param matrix
	 * @param rotatedImg
	 */
	public ImageProcessBackgroundTask(Context c, ProgressDialog waitSpinner,
			Bitmap rotatedImg, Matrix matrix) {
		this.context = c;
		this.waitSpinner = waitSpinner;
		this.matrix = matrix;
		this.rotatedImg = rotatedImg;
	}

	@Override
	protected void onPostExecute(List<Integer> linesList) {

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
		}

		waitSpinner.dismiss();
	}

	@Override
	public List<Integer> doInBackground(String... params) {

		// setting the name for this thread for monitoring
		Thread.currentThread().setName("Image Processing Task");
		List<Integer> linesList = costumizeImg.processImage(context);

		// release memory
		rotatedImg.recycle();
		rotatedImg = null;
		matrix.reset();
		matrix = null;

		return linesList;
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
			waitSpinner = ProgressDialog.show(context, "Processing",
					"Please wait...", true);
		}
	}

}
