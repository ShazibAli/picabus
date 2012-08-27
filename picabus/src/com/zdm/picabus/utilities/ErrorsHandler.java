package com.zdm.picabus.utilities;

import com.zdm.picabus.cameraservices.CameraActivity;
import com.zdm.picabus.facebook.LoginActivity;
import com.zdm.picabus.logic.BusLinesListActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class ErrorsHandler {


	//private static final String ERROR_SERVER_STATUS_CODE = "Cannot retrieve answer from server, please try again later";
	private static final String ERROR_CONNECTION_TO_SERVER = "Problem connecting to server, please check your internet connection and try again";
	private static final String ERROR_GPS_TERMINATED = "GPS is disabled in your device. Would you like to enable it?";
	private static final String ERROR_OPEVCV_NULL_RESULT = "No lines numbers were detected. Do you want to take the photo again?";
	private static final String ERROR_NULL_GPS_COORDINATES="Could not retrieve valid GPS coordinates. " +
	"Click 'Try again' or 'Cancel' to return to main menu";
	private static final String ERROR_FACEBOOK_LOGIN = "Cannot login to your facebook acount. Redirecting to Picabus";
	private static final String ERROR_FACEBOOK_LOGIN_MY_PICABUS = "Cannot login to your facebook acount. Please try again later";
	private static final String ERROR_FACEBOOK_LOGOUT_MY_PICABUS = "Cannot logout from your facebook acount. Please try again later";
	private static final String ERROR_NULL_GPS_COORDINATES_MANUAL_SEARCH="Could not retrieve valid GPS coordinates. Please try again later";
	private static final String ERROR_NULL_LINE_MANUAL_SEARCH="No line number was entered. Please insert the requested bus line number";
	
/**
 * Creates error message when open cv return null list of lines,
 * and suggests an option to re-take to photo
 * 
 * @param context
 */
	public static void createNullLinesListErrorAlert(final Context c) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		alertDialogBuilder
				.setMessage(ERROR_OPEVCV_NULL_RESULT)
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
				.setMessage(ERROR_GPS_TERMINATED)
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
				.setMessage(ERROR_NULL_GPS_COORDINATES)
				.setCancelable(false)
				.setPositiveButton("Try again",
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
						//goto main menu activity and close all other activities
						Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						c.startActivity(intent);
					}
				});
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	
	
	/**
	 * error for manual search - when gps result is null
	 * @param c - context
	 */
	public static void createNullGpsManualSearchErrorAlert(final Context c) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		alertDialogBuilder
				.setMessage(ERROR_NULL_GPS_COORDINATES_MANUAL_SEARCH)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	/**
	 * error when user didn't enter line number and pressed "sumbit"
	 * @param c
	 */
	public static void createNullLineManualSearchErrorAlert(final Context c) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		alertDialogBuilder
				.setMessage(ERROR_NULL_LINE_MANUAL_SEARCH)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	
	
	
	/**
	 * Creates error message when there's a problem in connectivity layer
	 * and returns to main menu activity
	 * @param c - context
	 * @param message - one of 2 error types
	 */
	public static void createConnectivityErrorAlert(final Context c) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		alertDialogBuilder
				.setMessage(ERROR_CONNECTION_TO_SERVER)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
						//goto main menu activity and close all other activities
							Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							c.startActivity(intent);
							}
						});

		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	
	/**
	 * Creates error when can't login to user's facebook acount
	 * @param c - context
	 */
	public static void createFacebookFirstLoginErrorAlert(final Context c) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		alertDialogBuilder
				.setMessage(ERROR_FACEBOOK_LOGIN)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//goto main menu activity and close all other activities
								Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
								//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								((LoginActivity) c).finish();
								c.startActivity(intent);

							}
						});

		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	/**
	 * Creates error when can't login to user's facebook acount from my picabus
	 * @param c - context
	 */
	public static void createFacebookMyPicabusLoginErrorAlert(final Context c) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		alertDialogBuilder
				.setMessage(ERROR_FACEBOOK_LOGIN_MY_PICABUS)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//do nothing
							}
						});

		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
	
	/**
	 * Creates error when can't login to user's facebook acount from my picabus
	 * @param c - context
	 */
	public static void createFacebookMyPicabusLogoutErrorAlert(final Context c) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		alertDialogBuilder
				.setMessage(ERROR_FACEBOOK_LOGOUT_MY_PICABUS)
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//do nothing
							}
						});

		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}
}
