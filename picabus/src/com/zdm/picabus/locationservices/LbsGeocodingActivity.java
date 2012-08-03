package com.zdm.picabus.locationservices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.zdm.picabus.R;

public class LbsGeocodingActivity extends Activity {

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1000; // in
																			// Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 30000; // in
																	// Milliseconds

	protected LocationManager locationManager;

	protected Button retrieveLocationButton;
	protected Button retrieveAddressButton;
	protected Button showOnMap;

	private MyLocationListener locationListener;

	private Double lat;
	private Double lng;

	List<Address> myList;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_tests);

		retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);
		retrieveAddressButton = (Button) findViewById(R.id.retrieve_address);
		//showOnMap = (Button) findViewById(R.id.show_on_map);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MINIMUM_TIME_BETWEEN_UPDATES,
				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locationListener);

		retrieveLocationButton.setOnClickListener(new OnClickListener() {
			//@Override
			public void onClick(View v) {
				showCurrentLocation();
			}
		});

		retrieveAddressButton.setOnClickListener(new OnClickListener() {

		//	@Override
			public void onClick(View v) {
				Geocoder myLocation = new Geocoder(getApplicationContext(),
						Locale.getDefault());
				try {
					List<Address> myList = myLocation.getFromLocation(lat, lng,
							1);
					StringBuilder curAddress = new StringBuilder();
					for (Address address : myList) {
						curAddress.append("\n" + "Country: "
								+ address.getCountryName() + "\n" + "City: "
								+ address.getLocality() + "\n" + "Street: "
								+ address.getAddressLine(0));
						// curAddress.append("\n" + address.getAddressLine(0) +
						// "\n" + address.getAddressLine(1) + "\n" +
						// address.getAddressLine(2));
					}
					String message = "Your current address is: "
							+ curAddress.toString();
					Toast.makeText(LbsGeocodingActivity.this, message,
							Toast.LENGTH_LONG).show();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int a = 5;

			}
		});

/*		showOnMap.setOnClickListener(new OnClickListener() {

//		//	@Override
//			public void onClick(View v) {
//
//				initiateGoogleMap();
//
//			}
		});
*/
	}

/*	protected void initiateGoogleMap() {
		Intent nextScreen = new Intent(getApplicationContext(),
				GoogleMapActivity.class);
		nextScreen.putExtra("longitude", lng);
		nextScreen.putExtra("latitude", lat);
		startActivity(nextScreen);

	}*/

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		locationManager.removeUpdates(locationListener);
	}

	protected void showCurrentLocation() {

		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location != null) {
			String message = String.format(
					"Current Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude());
			Toast.makeText(LbsGeocodingActivity.this, message,
					Toast.LENGTH_LONG).show();
			this.lat = location.getLatitude();
			this.lng = location.getLongitude();
		}

	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			String message = String.format(
					"New Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude());
			Toast.makeText(LbsGeocodingActivity.this, message,
					Toast.LENGTH_LONG).show();

		}

		public void onStatusChanged(String s, int i, Bundle b) {
			Toast.makeText(LbsGeocodingActivity.this,
					"Provider status changed", Toast.LENGTH_LONG).show();
		}

		public void onProviderDisabled(String s) {
			Toast.makeText(LbsGeocodingActivity.this,
					"Provider disabled by the user. GPS turned off",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderEnabled(String s) {
			Toast.makeText(LbsGeocodingActivity.this,
					"Provider enabled by the user. GPS turned on",
					Toast.LENGTH_LONG).show();
		}

	}

}