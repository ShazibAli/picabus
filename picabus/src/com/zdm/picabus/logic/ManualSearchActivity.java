package com.zdm.picabus.logic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DigitalClock;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.zdm.picabus.R;
import com.zdm.picabus.locationservices.GpsResult;
import com.zdm.picabus.maps.AddItemizedOverlay;
import com.zdm.picabus.utilities.DataCollector;

/**
 * 
 * Activity for handling manual search of lines option
 * 
 */
public class ManualSearchActivity extends MapActivity {

	ImageButton submitBtn;
	TextView textField;
	MapView mapView;
	DigitalClock dc;
	int line_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual_search);

		// the Digital Clock shows the current device time (by default)
		
		submitBtn = (ImageButton) findViewById(R.id.button_submit_ft);
		textField = (TextView) findViewById(R.id.freetext_line_num);
		mapView = (MapView) findViewById(R.id.mapViewManualSearch);
		
		
		displayUserOnMap(mapView);
		

		submitBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Get line number that was clicked
				String line_str = textField.getText().toString();
				line_number = Integer.parseInt(line_str);

				// Pass the line number to lines list intent
				List<Integer> linesList = new ArrayList<Integer>();
				linesList.add(line_number);
				// open new activity
				Intent intent = new Intent(
						"com.zdm.picabus.logic.BusLinesListActivity");
				intent.putIntegerArrayListExtra("linesList",
						(ArrayList<Integer>) linesList);
				startActivity(intent);

			}
		});
	}

	private void displayUserOnMap(MapView mapView) {
		
		// attempt to get GPS coordinates again
		GpsResult res = DataCollector.getGpsCoordinates(this);
		double lat = res.getLat();
		double lng = res.getLng();
		
		// Displaying Zooming controls
		mapView.setBuiltInZoomControls(true);
		MapController mc = mapView.getController();
		
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawableCurrenStop = this.getResources().getDrawable(R.drawable.u_r_here_pin);
		
		AddItemizedOverlay itemizedOverlayFirst = new AddItemizedOverlay(drawableCurrenStop, this);
  
		GeoPoint geoPoint = new GeoPoint((int) (lat * 1E6),
					(int) (lng * 1E6));
    		
    	OverlayItem overlayitem = new OverlayItem(geoPoint, "You are here", "The search is based on this location");

		itemizedOverlayFirst.addOverlay(overlayitem);
		mapOverlays.add(itemizedOverlayFirst);
		mc.animateTo(geoPoint);
		mc.setZoom(17);
	
		mapView.invalidate();
		
		
	}

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}
}