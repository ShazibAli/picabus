package com.zdm.picabus.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import sun.security.jca.GetInstance;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SlidingDrawer.OnDrawerScrollListener;
import android.widget.Spinner;

import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.zdm.picabus.R;
import com.zdm.picabus.facebook.PicabusFacebookObject;
import com.zdm.picabus.facebook.ProfileImageGetter;
import com.zdm.picabus.locationservices.GpsCorrdinates;
import com.zdm.picabus.utilities.DataCollector;
import com.zdm.picabus.utilities.ErrorsHandler;
import com.zdm.picabus.utilities.SettingsParser;

/**
 * 
 * Main menu activity
 * 
 */
public class MainScreenActivity extends Activity {

	static final int NOTIFICATION_UNIQUE_ID = 139874;

	ImageButton cameraBtn;
	ImageButton historyBtn;
	ImageButton aboutUsBtn;
	ImageButton searchBtn;
	ImageButton myPicabusBtn;
	Button slideButton;
	SlidingDrawer slidingDrawer;

	NotificationManager nm;
	GpsCorrdinates gpsObject;
	LocationManager locationManager;
	PicabusFacebookObject facebookObject;
	
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_screen);

		context = this;
		// Canceling the notification - if it was raised
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(NOTIFICATION_UNIQUE_ID);

		// get GPS service
		gpsObject = DataCollector.getGpsObject(this);
		locationManager = DataCollector.getLocationManager(this);

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
				if (!gpsObject.isGpsEnabled(locationManager)) {
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
				if (!gpsObject.isGpsEnabled(locationManager)) {
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
				Intent i = getIntent();
				Boolean loggedIn = i.getBooleanExtra("loggedIn", false);
				if (loggedIn) {
					facebookObject = PicabusFacebookObject
							.getFacebookInstance();
					updateProfileInformation();
					if (facebookObject.getProfilePicture() == null) {
						ProfileImageGetter profilePicRequest = new ProfileImageGetter(
								context);
						profilePicRequest.execute();
					} else {
						Intent intent = new Intent(
								"com.zdm.picabus.facebook.MyPicabusActivity");
						startActivity(intent);
					}
				} else {
					Intent intent = new Intent(
							"com.zdm.picabus.facebook.MyPicabusLoggedOutActivity");
					startActivity(intent);
				}
			}
		});

		// Adds sliding drawer and application settings inside
		addSlidingDrawer();
		addSettingsToSlidingDrawer();
	}

	/**
	 * Add setting UI to the sliding drawer
	 */
	private void addSettingsToSlidingDrawer() {

		// inside sliding drawer
		final Spinner spNotification = (Spinner) findViewById(R.id.SpinnerNotification);
		final Spinner spTimeInterval = (Spinner) findViewById(R.id.SpinnerTimeInterval);
		CheckBox checkboxTimeInterval = (CheckBox) findViewById(R.id.notifyCheckBox);

		// Spinner Time interval
		String intervalTimes[] = { "5 minutes", "10 minutes", "15 minutes",
				"30 minutes", "1 hour", "2 hours", "5 hours", "1 day" };
		final ArrayAdapter<String> adapterInterval = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, intervalTimes);
		spTimeInterval.setAdapter(adapterInterval);

		spTimeInterval.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				String choice = (String) parent.getItemAtPosition(pos);
				int choiceInMinutes = SettingsParser.ParseTimeInMinutes(choice);
				// TODO: call function
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		// Spinner Notification times
		String notificationTimes[] = { "1 minute", "5 minutes", "10 minutes",
				"15 minutes", "30 minutes", "1 hour" };
		final ArrayAdapter<String> adapterNotification = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, notificationTimes);
		spNotification.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				String choice = (String) parent.getItemAtPosition(pos);
				int choiceInMinutes = SettingsParser.ParseTimeInMinutes(choice);
				// TODO: call function
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}

		});

		// checkbox time interval
		checkboxTimeInterval
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							spNotification.setAdapter(adapterNotification);
						} else {
							spNotification.setEnabled(false);
						}
					}
				});

	}

	/**
	 * Adds a sliding drawer to menu (for application settings)
	 */
	private void addSlidingDrawer() {

		slideButton = (Button) findViewById(R.id.slideButton);
		slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);

		// Sliding drawers Events
		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				slideButton
						.setBackgroundResource(R.drawable.settings_icon_down);
				slidingDrawer.setClickable(true);
			}
		});
		slidingDrawer.setOnDrawerScrollListener(new OnDrawerScrollListener() {

			public void onScrollStarted() {
				if (slidingDrawer.isOpened()) {
					slideButton
							.setBackgroundResource(R.drawable.settings_icon_down);
				} else
					slideButton
							.setBackgroundResource(R.drawable.settings_icon_up);
				;
			}

			public void onScrollEnded() {
				if (slidingDrawer.isOpened()) {
					slideButton
							.setBackgroundResource(R.drawable.settings_icon_down);
				} else
					slideButton
							.setBackgroundResource(R.drawable.settings_icon_up);
				;

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


		if (slidingDrawer.isOpened()){//close sliding drawer
			slidingDrawer.close();
		}
		else{
			super.onBackPressed();
		}
	}
	
	/**
	 * Get information from facebook regarding to the user
	 */

	public void updateProfileInformation() {
		facebookObject.mAsyncRunner.request("me", new RequestListener() {

			public void onComplete(String response, Object state) {

				String name;
				String facebookId;
				Log.d("Profile", response);
				String json = response;
				try {
					JSONObject profile = new JSONObject(json);
					// getting name of the user
					name = profile.getString("name");

					// getting facebook id of the user
					facebookId = profile.getString("id");

					facebookObject.name = name;
					facebookObject.facebookId = facebookId;

					// get the picture
					// facebookObject.updateProfilePicture(facebookObject.facebookId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public void onIOException(IOException e, Object state) {
			}

			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}
}
