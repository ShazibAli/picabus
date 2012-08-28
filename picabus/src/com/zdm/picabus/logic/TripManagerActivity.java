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
import android.content.SharedPreferences;
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
import com.zdm.picabus.utilities.ErrorsHandler;

public class TripManagerActivity extends Activity {

	private final static boolean DEBUG_MODE = true;
	static final int NOTIFICATION_UNIQUE_ID = 139874;
	public static final String PREFS_NAME = "resultDataPfers";
	Context c;
	ImageButton routeImage;
	ToggleButton notificationToggle;
	TextView checkinInstructionText;
	TextView notifyInstructionText;
	TextView lineAndArrivalTop;
	Button checkinButton;
	boolean checkedIn = false;
	EditText reportEditText;
	Button submitReportButton;
	boolean userLoggedIn;
	TripResultObject tripRes;
	IHttpCaller ihc = null;
	ProgressDialog pd;// ?
	//boolean arrivedFromNotification;
	String userId;
	boolean previouslyCheckedInCurrTrip=false;
	Timer timer;// timer for notification
	Intent currIntent;
	
	boolean retFromNotificationAndCheckedOut;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.results_notify_checkin_route_screen);
		c = this;
		currIntent = getIntent();
		// check if arrived from notification click
/*		arrivedFromNotification = currIntent.getBooleanExtra(
				"fromNotification", false);*/
		
		pd = new ProgressDialog(this);
		
		ihc = HttpCaller.getInstance();

		//check if the user is checked in on that trip
		previouslyCheckedInCurrTrip = isPreviouslyCheckedInCurrTrip();
		
		//check if user has checked out and than clicked notification
		retFromNotificationAndCheckedOut = isRetFromNotificationAndCheckedOut();
		if (retFromNotificationAndCheckedOut){
			finish();
		}
		
		
		if (previouslyCheckedInCurrTrip){
			checkedIn=true;
		}
		
		// get facebook user ID
		if (previouslyCheckedInCurrTrip){
			setUserIdIfCheckedIn();
		}
/*		if (arrivedFromNotification) {
			userId = currIntent.getStringExtra("facebookId");
		}*/
		if (userId == null) {
			PicabusFacebookObject pfo = PicabusFacebookObject.getFacebookInstance();
			userId = pfo.getFacebookId();
		}

		// Check if user logged in
		userLoggedIn = userId != null;

		// get trip data, either from preferences or prev intent
		
		//if checked in on that trip
		if (previouslyCheckedInCurrTrip){
			setDataIfCheckedIn();
		}
		// else get trip from previous activity
		else if (!retFromNotificationAndCheckedOut){
			tripRes = (TripResultObject) currIntent.getSerializableExtra("Data");
		}

		// set UI
		lineAndArrivalTop = (TextView) findViewById(R.id.lineAndArrivalTop);
		notificationToggle = (ToggleButton) findViewById(R.id.notifyToggle);
		checkinInstructionText = (TextView) findViewById(R.id.checkinInstructionText);
		notifyInstructionText = (TextView) findViewById(R.id.notifyInst);
		checkinButton = (Button) findViewById(R.id.checkinButton);
		routeImage = (ImageButton) findViewById(R.id.getRouteIcon);
		reportEditText = (EditText) findViewById(R.id.reportEditText);
		submitReportButton = (Button) findViewById(R.id.submitReportBtn);



		//set all UI and functionality
		setLineAndArrivalTop();
		setNotificationToggle();
		setCheckinButton();
		setTextReportsUI();
		setGetRouteLink();
	}

	private boolean isRetFromNotificationAndCheckedOut() {
		
		boolean arrivedFromNotification = currIntent.getBooleanExtra("fromCheckinNotification", false);
		if (arrivedFromNotification){
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			boolean checkInSomeTrip = settings.getBoolean("CheckinButton", false); 
			if (!checkInSomeTrip){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}

	@Override
	protected void onPause() {

		super.onPause();
		
		if (checkedIn){
		// save trip data SharedPreferences settings =
		  SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
		  SharedPreferences.Editor editor = settings.edit(); 
		  //set data
		  editor.putInt("lineNumber", tripRes.getLineNumber());
		  editor.putString("stationName", tripRes.getStationName());
		  editor.putLong("tripId", tripRes.getTripId());
		  editor.putString("arrivalTime", tripRes.getArrivalTime());
		  editor.putInt("stopSequence", tripRes.getStopSequence());
		  
		  // save page user changes 
		  editor.putBoolean("notificationToggleChecked",notificationToggle.isChecked()); 
		  editor.putBoolean("CheckinButton",checkedIn);
		  
		  //save user Id
		  editor.putString("facebookId", userId);
		  
		  // Commit the edits 
		  editor.commit();
		}
	}
	
	private boolean isPreviouslyCheckedInCurrTrip(){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		boolean checkInSomeTrip = settings.getBoolean("CheckinButton", false); 
		return checkInSomeTrip;
	}
	
	private void setDataIfCheckedIn(){
		
			  SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			  
			  int lineNumber = settings.getInt("lineNumber", -1); 
			  String stationName = settings.getString("stationName", null); 
			  long tripId = settings.getLong("tripId", -1); 
			  String arrivalTime =  settings.getString("arrivalTime", null); 
			  int stopSequence = settings.getInt("stopSequence", -1);
		  
		  tripRes = new TripResultObject(lineNumber, stationName, tripId,arrivalTime, stopSequence); 
		  }
		 
	private void setNotificationStateIfCheckedIn() {
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		boolean notificationState = settings.getBoolean("notificationToggleChecked", false);
		if (notificationState){
			notificationToggle.setChecked(true);
		}
		notificationToggle.setEnabled(false);
	}
	
	
	private void setUserIdIfCheckedIn(){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		String id = settings.getString("facebookId", null);
		userId=id;
	}
	
	/**
	 * headline of page, contains line and arrival time
	 */
	private void setLineAndArrivalTop() {
		int lineNum = tripRes.getLineNumber();
		String time = 	tripRes.getArrivalTime();
		lineAndArrivalTop.setText("Line "+lineNum+" will arrive at "+time);
	}

	/**
	 * for notification - save activity state - UI elements
	 */
/*	private void getUiChangesFromPref(Intent intent) {

		// if came from notification click
		if (arrivedFromNotification) {
			checkedIn = intent.getBooleanExtra("CheckinButton", false);
		}

		// else - take from shared preferences
		
		 * SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		 * checkedIn = settings.getBoolean("CheckinButton", false); if
		 * (settings.getBoolean("notificationToggleChecked", false)) {
		 * notificationToggle.setChecked(true); }
		 

	}*/

	/**
	 * for notification - save activity state - trip data elements
	 */
/*	private void getTripResultFromPref(Intent intent) {

		// if came from notification click
		tripRes = (TripResultObject) intent
				.getSerializableExtra("notificationTripData");
		// else
		
		 * if (tripRes == null) { SharedPreferences settings =
		 * getSharedPreferences(PREFS_NAME, 0); int lineNumber =
		 * settings.getInt("lineNumber", -1); String stationName =
		 * settings.getString("stationName", null); long tripId =
		 * settings.getLong("tripId", -1); String arrivalTime =
		 * settings.getString("arrivalTime", null); int stopSequence =
		 * settings.getInt("stopSequence", -1);
		 * 
		 * tripRes = new TripResultObject(lineNumber, stationName, tripId,
		 * arrivalTime, stopSequence); }
		 
	}*/

	/**
	 * Set the link to get route (map image)
	 */
	private void setGetRouteLink() {
		// Set getRoute image
		routeImage.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				ihc.getRouteDetails(c, pd, tripRes.getStopSequence(),
						tripRes.getTripId());
			}
		});

	}

	/**
	 * Set UI for text reports
	 */
	private void setTextReportsUI() {
		// set report edit text and submit button not activated, unless checked in
		if (checkedIn){
			reportEditText.setEnabled(true);
			submitReportButton.setEnabled(true);
		}
		else{
			reportEditText.setEnabled(false);
			submitReportButton.setEnabled(false);
		}
		
		submitReportButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Get data that was written
				String reportText = reportEditText.getText().toString();
				if (reportText.length()==0 || reportText.length()>200){
					Toast toast = Toast.makeText(c,
							"Please enter text, limited up to 200 characters",
							Toast.LENGTH_LONG);
					toast.show();
				}
				ihc.reportTripDescription(c, pd, userId, tripRes.getTripId(),
						reportText);
			}
		});

	}

	/**
	 * Set UI and functionality for checkin button
	 */
	private void setCheckinButton() {
		
		if (checkedIn){
			checkinButton.setText("Checkout bus");			 
			checkinInstructionText
					.setText("Click to checkout from bus");
		}
		
		checkinButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				double lat = 0;
				double lng = 0;
				GpsResult res;

				if (!userLoggedIn) {
					ErrorsHandler.createNotLoggedInResultPageError(c);
				}

				else {
					if (checkedIn == false) {
						checkedIn = true;
						checkinButton.setText("Checkout bus");
 
						checkinInstructionText
								.setText("Click to checkout from bus");

						// activate report parts of page
						reportEditText.setEnabled(true);
						submitReportButton.setEnabled(true);

						res = DataCollector.getGpsCoordinates(c);
						if (res != null) {
							lat = res.getLat();
							lng = res.getLng();
						} else if (DEBUG_MODE) {
							lat = 32.045816;
							lng = 34.756983;
						}
						
						createCheckinNotification();
						
						ihc.reportCheckin(c, pd, userId, lng, lat,
								tripRes.getTripId());
						// TODO:start background service

					} else {
						checkedIn=false;
						checkinButton.setEnabled(false);
						reportEditText.setEnabled(false);
						submitReportButton.setEnabled(false);
						//save in shared pref that user is not checked in
						  SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0); 
						  SharedPreferences.Editor editor = settings.edit(); 
						  editor.putBoolean("CheckinButton",checkedIn);
						  // Commit the edits 
						  editor.commit();
						  
						  //send checkout request to server
						ihc.reportCheckout(c, pd, userId, tripRes.getTripId());
						// TODO:start background service
					}

				}
			}
		});

	}

	/**
	 * Set notifications UI and functionality
	 */
	private void setNotificationToggle() {
		// set notificationToggle
		notificationToggle.setTextOn("On");
		notificationToggle.setTextOff("Off");

		// restore if was previously set on that trip
		if (previouslyCheckedInCurrTrip){
			setNotificationStateIfCheckedIn();
		}
/*		if (arrivedFromNotification) {
			notificationToggle.setChecked(true);
			notificationToggle.setEnabled(false);
		}*/
		// set click listener
		notificationToggle
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {

						if (isChecked) {
							Toast toast = Toast.makeText(c,
									"You will be notified before bus arrives",
									Toast.LENGTH_LONG);
							toast.show();
							createNotification(tripRes.getArrivalTime(),
									tripRes.getLineNumber(),
									tripRes.getStationName(), 5);
						} else {
							// notification canceled
							Toast toast = Toast.makeText(c,
									"Notification is cancelled", Toast.LENGTH_LONG);
							toast.show();
							cancelNotification();
						}
					}
				});

	}

	/**
	 * 
	 * @param arrivalTime
	 *            - of the bus in format XX:XX
	 * @param notificationDelta
	 *            - in ms - time user chose to be notified before bus arrives
	 * @return time in ms until notification should be set
	 */
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
			e.printStackTrace();
		}

		if ((arrivalDate != null) && (currDate != null)) {
			diffInMs = arrivalDate.getTime() - currDate.getTime();
			diffInMs = diffInMs - ((long) notificationDelta * 60 * 1000);
		}
		return diffInMs;

	}

	/**
	 * Create a notification for bus arrival
	 * 
	 * @param arrivalTime
	 * @param lineNumber
	 * @param stopName
	 * @param notificationDelta
	 */
	private void createNotification(String arrivalTime, final int lineNumber,
			final String stopName, final int notificationDelta) {

		long msUntilNotification;
		// get time until notification in ms

		msUntilNotification = getMsUntilNotification(arrivalTime,
				notificationDelta);

		// if in debug and arrival time was earlier, result is negative-fix it
		if (msUntilNotification < 0) {
			if (DEBUG_MODE) {
				msUntilNotification = 10000;
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

	/**
	 * triggers the notification defined in createNotification function
	 * 
	 * @param lineNumber
	 * @param stopName
	 * @param notificationDelta
	 */
	private void triggerNotification(int lineNumber, String stopName,
			int notificationDelta) {
		CharSequence title = "Picabus Update";
		CharSequence message = "Line " + lineNumber + " is departing from\n"
				+ stopName + "\nin " + notificationDelta + " minutes!";

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(
				R.drawable.notification_icon, "Picabus Reminder",
				System.currentTimeMillis());

		
/*		Intent notificationIntent = new Intent(this,
				TripManagerActivity.class);*/

		// put extra
/*		notificationIntent.putExtra("fromNotification", true);
		notificationIntent.putExtra("notificationTripData", tripRes);
		notificationIntent.putExtra("CheckinButton", checkedIn);
		notificationIntent.putExtra("facebookId", userId);*/

/*		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);*/

		notification.setLatestEventInfo(c, title, message, null);
		notificationManager.notify(NOTIFICATION_UNIQUE_ID, notification);

	}

	/**
	 * Cancels a notification for bus arrival, before notification started
	 */
	private void cancelNotification() {
		if (timer != null) {
			timer.cancel();
		}
	};

	
	
	private void createCheckinNotification() {

		// set notification
		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				triggerCheckinNotification();
			}
		};
		timer.schedule(timerTask, 0);

	}

	/**
	 * triggers the notification defined in createNotification function
	 * 
	 * @param lineNumber
	 * @param stopName
	 * @param notificationDelta
	 */
	private void triggerCheckinNotification() {
		CharSequence title = "Picabus Update";
		CharSequence message = "You are now checked in on the bus, \nclick here to checkout";

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(
				R.drawable.notification_icon, "Picabus Reminder",
				System.currentTimeMillis());

		Intent notificationIntent = new Intent(this,
				TripManagerActivity.class);

		notificationIntent.putExtra("fromNotificationCheckin", true);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(c, title, message, contentIntent);
		notificationManager.notify(NOTIFICATION_UNIQUE_ID, notification);

	}	
	
	
	
	
	
	
	
	
	
	/**
	 * Save data if app is killed for memory reasons
	 */
	//@Override
/*	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
		savedInstanceState.putSerializable("Data", tripRes);
	}*/

	/**
	 * Restore data if app is killed for memory resonse
	 */
/*	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
		tripRes = (TripResultObject) savedInstanceState.getSerializable("Data");
	}*/

	/**
	 * Save data if user finished activity - for notifications
	 */
	/*
	 * @Override protected void onStop() { super.onStop();
	 * 
	 * // save trip data SharedPreferences settings =
	 * getSharedPreferences(PREFS_NAME, 0); SharedPreferences.Editor editor =
	 * settings.edit(); editor.putInt("lineNumber", tripRes.getLineNumber());
	 * editor.putString("stationName", tripRes.getStationName());
	 * editor.putLong("tripId", tripRes.getTripId());
	 * editor.putString("arrivalTime", tripRes.getArrivalTime());
	 * editor.putInt("stopSequence", tripRes.getStopSequence());
	 * 
	 * // save page user changes editor.putBoolean("notificationToggleChecked",
	 * notificationToggle.isChecked()); editor.putBoolean("CheckinButton",
	 * checkedIn);
	 * 
	 * // Commit the edits editor.commit(); }
	 */
}
