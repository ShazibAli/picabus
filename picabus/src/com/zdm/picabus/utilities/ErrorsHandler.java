package com.zdm.picabus.utilities;

import com.zdm.picabus.cameraservices.CameraActivity;
import com.zdm.picabus.logic.BusLinesListActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class ErrorsHandler {



/**
 * Creates error message when open cv return null list of lines,
 * and suggests an option to re-take to photo
 * 
 * @param context
 */
	public static void createNullLinesListErrorAlert(final Context c) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		alertDialogBuilder
				.setMessage(
						"No lines numbers were detected. Do you want to take the photo again?")
				.setCancelable(false)
				.setPositiveButton("Yes, activate my camera",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent(
										"com.zdm.picabus.cameraservices.CameraActivity");
								((CameraActivity) c).finish();
								c.startActivity(intent);
							}
						});
				alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						((CameraActivity) c).finish();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	
	/**
	 * Creates error message when GPS is disabled
	 * and suggests an option to open preferences and fix it
	 * 
	 * @param context
	 */
	public static void createGpsErrorAlert(final Context c) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		alertDialogBuilder
				.setMessage(
						"GPS is disabled in your device. Would you like to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes, goto Settings Page To Enable GPS",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent callGPSSettingIntent = new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								c.startActivity(callGPSSettingIntent);
							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	/**
	 * Creates error message when GPS langitude and latitude are NULL
	 * and suggests an option to try again
	 * @param timeInterval 
	 * @param time 
	 * @param line_number 
	 * @param pd 
	 * 
	 * @param context
	 */
	public static void createNullGpsCoordinatesErrorAlert(final Context c, final int line_number, final String time, final int timeInterval) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		alertDialogBuilder
				.setMessage(
						"Could not retrieve valid GPS coordinates. " +
						"Click 'OK' if you wish to try again or 'Cancel' to return to main menu")
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = ((BusLinesListActivity) c).getIntent();
								((BusLinesListActivity) c).finish();//close BusLinesListActivity
								//save data for current user's session
								intent.putExtra("isGpsError", true);
								intent.putExtra("lineNubmer",line_number);
								intent.putExtra("time",time);
								intent.putExtra("timeInterval", timeInterval);
								c.startActivity(intent);//restart activity
							}
						});
		alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						//((BusLinesListActivity) c).getParent().finish();//close free search page
						((BusLinesListActivity) c).finish();//close BusLinesListActivity
						Intent openMainScreen = new Intent("com.zdm.picabus.MAINSCREEN");
	        			c.startActivity(openMainScreen);
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	public static void createGeneralErrorAlert(final Context c, String message) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

							}
						});
		/*alertDialogBuilder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});*/
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
}
