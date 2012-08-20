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
import com.zdm.picabus.locationservices.GpsResult;
import com.zdm.picabus.utilities.DataCollector;
import com.zdm.picabus.utilities.ErrorsHandler;

public class BusLinesListActivity extends ListActivity {

	private final static boolean DEBUG_MODE = true;

	private ArrayList<Integer> linesList = null;
	private LineRowAdapter lineRowAdapter;
	PopupWindow pw;
	int popupRetVal = 0;
	Intent resultsIntent;
	ProgressDialog pd;
	int timeInterval=15;
	boolean afterGpsNull;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		afterGpsNull = i.getBooleanExtra("isGpsError", false);
		this.pd = new ProgressDialog(this);
		
		if (!afterGpsNull){
			setContentView(R.layout.buslines_list_screen);

			// Get list of lines from OPEN CV
			linesList = i.getIntegerArrayListExtra("linesList");
	
		
			// Show the list of lines
			this.lineRowAdapter = new LineRowAdapter(this, R.layout.row_bus_lines,
					linesList);
			setListAdapter(this.lineRowAdapter);
		}
		//after GPS was null - don't take data from the user
		else{
			//get previous user's session results
			int prevLineNumber = i.getIntExtra("lineNumber", -1);
			String prevTime = i.getStringExtra("time");
			int prevTimeInterval = i.getIntExtra("timeInterval", -1);
			
			//attempt to get GPS coordinates again
			GpsResult res = DataCollector.getGpsCoordinates(this);
			Double lat = res.getLat();
			Double lng = res.getLng();
			
			//check GPS coordinates are not null and handle
			if (lat != null || lng != null){
				HttpCaller.getDepartureTime(this, pd, prevLineNumber, lat, lng, prevTime, prevTimeInterval);
				finish();
			}
			else{
				ErrorsHandler.createNullGpsCoordinatesErrorAlert(this,prevLineNumber, prevTime, prevTimeInterval);
			}

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
		if (!DEBUG_MODE){
			if (lat != null || lng != null){
				HttpCaller.getDepartureTime(this, pd, line_number, lat, lng, time, timeInterval);
				finish();
			}
			else{
				ErrorsHandler.createNullGpsCoordinatesErrorAlert(this,line_number, time, timeInterval);
			}
		} else {
			 //for emulator
			HttpCaller.getDepartureTime(this, pd, line_number, 32.045816, 34.756983,"16:20:00", timeInterval);
		}

	}



}
