package com.zdm.picabus.logic;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdm.picabus.R;
import com.zdm.picabus.enitities.Company;
import com.zdm.picabus.enitities.HistoryObject;

/**
 * 
 * adapter for a line in the list of lines result
 * 
 */
public class HistoryRowAdapter extends ArrayAdapter<HistoryObject> {

	private ArrayList<HistoryObject> items;

	public HistoryRowAdapter(Context context, int textViewResourceId,
			ArrayList<HistoryObject> items) {
		super(context, textViewResourceId, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_history, null);
		}

		HistoryObject obj = items.get(position);
		if (obj != null) {
			ImageView imageCompany = (ImageView) v
					.findViewById(R.id.imageBusSignHistory);
			TextView lineNumber = (TextView) v
					.findViewById(R.id.lineNumberHistory);
			TextView stationName = (TextView) v
					.findViewById(R.id.stationNameHistory);

			// image
			if (imageCompany != null) {
				String companyName = obj.getCompanyName();

				if (companyName.equals(Company.DAN.getCompanyName())) {
					imageCompany.setImageResource(R.drawable.dan_icon);
				} else if (companyName.equals(Company.EGGED.getCompanyName())) {
					imageCompany.setImageResource(R.drawable.egged_icon);
				} else if (companyName.equals(Company.METROPOLIN
						.getCompanyName())) {
					imageCompany.setImageResource(R.drawable.metropoline_icon);
				} else {
					imageCompany.setImageResource(R.drawable.line_row_bus);
				}
			}

			// line number
			if (lineNumber != null) {
				int lineNum = obj.getLineNumber();
				if (lineNum != 0) {
					lineNumber.setText(Integer.toString(lineNum));
				}
			}

			// station name
			if (stationName != null) {
				String stationNameStr = obj.getStationName();
				if (stationNameStr != null) {
					stationName.setText(stationNameStr);
				}
			}
		}
		return v;
	}

}
