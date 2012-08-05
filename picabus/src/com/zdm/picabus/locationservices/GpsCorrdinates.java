package com.zdm.picabus.locationservices;

//import com.zdm.picabus.locationservices.LbsGeocodingActivity.MyLocationListener;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GpsCorrdinates {

	private LocationManager locationManager;
	private Double lat;
	private Double lng;
	Location location;
	
	//locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

	/*locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
			MINIMUM_TIME_BETWEEN_UPDATES,
			MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
*/	
	
	public GpsCorrdinates(LocationManager lm){
		this.locationManager = lm;
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	}
	
	public void getCurrentLocation() {

		//locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (location != null) {
			lat = location.getLongitude();
			lng = location.getLatitude();
		}
	}
	
	public Double getLatitude(){
		return lat;
	}
	
	public Double getLongitude(){
		return lng;
	}

	
}
