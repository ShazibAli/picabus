package com.zdm.picabus.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdm.picabus.R;
import com.zdm.picabus.enitities.Company;
import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.Trip;

//import com.zdm.picabus.logic.DataObject.TripObject;

public class ResultBusArrivalActivity extends ListActivity {

	private ArrayList<String> arrivalTimesList = null;
	private ArrivalRowAdapter arrivalRowAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_busarrival_screen);

		arrivalTimesList = new ArrayList<String>();
		Intent i = getIntent();
		Line lineDataModel = (Line) i.getSerializableExtra("lineDataModel");
		int directionChoice = (int) i.getIntExtra("direction", 9);

		if (lineDataModel==null){
			//TODO
		}
		else{

		//Manage data and get arrival times list from data
		List<Trip> trips = (List<Trip>) lineDataModel.getTrips();
		Trip firstTrip = trips.get(0);
		for (Iterator<Trip> iterator = trips.iterator(); iterator.hasNext();) {
			Trip trip = (Trip) iterator.next();
			if ((trip.getDirectionID()==directionChoice) || (lineDataModel.isBiDirectional()==false)){
			arrivalTimesList.add(trip.getEta().toString());
			}
		}
		//update fields from data results:
		// line
		TextView textViewLine = (TextView) findViewById(R.id.textViewLine);
		textViewLine.setText("Line number: "+firstTrip.getLineNumber());
		
		// company
		ImageView companyImage = (ImageView) findViewById(R.id.iconCompany);
		if (firstTrip.getCompany() == Company.DAN){
			companyImage.setImageResource(R.drawable.dan_icon);
		}
		else if (firstTrip.getCompany() == Company.EGGED){
			companyImage.setImageResource(R.drawable.egged_icon);
		}
		else if (firstTrip.getCompany() == Company.METROPOLIN){
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
			}
		});
		// last stop - //TODO - parse inside stop sequence, according to direction
		TextView textViewLastStop = (TextView) findViewById(R.id.textViewLastStop);
		if (directionChoice == 0)
			textViewLastStop.setText("Last stop: "+firstTrip.getDestination());
		else if (directionChoice == 1)
			textViewLastStop.setText("Last stop: "+firstTrip.getDestination());

		this.arrivalRowAdapter = new ArrivalRowAdapter(this,
				R.layout.row_arrival_time, arrivalTimesList);
		setListAdapter(this.arrivalRowAdapter);
		}
	}


}
