package com.zdm.picabus.localdatastorage;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalStorageServices{
	public static final String PREFS_NAME = "UserPreferences";
	public static final String HOSTORY_NAME = "UserHistory";
	
	public UserHistory getUserHistory(Context context) {
		return null;
	}
		
	public boolean saveUserPreferences(UserPreferences up, Context context) {
		  SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      return true;
	}

	
	public UserHistory getUserPreferences(Context context) {
		return null;
	}
	
	public boolean saveUserHistory(UserHistory uh, Context context) {
		  SharedPreferences settings = context.getSharedPreferences(HOSTORY_NAME, 0);
	      SharedPreferences.Editor editor = settings.edit();
	      return true;
	}
}
