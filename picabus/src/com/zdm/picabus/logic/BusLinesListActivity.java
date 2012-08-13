package com.zdm.picabus.logic;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.locationservices.GpsResult;
import com.zdm.picabus.logic.DataObject.TripObject;
import com.zdm.picabus.utilities.DataCollector;

public class BusLinesListActivity extends ListActivity {

	private ArrayList<Integer> linesList = null;
	private LineRowAdapter lineRowAdapter;
	PopupWindow pw;
	int popupRetVal = 0;
	Intent resultsIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buslines_list_screen);

		// Get list of lines from OPEN CV
		Intent i = getIntent();
		linesList = i.getIntegerArrayListExtra("linesList");

		// Show the list of lines
		this.lineRowAdapter = new LineRowAdapter(this, R.layout.row_bus_lines,
				linesList);
		setListAdapter(this.lineRowAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		int line_number = this.lineRowAdapter.getItem(position);

		// Get current time
		String time = DataCollector.getCurrentTime();

		// Get coordinates
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		GpsResult res = DataCollector.getGpsCoordinates(locationManager);
		Double lat = res.getLat();
		Double lng = res.getLng();

		// Send data to server
		if (lat != null && lng != null)
			HttpCaller.getDepartureTime(line_number, lat, lng, time, 15);
		else
			// for emulator
			HttpCaller.getDepartureTime(line_number, 32.046738, 34.758574,
					time, 15);

		// TODO: parseData() into DataObject type
		// Temp=Create data object to pass.
		int tripCount = 2;
		DataObject data = new DataObject(tripCount);
		// for now-update data:
		data.new TripObject();
		DataObject.TripObject to = data.new TripObject();
		to.lineNumber = 45;
		to.companyName = "דן";
		to.destinationA = "ת  רכבת מרכז-תל אביב יפו";
		to.destinationB = "מתחם גי ילדי טהרן-ראשון לציון";
		data.stopHeadsign = "תרעד 12 רמת גן";
		data.tripsList.add(to);
		data.tripsList.add(to);

		// Pop-up - choose direction
		initiatePopupWindow(to.destinationA, to.destinationB);

		//Open new intent
		if (popupRetVal != 0) {
			int temp = popupRetVal;
			// Intent resultsIntent = new
			// Intent("com.zdm.picabus.logic.ResultBusArrivalActivity");
			resultsIntent.putExtra("direction", popupRetVal);
			// resultsIntent.putExtra("dataObject", data);
			// startActivity(resultsIntent);
		}
	}

	private void initiatePopupWindow(String directionA, String directionB) {

		try {

			// Get the instance of the LayoutInflater, use the context of this activity
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// Inflate the view from a predefined XML layout
			View layout = inflater.inflate(R.layout.direction_popup,
					(ViewGroup) findViewById(R.id.popup_element));
			// create a 300px width and 470px height PopupWindow
			pw = new PopupWindow(layout, 300, 470, true);
			// display the popup in the center
			pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

			// Popup buttons
			Button directionBtnA = (Button) layout
					.findViewById(R.id.directionBtnA);
			directionBtnA.setText(directionA);
			Button directionBtnB = (Button) layout
					.findViewById(R.id.directionBtnB);
			directionBtnB.setText(directionB);
			Button cancelButton = (Button) layout
					.findViewById(R.id.cancelPopUp);

			// Buttons click listeners
			directionBtnA.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupRetVal = 1;
					// resultsIntent.putExtra("direction", popupRetVal);
					pw.dismiss();
					// startActivity(resultsIntent);
				}
			});

			directionBtnB.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupRetVal = 2;
					// resultsIntent.putExtra("direction", popupRetVal);
					pw.dismiss();
					// startActivity(resultsIntent);
				}
			});

			// cancel button

			cancelButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pw.dismiss();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
