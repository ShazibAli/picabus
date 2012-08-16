package com.zdm.picabus.logic;

import java.util.ArrayList;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.locationservices.GpsResult;
import com.zdm.picabus.utilities.DataCollector;

public class BusLinesListActivity extends ListActivity {

	private final static boolean DEBUG_MODE = true;

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
			 //for emulator
			// show loading
			HttpCaller.getDepartureTime(this, pd, line_number, 32.045816, 34.756983,"16:20:00", 15);
		}

	}



}
