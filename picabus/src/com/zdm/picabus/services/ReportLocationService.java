package com.zdm.picabus.services;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.zdm.picabus.connectivity.CustomHeader;
import com.zdm.picabus.connectivity.tasks.Tasks;

public class ReportLocationService extends Service implements LocationListener {

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 100; // in
																			// Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0; // in
																	// Milliseconds
	private static final long UPDATE_INTERVAL_MS = 3000;
	private static final long DELAY_INTERVAL_MS = 1000;
	private long tripId;
	private long userId;
	private Context mContext;
	private LocationManager locationManager;
	private Location location;
	Timer timer;
	private SendLocationUpdates sendLocationUpdates;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		this.mContext = this;
		// initiate the location manager and register for location updates from GPS
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);
		// start updated service
		sendLocationUpdates = new SendLocationUpdates();
		timer = new Timer("UpdatesReport");

	

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// extract values from the intent
		this.tripId = intent.getLongExtra("tripId", 0);
		this.userId = intent.getLongExtra("userId", 0);

		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		timer.schedule(sendLocationUpdates, DELAY_INTERVAL_MS, UPDATE_INTERVAL_MS);
		Log.d("UpdateService", "Created..");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
			// stop the location updates
		sendLocationUpdates.cancel();
		Log.d("UpdateService", "On Destroy..");
	}

	private class SendLocationUpdates extends TimerTask {

		private static final int CONNECTION_TIMEOUT = 10000;
		private static final int SO_TIMEOUT = 10000;
		private static final String localServerURL = "http://10.0.0.1:8888/reports";

		@Override
		public void run() {

			try {

				location = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);

				if (location == null || userId == 0 || tripId == 0) {
					return;
				}
				String serviceURL = localServerURL;

				JSONObject requestPayload = new JSONObject();
				try {
					requestPayload.put("userId", userId);
					requestPayload.put("latitude", location.getLatitude());
					requestPayload.put("longitude", location.getLongitude());
					requestPayload.put("tripId", tripId);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				;

				// Create new default http client
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						CONNECTION_TIMEOUT); // Timeout Limit
				HttpConnectionParams.setSoTimeout(client.getParams(),
						SO_TIMEOUT);
				HttpResponse response;
				HttpPost post = new HttpPost(serviceURL);

				try {
					StringEntity se = new StringEntity(
							requestPayload.toString());
					post.addHeader(CustomHeader.TASK_NAME.getHeaderName(),
							Tasks.REPORT_LOCATION.getTaskName());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
							"application/json"));
					post.setEntity(se);

					Log.d("UpdateService", "Sending update..");
					// Execute the request
					response = client.execute(post);
					Log.d("UpdateService", "Update sent..");

					// Get the response status code
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();

					if (statusCode == 200) { // Ok
						Log.d("UpdateService",
								"Update Recieved on the server..");
					} else if (statusCode == 500) {// Error
						Log.d("UpdateService",
								"Update Failed - On server level..");
					}
				} catch (Exception e) {
					Log.e("Error in connectivity layer, stacktrace: ",
							e.toString());

					return;
				}

			} catch (Exception e) {
				Log.d("UpdateService", "Error..");
				e.printStackTrace();

			}
		}
	}

	public void onLocationChanged(Location location) {
		this.location = location;

	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
