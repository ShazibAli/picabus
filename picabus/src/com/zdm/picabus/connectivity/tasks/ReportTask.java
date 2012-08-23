package com.zdm.picabus.connectivity.tasks;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;

public class ReportTask extends HttpAbstractTask {
	
	private static final int NUMBER_OF_POINTS_PER_LOCATION_REPORT = 2;
	private static final int NUMBER_OF_POINTS_PER_TEXTUAL_REPORT = 5;

	private Context context;

	public ReportTask(Context mContext,
			ProgressDialog waitSpinner, String taskName,
			JSONObject reuqestPayload) {
		super(mContext, waitSpinner, taskName, reuqestPayload);

		this.context = mContext;
	}

	@Override
	protected void onPostExecute(String result) { // note: this method is executed by the UI thread
		
		// null result from server means an error in connectivity stage
		if (result == null) {

			
		}
		else { // result != null 
			/* The update was successful - deal ui here (show message about the points) */
		}
	}

}
