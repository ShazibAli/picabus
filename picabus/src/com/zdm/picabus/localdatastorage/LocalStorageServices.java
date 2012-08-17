package com.zdm.picabus.localdatastorage;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class LocalStorageServices{
	
	private static final String HISTORY_RECORD_COUNT = "UserHistoryCount";
	private static final String HISTORY_RECORD_PREFFIX = "user_history_";
	

		
	public boolean saveUserPreferences(UserPreferences up, Context context) {
		  
	      return true;
	}
	
	public UserHistory getUserPreferences(Context context) {
		return null;
	}
	
	/**
	 * This method gets all the user history records
	 * 
	 * @param context the context of the current activity 
	 * @return an ArrayList containing all the user history records (can be empty if no record were found/an error occured)
	 */
	public ArrayList<UserHistory> getUserHistory(Context context) {
	     
		// get shared prefs editor
		SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		ArrayList<UserHistory> historyRecords = new ArrayList<UserHistory>();
		
		// get the number of history records
		int numOfRecords = appSharedPrefs.getInt(HISTORY_RECORD_COUNT, 0);
		
		Gson gson = new Gson();
		String json;
		UserHistory uh;
		
		for (int i = 1; i <= numOfRecords; i++) {
			json = appSharedPrefs.getString(HISTORY_RECORD_PREFFIX + i, null);
			// didn't get the value we've expected
			if (json == null) {
				return historyRecords;
			}
			
			uh = gson.fromJson(json, UserHistory.class);
			historyRecords.add(uh);
		}	
		return historyRecords;	
	}
	
	/**
	 * This method saves a single history record
	 * 
	 * @param uh UserHistory record we want to save
	 * @param context the context of the current activity 
	 * @return true iff the given record was saved successfully
	 */
	public boolean saveUserHistory(UserHistory uh, Context context) {
		
	     // get shared prefs editor
		  SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		  Editor prefsEditor = appSharedPrefs.edit();
		  
		  // convert user history object to json
		  Gson gson = new Gson();
		  String json = gson.toJson(uh);
		  
		  // calculate new id
		  int numOfExistingHistoryRecords = appSharedPrefs.getInt(HISTORY_RECORD_COUNT, 0);
		  int newHistoryRecordID = numOfExistingHistoryRecords + 1;
		  
		  // update records count and the current user history record
		  prefsEditor.putInt(HISTORY_RECORD_COUNT, numOfExistingHistoryRecords);
		  prefsEditor.putString(HISTORY_RECORD_PREFFIX + newHistoryRecordID , json);
		  prefsEditor.commit();
		    
	      return true;
	}
	
	
	/**
	 * This method deletes all of the user history records
	 * 
	 * @param context the context of the current activity
	 * @return true iff all the history records were deleted successfully
	 */
	public boolean removeAllHistoryRecords(Context context) {
	
		// get shared prefs editor
		 SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		  int numOfExistingHistoryRecords = appSharedPrefs.getInt("UserHistoryCount", 0);
		  
		  for (int i = 1; i <= numOfExistingHistoryRecords; i++) {
			context.getSharedPreferences(HISTORY_RECORD_PREFFIX + i, 0).edit().clear().commit();
		}
		return true;
		
	}
	
}
