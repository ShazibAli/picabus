package com.zdm.picabus.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zdm.picabus.R;
import com.zdm.picabus.enitities.Company;
import com.zdm.picabus.enitities.Destination;
import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.Trip;
import com.zdm.picabus.enitities.TripResultObject;
import com.zdm.picabus.utilities.DestinationParser;

/**
 * 
 * Activity for displaying result page for the main request of line arrival
 * time, company, station, map etc either from camera or the manual search
 * 
 */
public class ResultBusArrivalActivity extends ListActivity {

	private ArrayList<TripResultObject> arrivalTimesList = null;
	private ArrivalRowAdapter arrivalRowAdapter;
	ProgressDialog pd;
	Context context;


	TextView textViewLine;
	ImageView companyImage;
	TextView textViewStation;
	TextView textViewLastStop;
	ImageView routeImage;

	Line lineDataModel;
	String destination;
	List<Trip> trips;
	Trip firstTrip;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		// Get main line data
		Intent i = getIntent();
		lineDataModel = (Line) i.getSerializableExtra("lineDataModel");

		// null result from server
		if (lineDataModel == null) {
			// open null results activity
			Intent resultsIntent = new Intent(
					"com.zdm.picabus.logic.EmptyBusResultsActivity");
			this.context.startActivity(resultsIntent);
			// finish current activity
			finish();

		}
		// result from server contains data
		else {
			setContentView(R.layout.result_busarrival_screen);

			// save reference to context of this activity for the async task
			this.context = this;

			//get rest of data needed
			trips = (List<Trip>) lineDataModel.getTrips();
			firstTrip = trips.get(0);
			
			// Parse destination
			Destination dest = DestinationParser
					.parseDestination(firstTrip.getDestination());
			if (firstTrip.getDirectionID()==0){
				destination=dest.getDestinationA();
			}
			else{
				destination=dest.getDestinationB();
			}
			// Create a list of arrival times using the data
			arrivalTimesList = CreateArrivalTimesList();

			// update fields and UI of page from data results:
			UpdateResultsPageFields();

		}
	}

	/**
	 * Creates a list of the arrival times taken from the line's data
	 * 
	 * @return the arrival times list
	 */
	private ArrayList<TripResultObject> CreateArrivalTimesList() {

		int lineNumber;
		String stationName;
		long tripId;
		String arrivalTime;
		int stopSequence;
		
		TripResultObject tripResObject;
		
		arrivalTimesList = new ArrayList<TripResultObject>();
		for (Iterator<Trip> iterator = trips.iterator(); iterator.hasNext();) {
			Trip trip = (Trip) iterator.next();
			
			lineNumber = trip.getLineNumber();
			stationName=lineDataModel.getStopHeadsign();
			tripId = trip.getTripID();
			stopSequence=trip.getStopSequence();
			arrivalTime=trip.getEta();
			
			tripResObject = new TripResultObject(lineNumber, stationName, tripId, arrivalTime,stopSequence);

			arrivalTimesList.add(tripResObject);
		}
		return arrivalTimesList;
	}

	
	
	/**
	 * Updates all the fields in results page screen activity: Line number, bus
	 * company, station name, last stop station name, Arrival times list, and
	 * GetRoute link
	 */
	private void UpdateResultsPageFields() {
		// Set line
		textViewLine = (TextView) findViewById(R.id.textViewLine);
		textViewLine.setText("					Line " + firstTrip.getLineNumber());

		// Set company
		companyImage = (ImageView) findViewById(R.id.iconCompany);
		if (getCompanyByString(firstTrip.getCompany()) == getCompanyByString(Company.DAN)) {
			companyImage.setImageResource(R.drawable.dan_icon);
		} else if (getCompanyByString(firstTrip.getCompany()) == getCompanyByString(Company.EGGED)) {
			companyImage.setImageResource(R.drawable.egged_icon);
		} else if (getCompanyByString(firstTrip.getCompany()) == getCompanyByString(Company.METROPOLIN)) {
			companyImage.setImageResource(R.drawable.metropoline_icon);
		} else {
			companyImage.setImageResource(R.drawable.line_row_bus);
		}

		// Set station
		textViewStation = (TextView) findViewById(R.id.textViewStation);
		textViewStation.setText("Station name: "
				+ lineDataModel.getStopHeadsign());

		// Set last stop
		textViewLastStop = (TextView) findViewById(R.id.textViewLastStop);
		textViewLastStop.setText("Last stop: " + destination);

		// Set arrival times list
		this.arrivalRowAdapter = new ArrivalRowAdapter(this,
				R.layout.row_arrival_time, arrivalTimesList);
		setListAdapter(this.arrivalRowAdapter);
	}

	
	
	/**
	 * Arrival time list clickHandler - Activates notifications
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		TripResultObject res = this.arrivalRowAdapter.getItem(position);

		Intent intent = new Intent ("com.zdm.picabus.logic.ResultsExtraFeaturesActivity");
		intent.putExtra("Data", res);
		startActivity(intent);
	}

	private String getCompanyByString(Company company) {
		// TODO Auto-generated method stub
		return null;
	}


}
