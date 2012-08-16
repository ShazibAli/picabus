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
import android.widget.TextView;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.enitities.Company;
import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.Trip;

//import com.zdm.picabus.logic.DataObject.TripObject;

public class ResultBusArrivalActivity extends ListActivity {

	private ArrayList<String> arrivalTimesList = null;
	private ArrivalRowAdapter arrivalRowAdapter;
	ProgressDialog pd;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_busarrival_screen);

		// saving reference to the context of this activity for the async task
		this.context = this;
		arrivalTimesList = new ArrayList<String>();
		Intent i = getIntent();
		Line lineDataModel = (Line) i.getSerializableExtra("lineDataModel");
		int directionChoice = (int) i.getIntExtra("direction", 9);
		String destination = (String) i.getStringExtra("destination");
		if (lineDataModel==null){
			//TODO
		}
		else{

		//Manage data and get arrival times list from data
		List<Trip> trips = (List<Trip>) lineDataModel.getTrips();
		final Trip firstTrip = trips.get(0);
		for (Iterator<Trip> iterator = trips.iterator(); iterator.hasNext();) {
			Trip trip = (Trip) iterator.next();
			if ((trip.getDirectionID()==directionChoice) || (lineDataModel.isBiDirectional()==false)){
			arrivalTimesList.add(trip.getEta());
			}
		}
		//update fields from data results:
		// line
		TextView textViewLine = (TextView) findViewById(R.id.textViewLine);
		textViewLine.setText("Line number: "+firstTrip.getLineNumber());
		
		// company
		ImageView companyImage = (ImageView) findViewById(R.id.iconCompany);
		if (getCompanyByString(firstTrip.getCompany())== getCompanyByString(Company.DAN)){
			companyImage.setImageResource(R.drawable.dan_icon);
		}
		else if (getCompanyByString(firstTrip.getCompany())== getCompanyByString(Company.EGGED)){
			companyImage.setImageResource(R.drawable.egged_icon);
		}
		else if (getCompanyByString(firstTrip.getCompany())== getCompanyByString(Company.METROPOLIN)){
			companyImage.setImageResource(R.drawable.metropoline_icon);
		}
		else{
			companyImage.setImageResource(R.drawable.line_row_bus); 
		}
		
		// station
		TextView textViewStation = (TextView) findViewById(R.id.textViewStation);
		textViewStation.setText("Station name: "+lineDataModel.getStopHeadsign());
		
		// getRoute
		ImageView routeImage = (ImageView) findViewById(R.id.getRouteIcon);
		routeImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {	
				HttpCaller.getRouteDetails(context, pd, firstTrip.getStopSequence(), firstTrip.getTripID());
			}
		});
		// last stop
		TextView textViewLastStop = (TextView) findViewById(R.id.textViewLastStop);
		textViewLastStop.setText("Last stop: "+destination);

		this.arrivalRowAdapter = new ArrivalRowAdapter(this,
				R.layout.row_arrival_time, arrivalTimesList);
		setListAdapter(this.arrivalRowAdapter);
		}
	}

	private String getCompanyByString(Company company) {
		// TODO Auto-generated method stub
		return null;
	}


}
