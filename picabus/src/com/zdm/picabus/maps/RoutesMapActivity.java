package com.zdm.picabus.maps;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.zdm.picabus.R;
import com.zdm.picabus.enitities.Stop;

public class RoutesMapActivity extends MapActivity {


	MapView mapView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_map);
		mapView = (MapView) findViewById(R.id.mapView);
		
		Intent i = getIntent();
		
		@SuppressWarnings("unchecked")
		ArrayList<Stop> stops = (ArrayList<Stop>) i.getSerializableExtra("stops");
		
		// Displaying Zooming controls
		mapView.setBuiltInZoomControls(true);
		MapController mc = mapView.getController();

        if (stops == null || stops.size() == 0) {
        	// no stops
        	mapView.invalidate();
        }
        
        else {
    		List<Overlay> mapOverlays = mapView.getOverlays();
			Drawable drawableRegularStop = this.getResources().getDrawable(R.drawable.pin_point_icon);
			Drawable drawableCurrenStop = this.getResources().getDrawable(R.drawable.u_r_here_pin);
			Drawable drawableCurrentLocation = this.getResources().getDrawable(R.drawable.bus_current_pin);
			
			AddItemizedOverlay itemizedOverlayRegular = new AddItemizedOverlay(drawableRegularStop, this);
			AddItemizedOverlay itemizedOverlayFirst = new AddItemizedOverlay(drawableCurrenStop, this);
			AddItemizedOverlay itemizedOverlayCurrentBusLocation = new AddItemizedOverlay(drawableCurrentLocation, this);
			
			for (int j = 0; j < stops.size(); j++) {
				// handle bus real time location
				if (stops.get(j).isRealtimeReport()) {
					Stop realtimeLocation = stops.get(j);
					GeoPoint geoPoint = new GeoPoint(
							(int) (realtimeLocation.getLatitude() * 1E6),
							(int) (realtimeLocation.getLongitude() * 1E6));

					OverlayItem overlayitem = new OverlayItem(geoPoint, "Current bus location: ","This is the last reported location of this bus. \nReported at: " + realtimeLocation.getReportTimestampString());
					itemizedOverlayCurrentBusLocation.addOverlay(overlayitem);
					mapOverlays.add(itemizedOverlayCurrentBusLocation);
				}
				// handle "regular" stops
				else {
				
					Stop currentStop = stops.get(j);
					GeoPoint geoPoint = new GeoPoint(
							(int) (currentStop.getLatitude() * 1E6),
							(int) (currentStop.getLongitude() * 1E6));

					OverlayItem overlayitem = new OverlayItem(geoPoint,
							currentStop.getStopSequenceNumber() + "",
							currentStop.getStopName() + "\nETA: "
									+ currentStop.getDepartureTimeString());

					// using special pin for current user location
					if (j == 0) {
						itemizedOverlayFirst.addOverlay(overlayitem);
						mapOverlays.add(itemizedOverlayFirst);
					} else {
						itemizedOverlayRegular.addOverlay(overlayitem);
						mapOverlays.add(itemizedOverlayRegular);
					}

					// we want to zoom onto the source stop location
					if (j == 0) {
						mc.animateTo(geoPoint);
						mc.setZoom(17);
					}
				}
        	}	
			mapView.invalidate();
        }		        		
	}
	
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
