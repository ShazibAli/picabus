package com.zdm.picabus.connectivity.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import com.zdm.picabus.connectivity.IResponseParser;
import com.zdm.picabus.connectivity.ResponseParser;
import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.utilities.ErrorsHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

public class GetDepartureTimeTask extends HttpAbstractTask {

	private Context context;
	private JSONObject requestPayload;
	
	public GetDepartureTimeTask(Context mContext, ProgressDialog waitSpinner,
			String taskName, JSONObject requestPayload) {
		super(mContext, waitSpinner, taskName, requestPayload);
		this.context = mContext;
		this.requestPayload = requestPayload;
	}

	@Override
	protected void onPostExecute(String result) {

		JSONObject json = null;
		Line line = null;
		IResponseParser rp = null;
		double lat=0;
		double lng=0;
		
		// not null result from server (which means error in connectivity stage)
		if (result != null) {
			try {
				json = new JSONObject(result);
			} catch (JSONException e) {
				// TODO Handle the parse error
				e.printStackTrace();
			}

			rp = new ResponseParser();
			// reponse from server
			if (json != null) {
				line = rp.parseGetDepJsonResponse(json);
			}

			//get lat and lng for history purpose
			try {
				lat = requestPayload.getDouble("latitude");
				lng = requestPayload.getDouble("longitude");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Get necessary data from the response, and open results activity
			if (line != null) { // Response from server contains data
				// Prepare next intent
				Intent resultsIntent = new Intent(
						"com.zdm.picabus.logic.ResultBusArrivalActivity");
				resultsIntent.putExtra("lineDataModel", line);
				//for history
				resultsIntent.putExtra("lat", lat);
				resultsIntent.putExtra("lng", lng);
				// close the spinner
				waitSpinner.dismiss();

				this.context.startActivity(resultsIntent);
			} else { // null line result from server

				// Start empty results activity
				Intent resultsIntent = new Intent(
						"com.zdm.picabus.logic.EmptyBusResultsActivity");
				// close the spinner
				waitSpinner.dismiss();
				this.context.startActivity(resultsIntent);
			}

		} else { // null result from server
			// close the spinner
			waitSpinner.dismiss();
			ErrorsHandler.createConnectivityErrorAlert(this.context);
		}

	}

}
