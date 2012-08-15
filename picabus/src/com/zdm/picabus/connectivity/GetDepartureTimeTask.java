package com.zdm.picabus.connectivity;

import org.json.JSONException;
import org.json.JSONObject;

import com.zdm.picabus.enitities.Line;

import android.app.ProgressDialog;
import android.content.Context;

public class GetDepartureTimeTask extends HttpAbstractTask {

	private ProgressDialog waitSpinner;
	private Context context;
	
	public GetDepartureTimeTask(Context mContext, ProgressDialog waitSpinner,
			String taskName, JSONObject reuqestPayload) {
		super(mContext, waitSpinner, taskName, reuqestPayload);
		this.waitSpinner = waitSpinner;
		this.context = mContext;
	}

	@Override
	protected void onPostExecute(String result) {
	    // Create here your JSONObject...
        JSONObject json = null;
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Handle the parse error
			e.printStackTrace();
		}
		IResponseParser rp = new ResponseParser();
        Line line = rp.parseGetDepJsonResponse(json); 
       
        // *****************
        // update UI H-E-R-E
        // *****************
        waitSpinner.dismiss();
	}

}
