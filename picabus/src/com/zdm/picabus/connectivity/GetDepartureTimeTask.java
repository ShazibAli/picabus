package com.zdm.picabus.connectivity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.zdm.picabus.R;
import com.zdm.picabus.enitities.Destination;
import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.Trip;
import com.zdm.picabus.utilities.DestionationParser;
import com.zdm.picabus.utilities.ErrorsHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

public class GetDepartureTimeTask extends HttpAbstractTask {

	private Context context;
	PopupWindow pw;

	public GetDepartureTimeTask(Context mContext, ProgressDialog waitSpinner,
			String taskName, JSONObject reuqestPayload) {
		super(mContext, waitSpinner, taskName, reuqestPayload);
		this.context = mContext;
	}

	@Override
	protected void onPostExecute(String result) {

		JSONObject json = null;
		Line line = null;
		IResponseParser rp = null;

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

			// Get necessary data from the response, and open results activity
			if (line != null) { // Response from server contains data
				List<Trip> trips = (List<Trip>) line.getTrips();
				Trip firstTrip = trips.get(0);

				// Parse destination
				Destination dest = DestionationParser
						.parseDestination(firstTrip.getDestination());

				// Prepare next intent
				Intent resultsIntent = new Intent(
						"com.zdm.picabus.logic.ResultBusArrivalActivity");
				resultsIntent.putExtra("lineDataModel", line);

				// close the spinner
				waitSpinner.dismiss();

				// Pop-up - choose direction
				if (line.isBiDirectional() != true) {
					resultsIntent.putExtra("direction", 3);
					if (firstTrip.getDirectionID() == 0) {
						resultsIntent.putExtra("destination",
								dest.getDestinationA());
					} else {
						resultsIntent.putExtra("destination",
								dest.getDestinationB());
					}
					this.context.startActivity(resultsIntent);
				} else {
					initiatePopupWindow(dest.getDestinationA(),
							dest.getDestinationB(), this.context, resultsIntent);
				}
			} else { // null result from server

				// close the spinner
				waitSpinner.dismiss();
				// Start empty results activity
				Intent resultsIntent = new Intent(
						"com.zdm.picabus.logic.EmptyBusResultsActivity");
				this.context.startActivity(resultsIntent);
			}

		} else { // null result from server
			// close the spinner
			waitSpinner.dismiss();
			ErrorsHandler.createConnectivityErrorAlert(this.context);
		}

	}

	/**
	 * pop up for choosing the destination of the requested line
	 * 
	 * @param directionA
	 *            - first direction
	 * @param directionB
	 *            - second direction
	 * @param c
	 *            - context of activity
	 */
	private void initiatePopupWindow(final String directionA,
			final String directionB, final Context c, final Intent resultsIntent) {

		try {

			// Get the instance of the LayoutInflater, use the context of this
			// activity
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Inflate the view from a predefined XML layout
			View layout = inflater
					.inflate(R.layout.direction_popup,
							(ViewGroup) ((Activity) c)
									.findViewById(R.id.popup_element));
			// create a 300px width and 470px height PopupWindow
			pw = new PopupWindow(layout, 300, 470, true);

			// display the popup in the center
			pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

			// Popup buttons
			Button directionBtnA = (Button) layout
					.findViewById(R.id.directionBtnA);
			directionBtnA.setText(directionA);
			Button directionBtnB = (Button) layout
					.findViewById(R.id.directionBtnB);
			directionBtnB.setText(directionB);
			Button cancelButton = (Button) layout
					.findViewById(R.id.cancelPopUp);

			// Buttons click listeners
			directionBtnA.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					resultsIntent.putExtra("direction", 0);
					resultsIntent.putExtra("destination", directionA);
					pw.dismiss();
					c.startActivity(resultsIntent);
				}
			});

			directionBtnB.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					resultsIntent.putExtra("direction", 1);
					resultsIntent.putExtra("destination", directionB);
					pw.dismiss();
					c.startActivity(resultsIntent);
				}
			});

			// cancel button
			cancelButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pw.dismiss();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
