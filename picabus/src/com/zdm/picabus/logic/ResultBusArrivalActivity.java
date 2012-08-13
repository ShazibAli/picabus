package com.zdm.picabus.logic;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zdm.picabus.R;

//import com.zdm.picabus.logic.DataObject.TripObject;

public class ResultBusArrivalActivity extends ListActivity {

	private ArrayList<String> arrivalTimesList = null;
	private ArrivalRowAdapter arrivalRowAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_busarrival_screen);

		arrivalTimesList.add("08:55:33");
		arrivalTimesList.add("09:54:31");
		arrivalTimesList.add("10:55:10");

		Intent i = getIntent();
		DataObject data = (DataObject) i.getSerializableExtra("dataObject");
		int directionChoice = (int) i.getIntExtra("direction", 0);

		// update fields from data results:

		// line
		TextView textViewLine = (TextView) findViewById(R.id.textViewLine);
		textViewLine.setText(data.tripsList.get(1).lineNumber);
		// company
		ImageView companyImage = (ImageView) findViewById(R.id.iconCompany);
		if (data.tripsList.get(1).companyName == "דן")
			companyImage.setImageResource(R.drawable.dan_icon);
		else if (data.tripsList.get(1).companyName == "אגד")
			companyImage.setImageResource(R.drawable.egged_icon);
		else if (data.tripsList.get(1).companyName == "קווים")
			companyImage.setImageResource(R.drawable.kavim_icon);
		// station
		TextView textViewStation = (TextView) findViewById(R.id.textViewStation);
		textViewLine.setText(data.stopHeadsign);
		// last stop
		TextView textViewLastStop = (TextView) findViewById(R.id.textViewLastStop);
		if (directionChoice == 1)
			textViewLastStop.setText(data.tripsList.get(1).destinationA);
		else if (directionChoice == 2)
			textViewLastStop.setText(data.tripsList.get(1).destinationB);

		this.arrivalRowAdapter = new ArrivalRowAdapter(this,
				R.layout.row_arrival_time, arrivalTimesList);
		setListAdapter(this.arrivalRowAdapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO: us 'position' in order to understand which hour was selected
		// and get the data
		int temp = position;
	}

}
