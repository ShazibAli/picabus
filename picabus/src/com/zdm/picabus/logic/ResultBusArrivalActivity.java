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
	static final int NOTIFICATION_UNIQUE_ID = 139874;

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
		if (lineDataModel == null) {
			// TODO
		} else {

			// Manage data and get arrival times list from data
			List<Trip> trips = (List<Trip>) lineDataModel.getTrips();
			final Trip firstTrip = trips.get(0);
			for (Iterator<Trip> iterator = trips.iterator(); iterator.hasNext();) {
				Trip trip = (Trip) iterator.next();
				if ((trip.getDirectionID() == directionChoice)
						|| (lineDataModel.isBiDirectional() == false)) {
					arrivalTimesList.add(trip.getEta());
				}
			}
			// update fields from data results:
			// line
			TextView textViewLine = (TextView) findViewById(R.id.textViewLine);
			textViewLine.setText("Line number: " + firstTrip.getLineNumber());

			// company
			ImageView companyImage = (ImageView) findViewById(R.id.iconCompany);
			if (getCompanyByString(firstTrip.getCompany()) == getCompanyByString(Company.DAN)) {
				companyImage.setImageResource(R.drawable.dan_icon);
			} else if (getCompanyByString(firstTrip.getCompany()) == getCompanyByString(Company.EGGED)) {
				companyImage.setImageResource(R.drawable.egged_icon);
			} else if (getCompanyByString(firstTrip.getCompany()) == getCompanyByString(Company.METROPOLIN)) {
				companyImage.setImageResource(R.drawable.metropoline_icon);
			} else {
				companyImage.setImageResource(R.drawable.line_row_bus);
			}

			// station
			TextView textViewStation = (TextView) findViewById(R.id.textViewStation);
			textViewStation.setText("Station name: "
					+ lineDataModel.getStopHeadsign());

			// getRoute
			ImageView routeImage = (ImageView) findViewById(R.id.getRouteIcon);
			routeImage.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// creating notification Timer Task TODO: integrate in the
					// right place
					createNotification(10000, 10, "some stop", 5);
					HttpCaller.getRouteDetails(context, pd,
							firstTrip.getStopSequence(), firstTrip.getTripID());
				}
			});
			// last stop
			TextView textViewLastStop = (TextView) findViewById(R.id.textViewLastStop);
			textViewLastStop.setText("Last stop: " + destination);

			this.arrivalRowAdapter = new ArrivalRowAdapter(this,
					R.layout.row_arrival_time, arrivalTimesList);
			setListAdapter(this.arrivalRowAdapter);
		}
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
