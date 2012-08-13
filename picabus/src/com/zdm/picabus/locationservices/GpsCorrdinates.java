package com.zdm.picabus.locationservices;

//import com.zdm.picabus.locationservices.LbsGeocodingActivity.MyLocationListener;

import android.location.Location;
import android.location.LocationManager;

public class GpsCorrdinates {

	private LocationManager locationManager;
	private Double lat;
	private Double lng;
	Location location;

	public GpsCorrdinates(LocationManager lm) {
		this.locationManager = lm;
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}

	public void getCurrentLocation() {

		if (location != null) {
			lat = location.getLongitude();
			lng = location.getLatitude();
		}
	}

	public Double getLatitude() {
		return lat;
	}

	public Double getLongitude() {
		return lng;
	}

}
