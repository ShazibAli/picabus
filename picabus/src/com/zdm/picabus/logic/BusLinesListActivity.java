package com.zdm.picabus.logic;

import java.util.ArrayList;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.connectivity.IHttpCaller;
import com.zdm.picabus.locationservices.GpsResult;
import com.zdm.picabus.utilities.DataCollector;
import com.zdm.picabus.utilities.ErrorsHandler;

/**
 * 
 * Activity for displaying list of line number results either from camera or the
 * manual search
 * 
 */
public class BusLinesListActivity extends ListActivity {

	private final static boolean DEBUG_MODE = true;

	private ArrayList<Integer> linesList = null;
	private LineRowAdapter lineRowAdapter;
	PopupWindow pw;
	int popupRetVal = 0;
	Intent resultsIntent;
	ProgressDialog pd;
	int timeInterval = 15;
	boolean afterGpsNull;
	IHttpCaller ihc = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ihc = new HttpCaller();
		
		Intent i = getIntent();

		// Check if this is the first run of activity, or after GPS null
		// coordinates
		afterGpsNull = i.getBooleanExtra("isGpsError", false);

		// Open progress dialog
		this.pd = new ProgressDialog(this);

		// Handle regular case - first time of activity
		if (!afterGpsNull) {
			setContentView(R.layout.buslines_list_screen);

			// Get list of lines from OPEN CV
			linesList = i.getIntegerArrayListExtra("linesList");

			// Show the list of lines
			this.lineRowAdapter = new LineRowAdapter(this,
					R.layout.row_bus_lines, linesList);
			setListAdapter(this.lineRowAdapter);
		}
		// after GPS was null - don't take data from the user
		else {
			handleAfterGpsNullResult(i);
		}
	}

	/**
	 * Handles a case when GPS coordinates were null, and this activity was run
	 * again for another attempt
	 * 
	 * @param i
	 *            - current intent
	 */
	private void handleAfterGpsNullResult(Intent i) {

		int prevLineNumber;
		String prevTime;
		int prevTimeInterval;
		GpsResult res;
		Double lat;
		Double lng;

		// get previous user's session results
		prevLineNumber = i.getIntExtra("lineNumber", -1);
		prevTime = i.getStringExtra("time");
		prevTimeInterval = i.getIntExtra("timeInterval", -1);

		// attempt to get GPS coordinates again
		res = DataCollector.getGpsCoordinates(this);
		lat = res.getLat();
		lng = res.getLng();

		// check GPS coordinates are not null and send request to server
		if (lat != null || lng != null) {
			ihc.getDepartureTime(this, pd, prevLineNumber, lat, lng,
					prevTime, prevTimeInterval);
			finish();
		} else {// error-handle
			ErrorsHandler.createNullGpsCoordinatesErrorAlert(this,
					prevLineNumber, prevTime, prevTimeInterval);
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		int line_number = this.lineRowAdapter.getItem(position);

		// Get current time
		String time = DataCollector.getCurrentTime();

		// Get coordinates
		GpsResult res = DataCollector.getGpsCoordinates(this);
		Double lat = res.getLat();
		Double lng = res.getLng();

		// Send data to server
		if (!DEBUG_MODE) {
			if (lat != null || lng != null) {
				ihc.getDepartureTime(this, pd, line_number, lat, lng,
						time, timeInterval);
				finish();
			} else {
				ErrorsHandler.createNullGpsCoordinatesErrorAlert(this,
						line_number, time, timeInterval);
			}
		} else {
			// for emulator
			ihc.getDepartureTime(this, pd, line_number, 32.045816,
					34.756983, "08:20:00", timeInterval);
		}

	}

}
