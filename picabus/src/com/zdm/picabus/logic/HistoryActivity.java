package com.zdm.picabus.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.connectivity.IHttpCaller;
import com.zdm.picabus.enitities.Company;
import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.Trip;
import com.zdm.picabus.localdatastorage.LocalStorageServices;
import com.zdm.picabus.localdatastorage.UserHistory;
import com.zdm.picabus.locationservices.GpsResult;
import com.zdm.picabus.utilities.DataCollector;
import com.zdm.picabus.utilities.ErrorsHandler;


public class HistoryActivity extends ListActivity {

	private ArrayList<UserHistory> historyList = null;
	private ArrayList<Integer> linesList = new ArrayList<Integer>();;
	private ListAdapter lineRowAdapter;
	LocalStorageServices storage;
	UserHistory item;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_screen);
		
		storage = new LocalStorageServices();
		
		
		/*	
		historyList = storage.getUserHistory(this);
		
	
		for(UserHistory hist: historyList)
		{
			linesList.add(hist.getLineNumber());
		} 
		*/
		linesList.add(25);
		linesList.add(26);
		linesList.add(27);
		
		
		// Show the list of lines
		this.lineRowAdapter = new LineRowAdapter(this, R.layout.history_screen, linesList);
		setListAdapter(this.lineRowAdapter);
		
		//saveUserHistory(item, context);
	}
	
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Collection<Trip> trips = null;
		String stopHeadsign = null;
		Trip trip = null;
		String comp;
		IHttpCaller ihc = null;
		UserHistory hist = null;
		int line_number = (Integer) this.lineRowAdapter.getItem(position);
		Line lineDataModel = null;
		ProgressDialog pd;
		
		ihc = HttpCaller.getInstance();
		
		for(UserHistory histItem: historyList)
		{
			if(histItem.getLineNumber() == line_number)
			{
				hist = histItem;
				break;
			}
		} 
		
		
	
		/*eta
		tripID	
		stopSequence, routeID, serviceID
			*/
		/*			comp = hist.getCompanyName();
		trip = new Trip(tripID, destination, hist.getDirection(), hist.getLineNumber(), eta, Company.getCompanyByString(comp), hist.getStopID(), stopSequence, routeID, serviceID);
		lineDataModel = new Line(false, trips, stopHeadsign);
		
		// Pass the line number to lines list intent
		List<Integer> linesList = new ArrayList<Integer>();
		linesList.add(line_number);
		// open new activity
		Intent intent = new Intent(
				"com.zdm.picabus.logic.ResultBusArrivalActivity");
		intent.putIntegerArrayListExtra("lineDataModel",
				Line lineDataModel);
		intent.put
		startActivity(intent);

	}
		
		
		Double lat = null;		
		Double lng = null;
		// Get current time
		String time = DataCollector.getCurrentTime();

		// Get coordinates
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
			// for emulator
			ihc.getDepartureTime(this, pd, line_number, 32.045816, 34.756983,
					"08:20:00", timeInterval);
		}
*/
	}

*/
	
	
	}	
}
