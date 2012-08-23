package com.zdm.picabus.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.connectivity.IHttpCaller;
import com.zdm.picabus.enitities.Company;
import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.Trip;

/**
 * 
 * Activity for displaying result page for the main request of line arrival
 * time, company, station, map etc either from camera or the manual search
 * 
 */
public class ResultBusArrivalActivity extends ListActivity {

	private ArrayList<String> arrivalTimesList = null;
	private ArrivalRowAdapter arrivalRowAdapter;
	ProgressDialog pd;
	Context context;
	static final int NOTIFICATION_UNIQUE_ID = 139874;

	TextView textViewLine;
	ImageView companyImage;
	TextView textViewStation;
	TextView textViewLastStop;
	ImageView routeImage;

	Line lineDataModel;
	int directionChoice;
	String destination;
	List<Trip> trips;
	Trip firstTrip;
	IHttpCaller ihc = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		ihc = HttpCaller.getInstance();
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

			// get extra directions and destination data
			directionChoice = (int) i.getIntExtra("direction", 9);
			destination = (String) i.getStringExtra("destination");

			trips = (List<Trip>) lineDataModel.getTrips();
			firstTrip = trips.get(0);

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
	private ArrayList<String> CreateArrivalTimesList() {

		arrivalTimesList = new ArrayList<String>();
		for (Iterator<Trip> iterator = trips.iterator(); iterator.hasNext();) {
			Trip trip = (Trip) iterator.next();
			if ((trip.getDirectionID() == directionChoice)
					|| (lineDataModel.isBiDirectional() == false)) {
				arrivalTimesList.add(trip.getEta());
			}
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
		textViewLine.setText("Line number: " + firstTrip.getLineNumber());

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

		// Set getRoute image
		routeImage = (ImageView) findViewById(R.id.getRouteIcon);
		routeImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// creating notification Timer Task TODO: integrate in the
				// right place
				// createNotification(10000, 10, "some stop", 5);
				ihc.getRouteDetails(context, pd,
						firstTrip.getStopSequence(), firstTrip.getTripID());
			}
		});

		// Set arrival times list
		this.arrivalRowAdapter = new ArrivalRowAdapter(this,
				R.layout.row_arrival_time, arrivalTimesList);
		setListAdapter(this.arrivalRowAdapter);
	}

	
	
	/**
	 * Arrival time list clickHandler - Activates notifications
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String arrivalTime = this.arrivalRowAdapter.getItem(position);
		// TODO: set notification on that arrival time!
	}

	private String getCompanyByString(Company company) {
		// TODO Auto-generated method stub
		return null;
	}

	private void createNotification(long ms, final int lineNumber,
			final String stopName, final int notificationDelta) {
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				triggerNotification(lineNumber, stopName, notificationDelta);
			}
		};
		timer.schedule(timerTask, ms);
	}

	private void triggerNotification(int lineNumber, String stopName,
			int notificationDelta) {
		CharSequence title = "Picabus Update";
		CharSequence message = "Line " + lineNumber + " is departing from "
				+ stopName + " in " + notificationDelta + " minutes!";

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(
				R.drawable.notification_icon, "Picabus Reminder",
				System.currentTimeMillis());

		Intent notificationIntent = new Intent(this, MainScreenActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(ResultBusArrivalActivity.this, title,
				message, pendingIntent);
		notificationManager.notify(NOTIFICATION_UNIQUE_ID, notification);

	}

}
