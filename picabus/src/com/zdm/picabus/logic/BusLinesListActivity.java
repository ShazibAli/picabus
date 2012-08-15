package com.zdm.picabus.logic;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.enitities.Company;
import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.Trip;
import com.zdm.picabus.locationservices.GpsResult;
import com.zdm.picabus.utilities.DataCollector;

public class BusLinesListActivity extends ListActivity {

	private final static boolean DEBUG_MODE = true;

	private Line lineDataModel;
	private ArrayList<Integer> linesList = null;
	private LineRowAdapter lineRowAdapter;
	PopupWindow pw;
	int popupRetVal = 0;
	Intent resultsIntent;
	ProgressDialog pd;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buslines_list_screen);
		
		this.pd = new ProgressDialog(this);
		
		// Get list of lines from OPEN CV
		Intent i = getIntent();
		linesList = i.getIntegerArrayListExtra("linesList");

		// Show the list of lines
		this.lineRowAdapter = new LineRowAdapter(this, R.layout.row_bus_lines,
				linesList);
		setListAdapter(this.lineRowAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		int line_number = this.lineRowAdapter.getItem(position);

		// Get current time
		String time = DataCollector.getCurrentTime();

		// Get coordinates
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		GpsResult res = DataCollector.getGpsCoordinates(locationManager);
		Double lat = res.getLat();
		Double lng = res.getLng();

		// Send data to server
	//	if (lat != null || lng != null)
		if (!DEBUG_MODE){
			HttpCaller.getDepartureTime(this, pd, line_number, lat, lng, time, 15);
		} else {
			// for emulator
			// show loading
			HttpCaller.getDepartureTime(this, pd, line_number, 32.046738, 34.758574,
					time, 15);
		}

		onCurrentLineUpdated(line_number);

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
	private void initiatePopupWindow(String directionA, String directionB,
			final Context c) {

		try {

			// Get the instance of the LayoutInflater, use the context of this
			// activity
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Inflate the view from a predefined XML layout
			View layout = inflater.inflate(R.layout.direction_popup,
					(ViewGroup) findViewById(R.id.popup_element));
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
					// resultsIntent.putExtra("direction", 1);
					pw.dismiss();
					// c.startActivity(resultsIntent);
				}
			});

			directionBtnB.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// resultsIntent.putExtra("direction", 2);
					// c.startActivity(resultsIntent);
					pw.dismiss();

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

	/**
	 * 
	 * the function is called after http response completed. the function pops-up
	 * the user to select destination and opens new activity with line data and
	 * destination choice
	 * 
	 * @param line_number
	 *            - line number that was chosen by user
	 */
	private void onCurrentLineUpdated(int line_number) {
		// close loading
		// here the lineModel is updated and ready for use

		// FAKE DATA
		lineDataModel = new Line();
		Trip t = new Trip();
		t.setCompany(Company.EGGED);
		t.setDestination("תחנה מרכזית שקר כלשהו");
		t.setDirectionID(0);
		Time a = new Time(15, 42, 33);
		t.setEta(a);
		t.setLineNumber(line_number);
		Trip t2 = new Trip();
		t2.setCompany(Company.EGGED);
		t2.setDestination("תחנה מרכזית שקר כלשהו");
		t2.setDirectionID(0);
		a = new Time(16, 01, 02);
		t2.setEta(a);
		t2.setLineNumber(line_number);
		List<Trip> trips = new ArrayList<Trip>();
		trips.add(t);
		trips.add(t2);
		lineDataModel.setTrips(trips);
		lineDataModel.setBiDirectional(false);
		lineDataModel.setStopHeadsign("תרעד 12 רמת גן");

		// /Prepare next intent
		Intent resultsIntent = new Intent(
				"com.zdm.picabus.logic.ResultBusArrivalActivity");
		if (lineDataModel != null) {
			resultsIntent.putExtra("lineDataModel", lineDataModel); 
		}
		// Pop-up - choose direction
		if (lineDataModel.isBiDirectional() != true) {
			resultsIntent.putExtra("direction", 3);
			startActivity(resultsIntent);
		} else {
			// initiatePopupWindow("תחנה מרכזית ירוחם", "מקום כלשהו", this);
		}

		resultsIntent.putExtra("direction", 0);
		startActivity(resultsIntent);
	}
}
