package com.zdm.picabus.utilities;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.location.LocationManager;

import com.zdm.picabus.locationservices.GpsCorrdinates;
import com.zdm.picabus.locationservices.GpsResult;

public class DataCollector {

	public static String getCurrentTime() {

		String hourStr,minuteStr,secondStr;
		
		Calendar calendar = new GregorianCalendar();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		if (hour>=10)
			hourStr=Integer.toString(hour);
		else
			hourStr="0"+Integer.toString(hour);
		
		if (minute>=10)
			minuteStr=Integer.toString(minute);
		else
			minuteStr="0"+Integer.toString(minute);

		if (second>=10)
			secondStr=Integer.toString(second);
		else
			secondStr="0"+Integer.toString(second);
		
		String time = hourStr+":"+minuteStr+":"+secondStr;

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
