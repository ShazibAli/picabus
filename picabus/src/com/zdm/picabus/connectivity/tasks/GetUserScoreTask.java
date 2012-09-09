package com.zdm.picabus.connectivity.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.IResponseParser;
import com.zdm.picabus.connectivity.ResponseParser;

public class GetUserScoreTask extends HttpAbstractTask {

	private Context context;
	private static final int silverScore = 2000;
	private static final int goldScore = 5000;
	
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
				e.printStackTrace();
			}

			rp = new ResponseParser();
			
			// reponse from server
			if (json != null) {
				currentScore = rp.parseGetScoreResponse(json);
				
				//update score in 'my picabus' page
				TextView userPoints = (TextView) ((Activity) context).findViewById(R.id.pointsMyPicabus);//make sure that works from spinner
				TextView pointsTillNext = (TextView) ((Activity) context).findViewById(R.id.numberOfPointsTillNext);//make sure that works from spinner
				userPoints.setText(currentScore + "");
				ImageView medal = (ImageView) ((Activity) context).findViewById(R.id.imageMedal);
				if (currentScore < silverScore) {
					medal.setImageDrawable(context.getResources().getDrawable(R.drawable.bronze_medal));
					pointsTillNext.setText((silverScore - currentScore) + "");
				}
				else if (currentScore >= silverScore && currentScore < goldScore) {
					medal.setImageDrawable(context.getResources().getDrawable(R.drawable.silver_medal));
					pointsTillNext.setText((goldScore - currentScore) + "");
				}
				else if (currentScore >= goldScore) {
					medal.setImageDrawable(context.getResources().getDrawable(R.drawable.gold_medal));
					pointsTillNext.setText("0");
				}				

			}

		}
		
	}

}
