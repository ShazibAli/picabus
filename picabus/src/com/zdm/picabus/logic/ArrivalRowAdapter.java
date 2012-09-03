package com.zdm.picabus.logic;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zdm.picabus.R;
import com.zdm.picabus.enitities.TripResultObject;

/**
 * 
 *  adapter for a time arrival in the list of arrival times result
 *
 */
public class ArrivalRowAdapter extends ArrayAdapter<TripResultObject> {

	private ArrayList<TripResultObject> items;

	/** 
	 * constructor
	 * @param context
	 * @param textViewResourceId
	 * @param items - list of trips
	 */
	public ArrivalRowAdapter(Context context, int textViewResourceId,
			ArrayList<TripResultObject> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	/**
	 * Updates the view of each item on the list
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_arrival_time, null);
		}

		TripResultObject res = items.get(position);
		String time= res.getArrivalTime();
		//String time = items.get(position);
		if (time != null) {
			TextView tt = (TextView) v.findViewById(R.id.textviewarrival);

			if (tt != null) {
				tt.setText(time);
			}

		}
		return v;
	}

}
