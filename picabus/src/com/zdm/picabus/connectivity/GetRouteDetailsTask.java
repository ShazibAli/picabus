package com.zdm.picabus.connectivity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.zdm.picabus.enitities.Stop;

public class GetRouteDetailsTask extends HttpAbstractTask {

	private Context context;
	
	public GetRouteDetailsTask(Context mContext, ProgressDialog waitSpinner,
			String taskName, JSONObject reuqestPayload) {
		super(mContext, waitSpinner, taskName, reuqestPayload);
		
		this.context = mContext;
	}

	@Override
	protected void onPostExecute(String result) { // note: this method is executed by the UI thread
		JSONObject json = null;
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Handle the parse error
			e.printStackTrace();
		}
		IResponseParser rp = new ResponseParser();
		ArrayList <Stop> stops = (ArrayList<Stop>) rp.parseGetRouteJsonResponse(json);
		
		// Prepare next intent
		Intent resultsIntent = new Intent(
				"com.zdm.picabus.maps.RoutesMapActivity");
		
		resultsIntent.putExtra("stops", stops);
		// close the spinner and launch the new activity
		waitSpinner.dismiss();
		this.context.startActivity(resultsIntent);
	}
}
