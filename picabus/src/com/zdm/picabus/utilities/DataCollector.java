package com.zdm.picabus.utilities;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import com.zdm.picabus.locationservices.GpsCorrdinates;
import com.zdm.picabus.locationservices.GpsResult;

public class DataCollector {

	static GpsCorrdinates gps;

	/**
	 * 
	 * @return - user's current time as string XX:XX:XX
	 */
	public static String getCurrentTime() {

		String hourStr, minuteStr, secondStr;

		Calendar calendar = new GregorianCalendar();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		if (hour >= 10)
			hourStr = Integer.toString(hour);
		else
			hourStr = "0" + Integer.toString(hour);

		if (minute >= 10)
			minuteStr = Integer.toString(minute);
		else
			minuteStr = "0" + Integer.toString(minute);

		if (second >= 10)
			secondStr = Integer.toString(second);
		else
			secondStr = "0" + Integer.toString(second);

		String time = hourStr + ":" + minuteStr + ":" + secondStr;

		return time;
	}

	/**
	 * 
	 * @param locationManager
	 *            - Context.getSystemService(Context.LOCATION_SERVICE).
	 * 
	 * @return Gps coordinates of user's location
	 */
	public static GpsResult getGpsCoordinates(Context c) {

		GpsResult res = null;

		gps = GpsCorrdinates.getGpsInstance(c);
		gps.getCurrentLocation();
		Double lat = gps.getLatitude();
		Double lng = gps.getLongitude();

		// Format to 6 digits after dot
		if (lat != null && lng != null) {
			lat = Math.floor(lat * 1000000) / 1000000;
			lng = Math.floor(lng * 1000000) / 1000000;

			// create GPS object and return it
			res = new GpsResult(lat, lng);
		}
		return res;
	}

}
