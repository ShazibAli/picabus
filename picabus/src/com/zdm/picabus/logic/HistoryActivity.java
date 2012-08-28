package com.zdm.picabus.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.connectivity.IHttpCaller;
import com.zdm.picabus.enitities.Company;
import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.Trip;
import com.zdm.picabus.localdatastorage.LocalStorageServices;
import com.zdm.picabus.localdatastorage.UserHistory;
import com.zdm.picabus.locationservices.GpsResult;
import com.zdm.picabus.utilities.DataCollector;
import com.zdm.picabus.utilities.ErrorsHandler;


public class HistoryActivity extends ListActivity {

	private ArrayList<UserHistory> historyList = null;
	private ArrayList<Integer> linesList = new ArrayList<Integer>();;
	private ListAdapter lineRowAdapter;
	LocalStorageServices storage;
	UserHistory item;
	Context context;
	ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_screen);
		this.pd = new ProgressDialog(this);
		context = this;
		storage = new LocalStorageServices();
		
		// TODO: handle case where there is no history item or no history at all
		/*	
		historyList = storage.getUserHistory(this);
		
	
		for(UserHistory hist: historyList)
		{
			linesList.add(hist.getLineNumber());
		} 
		*/
		linesList.add(25);
		linesList.add(26);
		linesList.add(27);
		
		
		// Show the list of lines
		this.lineRowAdapter = new LineRowAdapter(this, R.layout.history_screen, linesList);
		setListAdapter(this.lineRowAdapter);
		
		//saveUserHistory(item, context);
	}
	
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		IHttpCaller ihc = null;
		UserHistory hist = null;
		int line_number = (Integer) this.lineRowAdapter.getItem(position);
		String time = DataCollector.getCurrentTime();
		ihc = HttpCaller.getInstance();
		
		// TODO: get time interval from preferences
		int timeInterval = 15;
		
		// TODO: handle case where there is no history item or no history at all
		/*for(UserHistory histItem: historyList)
		{
			if(histItem.getLineNumber() == line_number)
			{
				hist = histItem;
				break;
			}
		} */
		
		
		//ihc.getDepartureTime(context, pd, line_number, hist.getLatitude(), hist.getLongitude(), time, timeInterval);
		//for debug:
		ihc.getDepartureTime(context, pd, line_number, 32.045816, 34.756983, time, timeInterval);
	
	}	
}
