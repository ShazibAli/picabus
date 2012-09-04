package com.zdm.picabus.logic;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdm.picabus.R;
import com.zdm.picabus.facebook.UserNamePicGetter;
 
public class ReportsListAdapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

 
    public ReportsListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.list_row, null);
        }
            
 
        TextView reporterName = (TextView)vi.findViewById(R.id.title); 
        TextView report = (TextView)vi.findViewById(R.id.report); 
        TextView timeOfReport = (TextView)vi.findViewById(R.id.report_time); 
        ImageView reporterImage = (ImageView)vi.findViewById(R.id.list_image); 
 
        HashMap<String, String> currentReport = new HashMap<String, String>();
        currentReport = data.get(position);
 
        // Setting all values in listview
        reporterName.setText("Picabus User");
        report.setText(currentReport.get(TripManagerActivity.KEY_REPORT));
        timeOfReport.setText(currentReport.get(TripManagerActivity.KEY_REPORT_TIME));
        
        long reporterId = Long.valueOf(currentReport.get(TripManagerActivity.KEY_REPORTER));
        UserNamePicGetter userNamePicRequest = new UserNamePicGetter(currentReport.get(TripManagerActivity.KEY_REPORTER), reporterName, reporterImage);
		userNamePicRequest.execute();
        return vi;
    }
}