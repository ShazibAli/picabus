package com.zdm.picabus.connectivity.tasks;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.zdm.picabus.connectivity.IResponseParser;
import com.zdm.picabus.connectivity.ResponseParser;
import com.zdm.picabus.enitities.Report;
import com.zdm.picabus.logic.TripManagerActivity;

public class GetTextualReports extends HttpAbstractTask {
	
	private TripManagerActivity activity;
	
	public GetTextualReports(Context mContext, ProgressDialog waitSpinner,
			String taskName, JSONObject reuqestPayload) {
		super(mContext, waitSpinner, taskName, reuqestPayload);
		
	}

	public GetTextualReports(TripManagerActivity tripManagerActivity, Context mContext, ProgressDialog waitSpinner,
			String taskName, JSONObject reuqestPayload) {
		super(mContext, waitSpinner, taskName, reuqestPayload);
		this.activity = tripManagerActivity;
		
	}

	@Override
	protected void onPostExecute(String result) {
		JSONObject json = null;
		IResponseParser rp = null;

		// null result from server means an error in connectivity stage
		if (result == null) {
			return;
		} else { // result != null
			try {
				json = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			rp = new ResponseParser();
			ArrayList<Report> reports = rp.parseTripReports(json);

			if (reports.isEmpty()) {
			}

			else {
				activity.populateReportsList(reports);
				

			}
		}

	}

}
