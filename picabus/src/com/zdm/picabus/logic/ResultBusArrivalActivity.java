package com.zdm.picabus.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

	public static final String PREFS_NAME = "resultDataPfers";
	
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
			// open null results activity and finish current
			Intent resultsIntent = new Intent(
					"com.zdm.picabus.logic.EmptyBusResultsActivity");
			this.context.startActivity(resultsIntent);
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
		textViewLine.setText(Integer.toString(firstTrip.getLineNumber()));

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
		textViewStation.setText(
				lineDataModel.getStopHeadsign());

		// Set last stop
		textViewLastStop = (TextView) findViewById(R.id.textViewLastStop);
		textViewLastStop.setText(destination);

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

		//check if allowed to enter trip manager for curr trip
		if (CanEnterTripManager(res.getTripId())){
			Intent intent = new Intent ("com.zdm.picabus.logic.TripManagerActivity");
			intent.putExtra("Data", res);
			startActivity(intent);	
		}
		else{
			  Toast toast = Toast.makeText(context,
			  context.getResources().getString(R.string.not_allowed_enter_trip)
			  ,Toast.LENGTH_LONG);
			  toast.show();
		}

	}

	private String getCompanyByString(Company company) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param tripId
	 * @return true if user is allowed to enter trip 'tripId', otherwise false
	 * user is allowed only if not checked in to any trip, or if checked in to 'tripId'
	 */
	private boolean CanEnterTripManager(long tripId){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		
		//is checked in somewhere
		boolean checkedInSomeTrip = settings.getBoolean("CheckinButton", false); 
		
		if (checkedInSomeTrip){
			long checkedIntripId = settings.getLong("tripId", -1); 
			if (tripId==checkedIntripId) {
				return true; //checked in for curr trip
			}
			else{
				return false;//checked in on another trip
			}
		}
		else{ 
			return true; //not checked in for any trip
		}

	}

}
