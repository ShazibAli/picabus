package com.zdm.picabus.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.connectivity.IHttpCaller;
import com.zdm.picabus.enitities.TripResultObject;
import com.zdm.picabus.facebook.PicabusFacebookObject;
import com.zdm.picabus.locationservices.GpsResult;
import com.zdm.picabus.utilities.DataCollector;

public class ResultsExtraFeaturesActivity extends Activity {

	private final static boolean DEBUG_MODE = true;
	static final int NOTIFICATION_UNIQUE_ID = 139874;
	Context c;
	ImageButton routeImage;
	ToggleButton notificationToggle;
	TextView checkinInstructionText;
	TextView notifyInstructionText;
	Button checkinButton;
	Boolean checkedIn = false;
	EditText reportEditText;
	Button submitReportButton;

	TripResultObject tripRes;
	IHttpCaller ihc = null;
	ProgressDialog pd;// ?

	String userId;

	Timer timer;// timer for notification

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.results_notify_checkin_route_screen);
		c = this;
		ihc = HttpCaller.getInstance();
		// get facebook user ID
		PicabusFacebookObject pfo = PicabusFacebookObject.getFacebookInstance();
		userId = pfo.getFacebookId();

		Intent intent = getIntent();
		tripRes = (TripResultObject) intent.getSerializableExtra("Data");

		notificationToggle = (ToggleButton) findViewById(R.id.notifyToggle);
		checkinInstructionText = (TextView) findViewById(R.id.checkinInstructionText);
		notifyInstructionText = (TextView) findViewById(R.id.notifyInst);
		checkinButton = (Button) findViewById(R.id.checkinButton);
		routeImage = (ImageButton) findViewById(R.id.getRouteIcon);
		reportEditText = (EditText) findViewById(R.id.reportEditText);
		submitReportButton = (Button) findViewById(R.id.submitReportBtn);

		setNotificationToggle();
		setCheckinButton();
		setTextReportsUI();
		setGetRouteLink();

	}

	private void setGetRouteLink() {
		// Set getRoute image
		routeImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				ihc.getRouteDetails(c, pd, tripRes.getStopSequence(),
						tripRes.getTripId());
			}
		});

	}

	private void setTextReportsUI() {
		// set report edit text and submit button not activated
		reportEditText.setEnabled(false);
		/* reportEditText.setAlpha((float) 0.3); */
		submitReportButton.setEnabled(false);
		/* submitReportButton.setAlpha((float) 0.3); */

		submitReportButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Get data that was written
				String reportText = reportEditText.getText().toString();
				ihc.reportTripDescription(c, pd, userId, tripRes.getTripId(),
						reportText);
			}
		});

	}

	private void setCheckinButton() {
		checkinButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				double lat = 0;
				double lng = 0;
				GpsResult res;

				if (checkedIn == false) {
					checkedIn = true;
					checkinButton.setText("Checkout bus");
					/*
					 * Toast toast = Toast .makeText( c,
					 * "Reports regarding to location will be sent until you checkout from bus"
					 * , 5); toast.show();
					 */
					checkinInstructionText
							.setText("Click to checkout from bus");

					// activate report parts of page
					reportEditText.setEnabled(true);
					/* reportEditText.setAlpha((float) 1); */
					submitReportButton.setEnabled(true);
					/* submitReportButton.setAlpha((float) 1); */

					res = DataCollector.getGpsCoordinates(c);
					if (res != null) {
						lat = res.getLat();
						lng = res.getLng();
					} else if (DEBUG_MODE) {
						lat = 32.045816;
						lng = 34.756983;
					}
					ihc.reportCheckin(c, pd, userId, lng, lat,
							tripRes.getTripId());
					// TODO:start background service

				} else {
					checkinButton.setEnabled(false);
					ihc.reportCheckout(c, pd, userId, tripRes.getTripId());
					// TODO:start background service
				}

			}
		});

	}

	private void setNotificationToggle() {
		// set notificationToggle
		notificationToggle.setTextOn("On");
		notificationToggle.setTextOff("Off");
		notificationToggle
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {

						if (isChecked) {
							Toast toast = Toast.makeText(c,
									"You will be notified before bus arrives",
									5);
							toast.show();
							createNotification(tripRes.getArrivalTime(),
									tripRes.getLineNumber(),
									tripRes.getStationName(), 5);
						} else {
							// notification canceled
							Toast toast = Toast.makeText(c,
									"Notification is cancelled", 5);
							toast.show();
							cancelNotification();
						}
					}
				});

	}

	private long getMsUntilNotification(String arrivalTime,
			long notificationDelta) {

		long diffInMs = 0;
		Date arrivalDate = null;
		Date currDate = null;

		String currTime = DataCollector.getCurrentTime();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

		try {
			arrivalDate = formatter.parse(arrivalTime);
			currDate = formatter.parse(currTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if ((arrivalDate != null) && (currDate != null)) {
			diffInMs = arrivalDate.getTime() - currDate.getTime();
			diffInMs = diffInMs - ((long) notificationDelta * 60 * 1000);
		}
		return diffInMs;

	}

	private void createNotification(String arrivalTime, final int lineNumber,
			final String stopName, final int notificationDelta) {

		long msUntilNotification;
		// get time until notification in ms

		msUntilNotification = getMsUntilNotification(arrivalTime,
				notificationDelta);

		// if in debug and arrival time was earlier, result is negative-fix it
		if (msUntilNotification < 0) {
			if (DEBUG_MODE) {
				msUntilNotification = 5000;
			} else {
				return;
			}
		}

		// set notification
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				triggerNotification(lineNumber, stopName, notificationDelta);
			}
		};
		timer.schedule(timerTask, msUntilNotification);

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

		Intent notificationIntent = new Intent(this,
				ResultsExtraFeaturesActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(ResultsExtraFeaturesActivity.this,
				title, message, pendingIntent);
		notificationManager.notify(NOTIFICATION_UNIQUE_ID, notification);

	}

	private void cancelNotification() {
		if (timer != null) {
			timer.cancel();
		}
	};

}
