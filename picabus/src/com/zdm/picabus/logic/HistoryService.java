package com.zdm.picabus.logic;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.zdm.picabus.enitities.HistoryObject;

public class HistoryService{
	
	private static final String HISTORY_RECORD_COUNT = "UserHistoryCount";
	private static final String HISTORY_RECORD_PREFFIX = "user_history_";
	public static final String HISTORY_PREFS_NAME = "resultDataPfers";
	
	/**
	 * This method gets all the user history records
	 * 
	 * @param context the context of the current activity 
	 * @return an ArrayList containing all the user history records (can be empty if no record were found/an error occured)
	 */
	public ArrayList<HistoryObject> getUserHistory(Context context) {
	     
		// get shared prefs editor
		SharedPreferences settings = context.getSharedPreferences(HISTORY_PREFS_NAME, 0);
		
		ArrayList<HistoryObject> historyRecords = new ArrayList<HistoryObject>();
		
		// get the number of history records
		int numOfRecords = settings.getInt(HISTORY_RECORD_COUNT, 0);
		
		Gson gson = new Gson();
		String json;
		HistoryObject uh;
		
		for (int i = 1; i <= numOfRecords; i++) {
			json = settings.getString(HISTORY_RECORD_PREFFIX + i, null);
			// didn't get the value we've expected
			if (json == null) {
				return historyRecords;
			}
			
			uh = gson.fromJson(json, HistoryObject.class);
			historyRecords.add(uh);
		}	
		return historyRecords;	
	}
	
	/**
	 * This method saves a single history record
	 * 
	 * @param ho HistoryObject - record we want to save
	 * @param context
	 * @return true iff the given record was saved successfully
	 */
	public boolean saveUserHistory(HistoryObject ho, Context context) {
		

	     // get shared prefs editor
		SharedPreferences settings = context.getSharedPreferences(HISTORY_PREFS_NAME, 0);
		SharedPreferences.Editor prefsEditor = settings.edit();
		  
		  // convert user history object to json
		  Gson gson = new Gson();
		  String json = gson.toJson(ho);
		
			//check if record already exist
			if (!checkIfRecordExist(json,context)){
		  // calculate new id
		  int numOfExistingHistoryRecords = settings.getInt(HISTORY_RECORD_COUNT, 0);
		  int newHistoryRecordID = numOfExistingHistoryRecords + 1;
		  
		  // update records count and the current user history record
		  prefsEditor.putInt(HISTORY_RECORD_COUNT, newHistoryRecordID);
		  prefsEditor.putString(HISTORY_RECORD_PREFFIX + newHistoryRecordID , json);
		  prefsEditor.commit();
		}
	      return true;
	}
	
	
/**
 * 
 * @param ho - history object to search
 * @param context
 * @return
 */
	public boolean checkIfRecordExist(String historyJson, Context context) {
	
		String curr;
		// get shared prefs editor
		SharedPreferences settings = context.getSharedPreferences(HISTORY_PREFS_NAME, 0);
		  int numOfExistingHistoryRecords = settings.getInt(HISTORY_RECORD_COUNT, 0);
		  
		  for (int i = 1; i <= numOfExistingHistoryRecords; i++) {
			  curr = settings.getString(HISTORY_RECORD_PREFFIX + i, null);
			  if (historyJson.equals(curr)){
				  return true;
			  }
		}
		return false;
		
	}
}
