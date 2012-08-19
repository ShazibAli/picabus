package com.zdm.picabus.locationservices;

//import com.zdm.picabus.locationservices.LbsGeocodingActivity.MyLocationListener;

import android.location.Location;
import android.location.LocationManager;

public class GpsCorrdinates {

	private LocationManager locationManager;
	private Double lat;
	private Double lng;
	Location location;

	/**
	 * creates GpsCorrdinates object with location manager
	 * @param lm - location manager
	 */
	public GpsCorrdinates(LocationManager lm) {
		this.locationManager = lm;
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}

	/**
	 * updated lat and lng field according to device's location
	 */
	public void getCurrentLocation() {

		if (location != null) {
			lat = location.getLongitude();
			lng = location.getLatitude();
		}
	}

	/**
	 * 
	 * @param lm - location manager
	 * @return true is GPS is enabled, false otherwise
	 */
	public boolean isGpsEnabled(LocationManager lm) {

		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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

}
