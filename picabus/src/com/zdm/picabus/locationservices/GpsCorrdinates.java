package com.zdm.picabus.locationservices;

//import com.zdm.picabus.locationservices.LbsGeocodingActivity.MyLocationListener;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GpsCorrdinates {

	private static final long MIN_TIME_BETWEEN_LOCATION_UPDATES = 0; // ms
	private static final long MIN_DISTANCE_INTERVAL_FOR_NOTIFICATIONS = 0;// meters

	private static GpsCorrdinates instance = null;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private Double lat;
	private Double lng;
	private Location lastLocation = null;

	/**
	 * creates GpsCorrdinates object with location manager
	 * 
	 * @param lm
	 *            - location manager
	 */
	private GpsCorrdinates(Context c) {
		locationManager = (LocationManager) c
				.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListenerHelper();
		requestLocationUpdates();
	}

	/**
	 * requests locationListener to update GPS locations constantly
	 */
	private void requestLocationUpdates(){
		//request locations updates from GPS provider
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				MIN_TIME_BETWEEN_LOCATION_UPDATES,
				MIN_DISTANCE_INTERVAL_FOR_NOTIFICATIONS, 
				locationListener);
	}
	
	
	/**
	 * after gps corrdinates was taken
	 * stop locationManager from getting updates
	 */
	public void stopLocationUpdates(){
		locationManager.removeUpdates(locationListener);
	}
	
	
	/**
	 * 
	 * @param c
	 *            - context of android activity
	 * @return object GpsCorrdinates, represents device's GpsCorrdinates
	 */
	public static GpsCorrdinates getGpsInstance(Context c) {
		if (instance != null) {
			instance.requestLocationUpdates();
			return instance;
		} else {
			instance = new GpsCorrdinates(c);
			return instance;
		}
	}

	/**
	 * updated lat and lng field according to device's location
	 */
	public void getCurrentLocation() {

/*		lastLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastLocation != null) {
			lat = lastLocation.getLongitude();
			lng = lastLocation.getLatitude();
		}*/
	}

	/**
	 * 
	 * @param lm
	 *            - location manager
	 * @return true is GPS is enabled, false otherwise
	 */
	public boolean isGpsEnabled() {

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return Latitude of machine's GPS corrdinates
	 */
	public Double getLatitude() {
		return lat;
	}

	/**
	 * 
	 * @return Longitude of machine's GPS corrdinates
	 */
	public Double getLongitude() {
		return lng;
	}

	

	
	/**
	 * private inner class for getting updated locations constantly
	 */
	private class LocationListenerHelper implements LocationListener{

		public void onLocationChanged(Location loc) {
			lastLocation = loc;
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

	}
	


}
