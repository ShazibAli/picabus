package com.zdm.picabus.services;

import java.net.DatagramSocket;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class ReportLocationService extends Service implements LocationListener{

	private LocationManager locationManager;
	private Intent intent;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		initializeLocationManager();

	}


	@Override
	public IBinder onBind(Intent arg0) {
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {

		// start updates service

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class UpdatesService extends TimerTask {

		private DatagramSocket updatesSocket = null;

		public void run() {
			try {

			} catch (Exception e) {
				// Log.d("UDP Server", "S: Error updates...");
				e.printStackTrace();
			}
		}

	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	private void initializeLocationManager() {
		
		
	}

}
