package com.zdm.picabus.logic;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SlidingDrawer.OnDrawerScrollListener;
import android.widget.Spinner;

import com.zdm.picabus.R;
import com.zdm.picabus.locationservices.GpsCorrdinates;
import com.zdm.picabus.utilities.ErrorsHandler;
import com.zdm.picabus.utilities.SettingsParser;

/**
 * 
 * Main menu activity
 * 
 */
public class MainScreenActivity extends Activity {

	/*
	 * Demo mode flag - global flag which indicates that we are using GPS
	 * coordinates of the DEMO bus sign
	 */
	public static final boolean DEMO_MODE = true;

	private static final int NOTIFICATION_UNIQUE_ID = 139874;
	public static final String PICABUS_PREFS_NAME = "picabusSettings";
	private ImageButton cameraBtn;
	private ImageButton historyBtn;
	private ImageButton aboutUsBtn;
	private ImageButton searchBtn;
	private ImageButton myPicabusBtn;
	private Button slideButton;
	private SlidingDrawer slidingDrawer;
	private LinearLayout slideBg;
	private NotificationManager nm;
	private GpsCorrdinates gpsObject;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_screen);

		context = this;
		// Canceling the notification - if it was raised
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(NOTIFICATION_UNIQUE_ID);

		// get GPS service and check is GPS enabled
		gpsObject = GpsCorrdinates.getGpsInstance(this);
		if (!gpsObject.isGpsEnabled()) {
			ErrorsHandler.createGpsErrorAlert(context);
		}

		// UI and listeners
		setListenersForUi();

		// Adds sliding drawer and application settings inside
		addSlidingDrawer();
		addSettingsToSlidingDrawer();
	}

	/**
	 * Set page UI and click listeners
	 */
	private void setListenersForUi() {

		// Menu Buttons
		cameraBtn = (ImageButton) findViewById(R.id.button_camera);
		historyBtn = (ImageButton) findViewById(R.id.button_history);
		aboutUsBtn = (ImageButton) findViewById(R.id.button_aboutus);
		searchBtn = (ImageButton) findViewById(R.id.button_search);
		myPicabusBtn = (ImageButton) findViewById(R.id.button_mypicabus);
		// Listeners for buttons:
		cameraBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Check GPS coordinates available, alert if disabled
				if (!gpsObject.isGpsEnabled()) {
					ErrorsHandler.createGpsErrorAlert(context);
				} else {
					// Open camera activity
					Intent intent = new Intent(
							"com.zdm.picabus.cameraservices.CameraActivity");
					startActivity(intent);
				}
			}
		});

		aboutUsBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(
						"com.zdm.picabus.logic.AboutUsActivity");
				startActivity(intent);
			}
		});

		searchBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Check GPS coordinates available, alert if disabled
				if (!gpsObject.isGpsEnabled()) {
					ErrorsHandler.createGpsErrorAlert(context);
				} else {
					// Open Free text activity
					Intent intent = new Intent(
							"com.zdm.picabus.logic.ManualSearchActivity");
					startActivity(intent);
				}
			}
		});

		historyBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(
						"com.zdm.picabus.logic.HistoryActivity");
				startActivity(intent);
			}
		});

		myPicabusBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(
						"com.zdm.picabus.facebook.MyPicabusPageActivity");
				startActivity(intent);
			}
		});

	}

	/**
	 * Add setting UI to the sliding drawer
	 */
	private void addSettingsToSlidingDrawer() {

		// inside sliding drawer
		final Spinner spNotification = (Spinner) findViewById(R.id.SpinnerNotification);
		final Spinner spTimeInterval = (Spinner) findViewById(R.id.SpinnerTimeInterval);

		// Spinner Time interval
		String intervalTimes[] = { "30 minutes", "1 hour", "2 hours",
				"5 hours", "1 day" };
		final ArrayAdapter<String> adapterInterval = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, intervalTimes);

		// get current selection if exists
		SharedPreferences settings = getSharedPreferences(PICABUS_PREFS_NAME, 0);
		int pos = settings.getInt("timeIntervalPosition", -1);

		// set adapter
		spTimeInterval.setAdapter(adapterInterval);
		if (pos != -1) {
			spTimeInterval.setSelection(pos);
		}

		// select listener
		spTimeInterval.setOnItemSelectedListener(new OnItemSelectedListener() {

			int count = 0;

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				if (count >= 1) {
					String choice = (String) parent.getItemAtPosition(pos);
					int choiceInMinutes = SettingsParser
							.ParseTimeInMinutes(choice);

					// save in shared prefs
					SharedPreferences settings = getSharedPreferences(
							PICABUS_PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("timeInterval", choiceInMinutes);
					editor.putInt("timeIntervalPosition", pos);
					editor.commit();
				}
				count++;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// Spinner Notification times
		String notificationTimes[] = { "10 seconds", "30 seconds", "1 minute",
				"5 minutes", "10 minutes", "15 minutes", "30 minutes", "1 hour" };
		final ArrayAdapter<String> adapterNotification = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, notificationTimes);
		// get current selection if exists
		settings = getSharedPreferences(PICABUS_PREFS_NAME, 0);

		// set adapter
		spNotification.setAdapter(adapterNotification);
		pos = settings.getInt("notificationDeltaPosition", -1);
		if (pos != -1) {
			spNotification.setSelection(pos);
		}
		// select listener
		spNotification.setOnItemSelectedListener(new OnItemSelectedListener() {
			int count = 0;

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				if (count >= 1) {
					String choice = (String) parent.getItemAtPosition(pos);
					int choiceInSeconds = SettingsParser
							.ParseTimeInSeconds(choice);

					// save in shared prefs
					SharedPreferences settings = getSharedPreferences(
							PICABUS_PREFS_NAME, 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putInt("notificationDelta", choiceInSeconds);
					editor.putInt("notificationDeltaPosition", pos);
					editor.commit();
				}
				count++;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

	}

	/**
	 * Adds a sliding drawer to menu (for application settings)
	 */
	private void addSlidingDrawer() {

		slideButton = (Button) findViewById(R.id.slideButton);
		slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);
		slideBg = (LinearLayout) findViewById(R.id.contentLayout);
		// Sliding drawers Events
		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				slideButton
						.setBackgroundResource(R.drawable.settings_icon_down);
				slideBg.setBackgroundResource(R.drawable.setting_bg_opened);
				slidingDrawer.setClickable(true);
			}
		});
		slidingDrawer.setOnDrawerScrollListener(new OnDrawerScrollListener() {

			public void onScrollStarted() {
				if (slidingDrawer.isOpened()) {
					slideButton
							.setBackgroundResource(R.drawable.settings_icon_down);
					slideBg.setBackgroundResource(R.drawable.setting_bg_opened);
				} else {
					slideBg.setBackgroundResource(R.drawable.setting_bg_opened);
				}
			}

			public void onScrollEnded() {
				if (slidingDrawer.isOpened()) {
					slideButton
							.setBackgroundResource(R.drawable.settings_icon_down);
					slideBg.setBackgroundResource(R.drawable.setting_bg_opened);
				} else {
					slideButton
							.setBackgroundResource(R.drawable.settings_icon_up);
					slideBg.setBackgroundResource(R.drawable.settings_bg);
				}

			}
		});

		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			public void onDrawerClosed() {

				slideButton.setBackgroundResource(R.drawable.settings_icon_up);
				slidingDrawer.setClickable(false);
			}
		});

	}

	/**
	 * Handle click on menu button OF THE DEVICE - opens and closes sliding
	 * drawer
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (!slidingDrawer.isOpened()) {
				slidingDrawer.open();
				return true;
			} else {
				slidingDrawer.close();
				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {

		if (slidingDrawer.isOpened()) {// close sliding drawer
			slidingDrawer.close();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		gpsObject.stopLocationUpdates();
		System.gc();
		Runtime.getRuntime().gc();

	}
}
