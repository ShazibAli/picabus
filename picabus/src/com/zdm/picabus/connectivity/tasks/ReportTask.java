package com.zdm.picabus.connectivity.tasks;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class ReportTask extends HttpAbstractTask {

	final int NUMBER_OF_POINTS_PER_TEXTUAL_REPORT = 5;
	private Context context;

	public ReportTask(Context mContext, ProgressDialog waitSpinner,
			String taskName, JSONObject reuqestPayload) {
		super(mContext, waitSpinner, taskName, reuqestPayload);

		this.context = mContext;
	}

	@Override
	protected void onPostExecute(String result) { // note: this method is
													// executed by the UI thread

		int pointsOnCheckin;
		int pointOnCheckout;
		int pointsEarnedByCheckin = 0;

		final String CHECKIN_MESSAGE = "Thank you for checkin! You will earn points for the time you are checked in on a bus, and reports regarding to location will be sent until you checkout from bus";
		final String CHECKIN_ERROR_MESSAGE = "Attemp to checkin failed, please try again later";
		final String CHECKOUT_MESSAGE = "Thank you for checkout! You have earned "
				+ pointsEarnedByCheckin + "points";
		final String CHECKOUT_ERROR_MESSAGE = "Attemp to checkin failed, please try again later";
		final String REPORT_TEXT_MESSAGE = "Thank you for the report! You have earned "
				+ NUMBER_OF_POINTS_PER_TEXTUAL_REPORT + "points";
		final String REPORT_TEXT_ERROR_MESSAGE = "Attemp to report failed, please try again later";

		String taskName = null;

		// null result from server means an error in connectivity stage

		// Checkin
		if (taskName == Tasks.REPORT_LOCATION.getTaskName()) {
			if (result == null) {
				Toast toast = Toast.makeText(context, CHECKIN_ERROR_MESSAGE, 5);
				toast.show();
			} else { // result != null
				/* The update was successful */
				Toast toast = Toast.makeText(context, CHECKIN_MESSAGE, 5);
				toast.show();
				// TODO:pointsOnCheckin=
				// TODO:pointsEarnedByCheckin=pointsOnCheckin-pointOnCheckout
			}
		} else
		// Checkout
		if (taskName == Tasks.REPORT_CHECKOUT.getTaskName()) {
			if (result == null) {
				Toast toast = Toast
						.makeText(context, CHECKOUT_ERROR_MESSAGE, 5);
				toast.show();

			} else { // result != null
				/* The update was successful */
				// TODO:pointsOnCheckout=
				Toast toast = Toast.makeText(context, CHECKOUT_MESSAGE, 5);
				toast.show();

			}
		} else // text report
		if (taskName == Tasks.REPORT_TEXTUAL_MSG.getTaskName()) {
			if (result == null) {
				Toast toast = Toast.makeText(context,
						REPORT_TEXT_ERROR_MESSAGE, 5);
				toast.show();

			} else { // result != null
				/* The update was successful */
				Toast toast = Toast.makeText(context, REPORT_TEXT_MESSAGE, 5);
				toast.show();
			}
		}
	}
}
