package com.zdm.picabus.client.locationservices;

import java.util.Timer;
import java.util.TimerTask;

import com.zdm.picabus.client.locationservices.LocationHandler.GetLastLocation.LocationResult;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * 
 * Location service provider. 
 * 
 * @author Daniel Lereya
 * 
 * 
 */
public class LocationHandler {
	private Timer timer;
	private LocationManager lm;
	private LocationResult lr;
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	private static LocationHandler instance = null;
	private final int timerTime = 10000;
	
	private LocationHandler() {
	}

	/**
	 * Handles 'LocationHandler' class instance.
	 * 
	 * @return the only instance of 'LocationHandler' class (Singleton)
	 */
	public static LocationHandler getInstance() {
		if (instance == null) {
			instance = new LocationHandler();
		}
		return instance;
	}

	/**
	 * Gets the user current location coordinates.
	 * 
	 * @param context
	 * @param locationResult
	 *            used as callback class in order to pass location value from
	 *            'LocationHandler' to the user's code.
	 * @return true if the , otherwise false.
	 * 
	 */
	public boolean getLocation(Context context, LocationResult locationResult) {
		// 'LocationResult' is .
		lr = locationResult;

		if (lm == null) {
			lm = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
		}

		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
			// TODO: create ui popup - "Please turn your GPS on" and click ok
			// we'll create a custom exception that will propagate to the server
			// layer later on
		}

		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			// TODO: handle this exception
		}
		// if no provider is available, return false
		if (!gps_enabled && !network_enabled) {
			return false;
		}

		if (gps_enabled) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListenerGps);
		}

		if (network_enabled)
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
		timer = new Timer();
		timer.schedule(new GetLastLocation(), timerTime);
		return true;
	}

	
	
	
	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer.cancel();
			lr.gotLocation(location);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);
		}

		public void onProviderDisabled(String provider) {}

		public void onProviderEnabled(String provider) {}

		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer.cancel();
			lr.gotLocation(location);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerGps);
		}

		public void onProviderDisabled(String provider) {}

		public void onProviderEnabled(String provider) {}

		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};

	
	/**
	 * 
	 * @author Daniel Lereya
	 *
	 */
	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);

			Location net_loc = null, gps_loc = null;
			if (gps_enabled)
				gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (network_enabled)
				net_loc = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			// if we got location from both providers use the latest one
			if (gps_loc != null && net_loc != null) {
				if (gps_loc.getTime() > net_loc.getTime())
					lr.gotLocation(gps_loc);
				else
					lr.gotLocation(net_loc);
				return;
			}

			// both providers got null location
			else if (gps_loc == null && net_loc == null) {
				lr.gotLocation(net_loc);
				return;
			}

			// one of the providers is not null - pass on it's location
			else if (gps_loc != null) {
				lr.gotLocation(gps_loc);
			} else
				lr.gotLocation(net_loc);
			return;

		}

		public abstract class LocationResult {
			public abstract void gotLocation(Location location);
		}
	}
}
