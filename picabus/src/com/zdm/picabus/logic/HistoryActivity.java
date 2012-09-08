package com.zdm.picabus.logic;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.connectivity.IHttpCaller;
import com.zdm.picabus.enitities.HistoryObject;
import com.zdm.picabus.utilities.DataCollector;

/**
 * 
 * Activity used to show users searches history
 *
 */
public class HistoryActivity extends ListActivity {

	public static final String PICABUS_PREFS_NAME = "picabusSettings";
	private ArrayList<HistoryObject> historyList = null;
	private ListAdapter historyRowAdapter;
	HistoryObject item;
	Context context;
	ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_screen);
		this.pd = new ProgressDialog(this);
		this.context = this;
		
		//get the history list
		HistoryService lss = new HistoryService();
		historyList = lss.getUserHistory(context);
	
		// Show the list
		this.historyRowAdapter = new HistoryRowAdapter(this, R.layout.history_screen, historyList);
		setListAdapter(this.historyRowAdapter);
	}
	
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		IHttpCaller ihc = null;
		ihc = HttpCaller.getInstance();
		
		//get history object
		HistoryObject res = (HistoryObject) this.historyRowAdapter.getItem(position);
		
		// get time interval from preferences
		SharedPreferences settings = getSharedPreferences(
				PICABUS_PREFS_NAME, 0);
		int timeInterval = settings.getInt("timeInterval", 30);

		// Get current time
		String time = DataCollector.getCurrentTime();

		//Send data to server
		ihc.getDepartureTime(context, pd, res.getLineNumber(), res.getLatitude(), res.getLongtitude(), time, timeInterval);
	
	}	
}
