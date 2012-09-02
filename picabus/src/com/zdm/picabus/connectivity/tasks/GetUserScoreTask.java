package com.zdm.picabus.connectivity.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.IResponseParser;
import com.zdm.picabus.connectivity.ResponseParser;

public class GetUserScoreTask extends HttpAbstractTask {

	private Context context;

	public GetUserScoreTask(Context mContext, ProgressDialog waitSpinner,
			String taskName, JSONObject reuqestPayload) {
		super(mContext, waitSpinner, taskName, reuqestPayload);

		this.context = mContext;
	}

	public GetUserScoreTask(Activity activity, Context mContext, ProgressDialog waitSpinner,
			String taskName, JSONObject reuqestPayload) {
		super(mContext, waitSpinner, taskName, reuqestPayload);

		this.context = mContext;
	}
	

	@Override
	protected void onPostExecute(String result) {
		JSONObject json = null;
		IResponseParser rp = null;
		long currentScore = 0;
		waitSpinner.dismiss();
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
				currentScore = rp.parseGetScoreResponse(json);
				
				//update score in 'my picabus' page
				TextView userPoints = (TextView) ((Activity) context).findViewById(R.id.pointsMyPicabus);//make sure that works from spinner
				userPoints.setText("You have earned " + currentScore + " points!");
			}

		}
		
		/* Handle UI score update here - use current score as the updated score! */
	}

}
