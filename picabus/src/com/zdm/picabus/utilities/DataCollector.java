package com.zdm.picabus.utilities;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.location.LocationManager;

import com.zdm.picabus.locationservices.GpsCorrdinates;
import com.zdm.picabus.locationservices.GpsResult;

public class DataCollector {

	public static String getCurrentTime() {

		Calendar calendar = new GregorianCalendar();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String time = Integer.toString(hour) + ":" + Integer.toString(minute);

		return time;
	}

	public static GpsResult getGpsCoordinates(LocationManager locationManager) {

		GpsCorrdinates gps = new GpsCorrdinates(locationManager);
		gps.getCurrentLocation();
		Double lat = gps.getLatitude();
		Double lng = gps.getLongitude();
		GpsResult res = new GpsResult(lat, lng);

		return res;
	}
}
