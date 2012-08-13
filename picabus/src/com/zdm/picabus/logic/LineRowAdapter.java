package com.zdm.picabus.logic;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.zdm.picabus.R;

public class LineRowAdapter extends ArrayAdapter<Integer> {

	private ArrayList<Integer> items;

	public LineRowAdapter(Context context, int textViewResourceId,
			ArrayList<Integer> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_bus_lines, null);
		}

		Integer line = items.get(position);
		if (line != null) {
			TextView tt = (TextView) v.findViewById(R.id.toptext);

			if (tt != null) {
				tt.setText("Line " + line);
			}

		}
		return v;
	}

}
