package com.zdm.picabus.logic;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.connectivity.IHttpCaller;
import com.zdm.picabus.locationservices.GpsResult;
import com.zdm.picabus.maps.AddItemizedOverlay;
import com.zdm.picabus.utilities.DataCollector;
import com.zdm.picabus.utilities.ErrorsHandler;

/**
 * 
 * Activity for handling manual search of lines option
 * 
 */
public class ManualSearchActivity extends MapActivity {

	public static final String PICABUS_PREFS_NAME = "picabusSettings";
	private ImageButton submitBtn;
	private TextView textField;
	private MapView mapView;
	private int line_number;
	private Context c;
	private IHttpCaller ihc = null;
	private ProgressDialog pd;
	private Double lat = null;
	private Double lng = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual_search);
		c = this;
		// Open progress dialog
		this.pd = new ProgressDialog(this);
		// get http caller instance
		ihc = HttpCaller.getInstance();

		// buttons
		submitBtn = (ImageButton) findViewById(R.id.button_submit_ft);
		textField = (TextView) findViewById(R.id.freetext_line_num);
		mapView = (MapView) findViewById(R.id.mapViewManualSearch);

		// the Digital Clock shows the current device time (by default)
		displayUserOnMap(mapView);

		submitBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// Get line number that was clicked
				String line_str = textField.getText().toString();
				if (line_str.equals("")) {
					ErrorsHandler.createNullLineManualSearchErrorAlert(c);
				} else {
					line_number = Integer.parseInt(line_str);

					// get necessary data and send request to server

					// get time interval from preferences
					SharedPreferences settings = getSharedPreferences(
							PICABUS_PREFS_NAME, 0);
					int timeInterval = settings.getInt("timeInterval", 15);

					// Get current time
					String time = DataCollector.getCurrentTime();

					// Get coordinates
					GpsResult res = DataCollector.getGpsCoordinates(c);
					if (res != null) {
						lat = res.getLat();
						lng = res.getLng();
					}

					//Send data to server
					// Use demo sign GPS coordinates if DEMO mode
					if (!MainScreenActivity.DEMO_MODE) {
					// Use real location
						if (lat != null || lng != null) {
							ihc.getDepartureTime(c, pd, line_number, lat, lng,
									time, timeInterval);
							finish();
						} else {
							ErrorsHandler
									.createNullGpsManualSearchErrorAlert(c);
						}
					} else {
						// in debug mode - GPS coordinates of station that
						// matches the DEMO sign
						ihc.getDepartureTime(c, pd, line_number, 32.073397,
								34.775048, time, timeInterval);
					}

				}
			}
		});
	}

	private void displayUserOnMap(MapView mapView) {

		// attempt to get GPS coordinates again
		double lat = 0;
		double lng = 0;

		GpsResult res = DataCollector.getGpsCoordinates(this);

		// Use demo sign GPS coordinates if DEMO mode
		if (MainScreenActivity.DEMO_MODE) {
			lat = 32.073397;
			lng = 34.775048;
		// Use real location
		} else if (res != null) {
			lat = res.getLat();
			lng = res.getLng();
		}
		else{
			return; //don't show location on map
		}
		// here lat and lng are not null - continue showing map

		// Displaying Zooming controls
		mapView.setBuiltInZoomControls(true);
		MapController mc = mapView.getController();

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawableCurrenStop = this.getResources().getDrawable(
				R.drawable.u_r_here_pin);

		AddItemizedOverlay itemizedOverlayFirst = new AddItemizedOverlay(
				drawableCurrenStop, this);

		GeoPoint geoPoint = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));

		OverlayItem overlayitem = new OverlayItem(geoPoint, "You are here",
				"The search is based on this location");

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