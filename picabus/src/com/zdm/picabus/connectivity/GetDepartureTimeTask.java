package com.zdm.picabus.connectivity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.zdm.picabus.R;
import com.zdm.picabus.enitities.Company;
import com.zdm.picabus.enitities.Destination;
import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.Trip;
import com.zdm.picabus.utilities.DestionationParser;

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

	private ProgressDialog waitSpinner;
	private Context context;
	PopupWindow pw;
	
	public GetDepartureTimeTask(Context mContext, ProgressDialog waitSpinner,
			String taskName, JSONObject reuqestPayload) {
		super(mContext, waitSpinner, taskName, reuqestPayload);
		this.waitSpinner = waitSpinner;
		this.context = mContext;
	}

	@Override
	protected void onPostExecute(String result) {
	    // Create here your JSONObject...
/*        JSONObject json = null;
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Handle the parse error
			e.printStackTrace();
		}*/
	//	IResponseParser rp = new ResponseParser();
   //     Line line = rp.parseGetDepJsonResponse(json); 
       
		// FAKE DATA creation - remove
		String destination = "מתחם גי/ילדי טהרן-ראשון לציון<->ת. רכבת מרכז-תל אביב יפו";
		Line lineDataModel = new Line();
		Trip t = new Trip();
		t.setCompany(Company.EGGED);
		t.setDestination(destination);
		t.setDirectionID(0);
		t.setLineNumber(15);
		Time a = new Time(15, 42, 33);
		t.setEta(a);
		t.setLineNumber(15);
		Trip t2 = new Trip();
		t2.setCompany(Company.EGGED);
		t2.setDestination(destination);
		t2.setDirectionID(0);
		a = new Time(16, 01, 02);
		t2.setEta(a);
		t2.setLineNumber(15);
		Trip t3 = new Trip();
		t3.setCompany(Company.EGGED);
		t3.setDestination(destination);
		t3.setDirectionID(1);
		a = new Time(16, 01, 02);
		t3.setEta(a);
		t3.setLineNumber(15);
		Trip t4 = new Trip();
		t4.setCompany(Company.EGGED);
		t4.setDestination(destination);
		t4.setDirectionID(1);
		a = new Time(19, 01, 22);
		t4.setEta(a);
		t4.setLineNumber(15);
		List<Trip> trips = new ArrayList<Trip>();
		trips.add(t);
		trips.add(t2);
		trips.add(t3);
		trips.add(t4);
		lineDataModel.setTrips(trips);
		lineDataModel.setBiDirectional(true);
		lineDataModel.setStopHeadsign("תרעד 12 רמת גן");

		//Get the requested line number - comment in when real data!
		
/*		List<Trip> trips = (List<Trip>) line.getTrips();
		Trip firstTrip = trips.get(0);
		int line_number = firstTrip.getLineNumber();*/
		
		//Parse destination
		Destination dest =DestionationParser.parseDestination(destination);
		
		//Prepare next intent
		Intent resultsIntent = new Intent(
				"com.zdm.picabus.logic.ResultBusArrivalActivity");
		if (lineDataModel != null) {
			resultsIntent.putExtra("lineDataModel", lineDataModel); 
		}
		// Pop-up - choose direction
		if (lineDataModel.isBiDirectional() != true) {
			resultsIntent.putExtra("direction", 3);
			this.context.startActivity(resultsIntent);
		} else {
			initiatePopupWindow(dest.getDestinationA(), dest.getDestinationB(), this.context, resultsIntent);
		}

     //   waitSpinner.dismiss();
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
	private void initiatePopupWindow(final String directionA, final String directionB,
			final Context c, final Intent resultsIntent) {

		try {

			// Get the instance of the LayoutInflater, use the context of this
			// activity
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Inflate the view from a predefined XML layout
			View layout = inflater.inflate(R.layout.direction_popup,
					(ViewGroup) ((Activity) c).findViewById(R.id.popup_element));
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
					resultsIntent.putExtra("destination",directionA);
					pw.dismiss();
					c.startActivity(resultsIntent);
				}
			});

			directionBtnB.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					resultsIntent.putExtra("direction", 1);
					resultsIntent.putExtra("destination",directionB);
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
