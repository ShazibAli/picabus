package com.zdm.picabus.locationservices;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class GpsCorrdinates {

	protected LocationManager locationManager;
	//locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	
	private Double lat;
	private Double lng;
	
	protected void getCurrentLocation() {

		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			lat = location.getLongitude();
			lng = location.getLatitude();
		}

	}

}
