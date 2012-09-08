package com.zdm.picabus.maps;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.zdm.picabus.R;
 
public class AddItemizedOverlay extends ItemizedOverlay<OverlayItem> {
 
       private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();
 
       private Context context;
     
       public AddItemizedOverlay(Drawable defaultMarker) {
            super(boundCenterBottom(defaultMarker));
       }
 
       public AddItemizedOverlay(Drawable defaultMarker, Context context) {
            this(defaultMarker);
            this.context = context;
       }
 
       
       @Override
       protected OverlayItem createItem(int i) {
          return mapOverlays.get(i);
       }
 
       @Override
       public int size() {
          return mapOverlays.size();
       }
 
       @Override
       protected boolean onTap(int index) {
    	   
		OverlayItem item = mapOverlays.get(index);

           //Do stuff here when you tap
           AlertDialog.Builder dialog = new AlertDialog.Builder(context);
           dialog.setTitle(item.getTitle());
           dialog.setMessage(item.getSnippet());
           dialog.setIcon(R.drawable.application_icon);
           
           // Setting OK Button
           dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                	   // just return to map
                   }
           });
           dialog.show();

           //return true to indicate we've taken care of it
           return true;
       }
 
       @Override
       public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow) {
           super.draw(canvas, mapView, false);
               //cycle through all overlays
               for (int index = 0; index < mapOverlays.size(); index++) {
                   OverlayItem item = mapOverlays.get(index);
                   
                   // Converts lat/lng-Point to coordinates on the screen
                   GeoPoint point = item.getPoint();
                   Point ptScreenCoord = new Point() ;
                   mapView.getProjection().toPixels(point, ptScreenCoord);       
           }
       }
       
       public void addOverlay(OverlayItem overlay) {
          mapOverlays.add(overlay);
           this.populate();
       }
       @Override
       public boolean onTouchEvent(MotionEvent event, MapView mapView) {   
           if (event.getAction() == 1) {  	   
             //  Toast.makeText(context, "For details, just click on a station", Toast.LENGTH_SHORT).show();
           }
           return false;
       }
 
    }