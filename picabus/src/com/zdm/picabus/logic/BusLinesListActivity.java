package com.zdm.picabus.logic;

import java.util.ArrayList;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
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

	public static final String PICABUS_PREFS_NAME = "picabusSettings";
	private final static boolean DEBUG_MODE = true;
	private ArrayList<Integer> linesList = null;
	private LineRowAdapter lineRowAdapter;
	private ProgressDialog pd;
	private boolean afterGpsNull;
	private IHttpCaller ihc = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ihc = HttpCaller.getInstance();

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
			ihc.getDepartureTime(this, pd, prevLineNumber, lat, lng, prevTime,
					prevTimeInterval);
			finish();
		} else {// error-handle
			ErrorsHandler.createNullGpsCoordinatesErrorAlert(this,
					prevLineNumber, prevTime, prevTimeInterval);
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		//get line number from click
		int line_number = this.lineRowAdapter.getItem(position);
		
		//get time interval from preferences
		SharedPreferences settings = getSharedPreferences(PICABUS_PREFS_NAME, 0);
		int timeInterval = settings.getInt("timeInterval",15);

		// Get current time
		String time = DataCollector.getCurrentTime();
		
		// Get coordinates
		Double lat = null;
		Double lng = null;
		GpsResult res = DataCollector.getGpsCoordinates(this);
		if (res != null) {
			lat = res.getLat();
			lng = res.getLng();
		}
		
		// Send data to server
		if (!DEBUG_MODE) {
			if (lat != null || lng != null) {
				ihc.getDepartureTime(this, pd, line_number, lat, lng, time,
						timeInterval);
				finish();
			} else {
				ErrorsHandler.createNullGpsCoordinatesErrorAlert(this,
						line_number, time, timeInterval);
			}
		} else {
			//in debug mode
			ihc.getDepartureTime(this, pd, line_number, 32.045816, 34.756983,
					"08:20:00", timeInterval);
		}

	}

}
