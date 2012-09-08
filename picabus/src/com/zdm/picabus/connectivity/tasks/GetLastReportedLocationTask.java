package com.zdm.picabus.connectivity.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;

import com.zdm.picabus.connectivity.IResponseParser;
import com.zdm.picabus.connectivity.ResponseParser;
import com.zdm.picabus.enitities.RealtimeLocationReport;

public class GetLastReportedLocationTask extends HttpAbstractTask {

	private Context context;

	public GetLastReportedLocationTask(Context mContext,
			ProgressDialog waitSpinner, String taskName,
			JSONObject reuqestPayload) {
		super(mContext, waitSpinner, taskName, reuqestPayload);

		this.context = mContext;
	}

	@Override
	protected void onPostExecute(String result) { // note: this method is
													// executed by the UI thread
		JSONObject json = null;
		IResponseParser rp = null;

		// null result from server means an error in connectivity stage
		if (result == null) {
			// we do nothing, real-time report won't be available
		}
		else { // result != null
			try {
				json = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			rp = new ResponseParser();
			RealtimeLocationReport rlr = (RealtimeLocationReport) rp
					.parseRealtimeLocationResponse(json);
			
			if (rlr.isEmpty()) {
				// parsing error
			}
			
			else {
				
				/* Enter UI code here - use rlr object */
				
			}
		}
	}

}
