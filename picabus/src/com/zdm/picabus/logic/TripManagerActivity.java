package com.zdm.picabus.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.connectivity.IHttpCaller;
import com.zdm.picabus.enitities.Report;
import com.zdm.picabus.enitities.TripResultObject;
import com.zdm.picabus.facebook.FacbookIdentity;
import com.zdm.picabus.locationservices.GpsResult;
import com.zdm.picabus.services.ReportLocationService;
import com.zdm.picabus.utilities.DataCollector;
import com.zdm.picabus.utilities.ErrorsHandler;

public class TripManagerActivity extends Activity {

	private final static boolean DEBUG_MODE = true;
	static final int REMINDER_NOTIFICATION_UNIQUE_ID = 139874;
	static final int CHECKIN_NOTIFICATION_UNIQUE_ID = 139875;
	public static final String TRIP_MANAGER_PREFS_NAME = "resultDataPfers";
	public static final String PICABUS_PREFS_NAME = "picabusSettings";

	private Context c;
	private ImageButton routeImage;
	private ToggleButton notificationToggle;
	private TextView lineAndArrivalTop;
	private Button checkinButton;
	private boolean checkedIn = false;
	private EditText reportEditText;
	private Button submitReportButton;
	private boolean userLoggedIn;
	private TripResultObject tripRes;
	private IHttpCaller ihc = null;
	private ProgressDialog pd;
	private String userId;
	private boolean previouslyCheckedInCurrTrip = false;
	private Timer timer;// timer for notification
	private Intent currIntent;
	Intent serviceIntent;

	static final String KEY_ID = "id";
	static final String KEY_REPORTER = "reporter";
	static final String KEY_REPORT = "report";
	static final String KEY_REPORT_TIME = "reportTime";
    ListView list;
    ReportsListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.results_notify_checkin_route_screen);
		c = this;
		currIntent = getIntent();
		pd = new ProgressDialog(this);
		ihc = HttpCaller.getInstance();

		// if user is checked in on that trip-set checkIn field and get userid
		previouslyCheckedInCurrTrip = isPreviouslyCheckedInCurrTrip();
		if (previouslyCheckedInCurrTrip) {
			checkedIn = true;
			setUserIdIfCheckedIn();
		}


		if (userId == null) {
			userId = FacbookIdentity.getUserId();
		}

		// Check if user logged in
		userLoggedIn = userId != null;

		// get trip data, either from preferences or prev intent
		if (previouslyCheckedInCurrTrip) {// if checked in on that trip
			setDataIfCheckedIn();
		} else {// else get trip from previous activity
			tripRes = (TripResultObject) currIntent
					.getSerializableExtra("Data");
		}

		// set UI views only
		setUiViewsById();
		// set all UI functionality
		setLineAndArrivalTop();
		setNotificationToggle();
		setCheckinButton();
		setTextReportsUI();
		setGetRouteLink();
		
		// TODO: Change to real TripId
		HttpCaller.getInstance().getTripTextualReports(10, this);
	}
	
	public void insertFacebookData(){
		 list=(ListView)findViewById(R.id.listReports);
		 
	}

	public void populateReportsList(ArrayList<Report> reports) {
		ArrayList<HashMap<String, String>> reportsList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map;
		
		Integer idCounter = 0;
		for (Report report : reports) {
			map = new HashMap<String, String>();
			// adding each child node to HashMap 
	        map.put(KEY_ID, idCounter.toString());
	        map.put(KEY_REPORTER, report.getReporterId().toString());
	        map.put(KEY_REPORT, report.getReport());
	        map.put(KEY_REPORT_TIME, report.getReportTimeString());
	        reportsList.add(map);
	        idCounter++;
		}
 
        list=(ListView)findViewById(R.id.listReports);
 
        // Getting adapter by passing xml data ArrayList
        adapter=new ReportsListAdapter(this, reportsList);
        list.setAdapter(adapter);
 
        // Click event for single list row
        list.setOnItemClickListener(new OnItemClickListener() {
 
      
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            //	Toast.makeText(c, "Item Clicked", Toast.LENGTH_LONG).show();
            }

        });
	}

	/**
	 * override onPause Save all data to shared preferences if user is checked
	 * in
	 */
	@Override
	protected void onPause() {

		super.onPause();
		if (checkedIn) {
			saveAllDataIfCheckedIn();
		}
	}

	/**
	 * Set all UI views - without their functionality
	 */
	private void setUiViewsById() {

		lineAndArrivalTop = (TextView) findViewById(R.id.lineAndArrivalTop);
		notificationToggle = (ToggleButton) findViewById(R.id.notifyToggle);
		checkinButton = (Button) findViewById(R.id.checkinButton);
		routeImage = (ImageButton) findViewById(R.id.getRouteIcon);
		reportEditText = (EditText) findViewById(R.id.reportEditText);
		submitReportButton = (Button) findViewById(R.id.submitReportBtn);
	}

	/**
	 * saved all relevant data for activity if user is checked in saves to
	 * shares preferences
	 */
	private void saveAllDataIfCheckedIn() {

		// save trip data SharedPreferences settings =
		SharedPreferences settings = getSharedPreferences(TRIP_MANAGER_PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		// set data
		editor.putInt("lineNumber", tripRes.getLineNumber());
		editor.putString("stationName", tripRes.getStationName());
		editor.putLong("tripId", tripRes.getTripId());
		editor.putString("arrivalTime", tripRes.getArrivalTime());
		editor.putInt("stopSequence", tripRes.getStopSequence());
		// save page user changes
		editor.putBoolean("notificationToggleChecked",
				notificationToggle.isChecked());
		editor.putBoolean("CheckinButton", checkedIn);

		// save user Id
		editor.putString("facebookId", userId);

		// Commit the edits
		editor.commit();

	}

	/**
	 * 
	 * @return true is user is checked in on the trip false otherwise
	 */

	private boolean isPreviouslyCheckedInCurrTrip() {
		SharedPreferences settings = getSharedPreferences(TRIP_MANAGER_PREFS_NAME, 0);
		boolean checkInSomeTrip = settings.getBoolean("CheckinButton", false);
		return checkInSomeTrip;
	}

	/**
	 * Sets activity data from shared preferences saved data in cases when user
	 * is checked in on that trip and returns to that activity
	 */
	private void setDataIfCheckedIn() {

		SharedPreferences settings = getSharedPreferences(TRIP_MANAGER_PREFS_NAME, 0);

		int lineNumber = settings.getInt("lineNumber", -1);
		String stationName = settings.getString("stationName", null);
		long tripId = settings.getLong("tripId", -1);
		String arrivalTime = settings.getString("arrivalTime", null);
		int stopSequence = settings.getInt("stopSequence", -1);

		tripRes = new TripResultObject(lineNumber, stationName, tripId,
				arrivalTime, stopSequence);
	}

	/**
	 * Sets notification toggle according to user's previous selection and
	 * disables it in cases when user is checked in on that trip and returns to
	 * that activity
	 */
	private void setNotificationStateIfCheckedIn() {

		SharedPreferences settings = getSharedPreferences(TRIP_MANAGER_PREFS_NAME, 0);
		boolean notificationState = settings.getBoolean(
				"notificationToggleChecked", false);
		if (notificationState) {
			notificationToggle.setChecked(true);
		}
		notificationToggle.setEnabled(false);
	}

	/**
	 * Sets user facebook id according to user's previous id in cases when user
	 * is checked in on that trip and returns to that activity
	 */
	private void setUserIdIfCheckedIn() {
		SharedPreferences settings = getSharedPreferences(TRIP_MANAGER_PREFS_NAME, 0);
		String id = settings.getString("facebookId", null);
		userId = id;
	}

	/**
	 * Sets headline of page, contains line and arrival time
	 */
	private void setLineAndArrivalTop() {
		int lineNum = tripRes.getLineNumber();
		String time = tripRes.getArrivalTime();
		lineAndArrivalTop
				.setText("Line " + lineNum + " will arrive at " + time);
	}

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
		// set report edit text and submit button not activated, unless checked
		// in
		if (checkedIn) {
			reportEditText.setEnabled(true);
			submitReportButton.setEnabled(true);
		} else {
			reportEditText.setEnabled(false);
			submitReportButton.setEnabled(false);
		}

		submitReportButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Get data that was written
				String reportText = reportEditText.getText().toString();
				if (reportText.length() == 0 || reportText.length() > 200) {
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

		if (checkedIn) {
			checkinButton.setBackgroundResource(R.drawable.check_out_ok);
		}
		else {
			checkinButton.setBackgroundResource(R.drawable.check_in_ok);
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
						checkinButton.setBackgroundResource(R.drawable.check_out_ok);
						
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
						serviceIntent = new Intent(getApplicationContext(), ReportLocationService.class);
						serviceIntent.putExtra("userId", Long.valueOf(userId));
						serviceIntent.putExtra("tripId", tripRes.getTripId());
						startService(serviceIntent);

					} else {
						checkedIn = false;
						checkinButton.setBackgroundResource(R.drawable.check_in_ok);
						checkinButton.setEnabled(false);
						checkinButton.setText("Thanks!");
						// cancel notification
						NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
						nm.cancel(CHECKIN_NOTIFICATION_UNIQUE_ID);
						
						reportEditText.setEnabled(false);
						submitReportButton.setEnabled(false);
						// save in shared pref that user is not checked in
						SharedPreferences settings = getSharedPreferences(
								TRIP_MANAGER_PREFS_NAME, 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putBoolean("CheckinButton", checkedIn);
						// Commit the edits
						editor.commit();

						// send checkout request to server
						ihc.reportCheckout(c, pd, userId, tripRes.getTripId());
						// means that we came back from the checkin notification
						if (serviceIntent == null) {
							serviceIntent = new Intent(getApplicationContext(), ReportLocationService.class);
							serviceIntent.putExtra("userId", Long.valueOf(userId));
							serviceIntent.putExtra("tripId", tripRes.getTripId());
						}
						stopService(serviceIntent);

					}

				}
			}
		});

	}

	/**
	 * Set notifications UI and functionality
	 */
	private void setNotificationToggle() {

		// restore if was previously set on that trip
		if (previouslyCheckedInCurrTrip) {
			setNotificationStateIfCheckedIn();
		}
 
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
							
							//get notification delta (in seconds) from preferences
							SharedPreferences settings = getSharedPreferences(PICABUS_PREFS_NAME, 0);
							int notificationDelta = settings.getInt("notificationDelta",10);
							
							createNotification(tripRes.getArrivalTime(),
									tripRes.getLineNumber(),
									tripRes.getStationName(), notificationDelta);
						} else {
							// notification canceled
							Toast toast = Toast.makeText(c,
									"Notification is cancelled",
									Toast.LENGTH_LONG);
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
	 *            - in seconds - time user chose to be notified before bus arrives
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
			diffInMs = diffInMs - ((long) notificationDelta * 1000);
		}
		return diffInMs;

	}

	/**
	 * Create a notification for bus arrival
	 * 
	 * @param arrivalTime - time when bus is about to arrive
	 * @param lineNumber
	 * @param stopName
	 * @param notificationDelta - in seconds - time that user chose
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
	@SuppressWarnings("deprecation")
	private void triggerNotification(int lineNumber, String stopName,
			int notificationDelta) {
		CharSequence title = "Picabus Update";
		CharSequence message = "Line " + lineNumber + " is departing in " + notificationDelta + " seconds!";

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(
				R.drawable.notification_icon, "Picabus Reminder",
				System.currentTimeMillis());

		notification.setLatestEventInfo(c, title, message, null);
		notificationManager.notify(REMINDER_NOTIFICATION_UNIQUE_ID, notification);
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
	@SuppressWarnings("deprecation")
	private void triggerCheckinNotification() {
		CharSequence title = "Picabus Update";
		CharSequence message = "You are now checked in on the bus, \nclick here to checkout";

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(
				R.drawable.notification_icon, "Picabus Reminder",
				System.currentTimeMillis());

		Intent notificationIntent = new Intent(this, TripManagerActivity.class);

		notificationIntent.putExtra("fromNotificationCheckin", true);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(c, title, message, contentIntent);
		notificationManager.notify(CHECKIN_NOTIFICATION_UNIQUE_ID, notification);

	}

}
