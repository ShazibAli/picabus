package com.zdm.picabus.logic;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zdm.picabus.R;

public class ArrivalRowAdapter extends ArrayAdapter<String> {

	private ArrayList<String> items;

	public ArrivalRowAdapter(Context context, int textViewResourceId,
			ArrayList<String> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_arrival_time, null);
		}

		String time = items.get(position);
		if (time != null) {
			TextView tt = (TextView) v.findViewById(R.id.textviewarrival);

			if (tt != null) {
				tt.setText(time);
			}

		}
		return v;
	}

}
