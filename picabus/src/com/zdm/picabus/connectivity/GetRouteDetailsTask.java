package com.zdm.picabus.connectivity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.zdm.picabus.enitities.Stop;
import com.zdm.picabus.utilities.ErrorsHandler;

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
		IResponseParser rp = null;

		//not null result from server (which means error in connectivity stage)
		if (result != null) {

		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Handle the parse error
			e.printStackTrace();
		}
		rp = new ResponseParser();
		ArrayList <Stop> stops = (ArrayList<Stop>) rp.parseGetRouteJsonResponse(json);
		
		if (stops!=null){
		// Prepare next intent
		Intent resultsIntent = new Intent(
				"com.zdm.picabus.maps.RoutesMapActivity");
		
		resultsIntent.putExtra("stops", stops);
		// close the spinner and launch the new activity
		waitSpinner.dismiss();
		this.context.startActivity(resultsIntent);}
		else
		{
			waitSpinner.dismiss();
			//Start empty results activity
			Intent intent = new Intent(
					"com.zdm.picabus.logic.EmptyMapOverlaysResultActivity");
			this.context.startActivity(intent);
		}
	}
		else{//null response from server
			// close the spinner
			waitSpinner.dismiss();
			//call error alert
			ErrorsHandler.createConnectivityErrorAlert(this.context);

		}
		}
}
