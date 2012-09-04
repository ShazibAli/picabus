package com.zdm.picabus.connectivity.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.IResponseParser;
import com.zdm.picabus.connectivity.ResponseParser;
import com.zdm.picabus.enitities.ReportResult;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class ReportTask extends HttpAbstractTask {

	private static final int NUMBER_OF_POINTS_PER_LOCATION_REPORT = 2;
	private static final int NUMBER_OF_POINTS_PER_TEXTUAL_REPORT = 5;
	private Context context;

	public ReportTask(Context mContext, ProgressDialog waitSpinner,
			String taskName, JSONObject reuqestPayload) {
		super(mContext, waitSpinner, taskName, reuqestPayload);

		this.context = mContext;

	}

	@Override
	protected void onPostExecute(String result) { // note: this method is
													// executed by the UI thread

		int pointsEarnedByCheckin = 0;

		// TODO: this messages should be in Strings.xml
		final String CHECKOUT_MESSAGE = "Thank you for checkout! You have earned "
				+ pointsEarnedByCheckin + "points";
		final String REPORT_TEXT_MESSAGE = "Thank you for the report! You have earned "
				+ NUMBER_OF_POINTS_PER_TEXTUAL_REPORT + "points";

		// Dismissing progress dialod
		waitSpinner.dismiss();

		JSONObject json = null;
		IResponseParser rp = null;
		ReportResult rs = new ReportResult();
		// not null result from server (which means error in connectivity stage)
		if (result != null) {
			try {
				json = new JSONObject(result);
			} catch (JSONException e) {
				// TODO: handle error + return
				e.printStackTrace();
			}
			rp = new ResponseParser();
			// reponse from server
			if (json != null) {
				rs = rp.parseReportResult(json);
			}

			// empty report result
			if (rs.isEmpty()) {
				Toast toast = Toast.makeText(context, context.getResources()
						.getString(R.string.report_failed_msg),
						Toast.LENGTH_SHORT);
				toast.show();
			}

			else {
				// values of the reponse
				String taskName = rs.getTaskName();
				long currentNumOfPoints = rs.getCurrentNumOfPoints();

				// Checkin
				if (taskName.equalsIgnoreCase(Tasks.REPORT_LOCATION
						.getTaskName())) {

					String message = context.getResources().getString(
							R.string.checkin_msg,
							NUMBER_OF_POINTS_PER_LOCATION_REPORT);
					Toast toast = Toast.makeText(context, message,
							Toast.LENGTH_LONG);
					toast.show();
					// TODO:pointsOnCheckin=
					// TODO:pointsEarnedByCheckin=pointsOnCheckin-pointOnCheckout

					// Checkout
				} else if (taskName.equalsIgnoreCase(Tasks.REPORT_CHECKOUT
						.getTaskName())) {

					/* The update was successful */
					// TODO:pointsOnCheckout=
					Toast toast = Toast.makeText(context, CHECKOUT_MESSAGE, 5);
					toast.show();

					// text report
				} else if (taskName.equals(Tasks.REPORT_TEXTUAL_MSG
						.getTaskName())) {
					/* The update was successful */
					Toast toast = Toast.makeText(context, REPORT_TEXT_MESSAGE,
							5);
					toast.show();

				}
			}
		} else {// result=null
			Toast toast = Toast.makeText(context, context.getResources()
					.getString(R.string.report_failed_msg), Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
