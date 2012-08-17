package com.zdm.picabus.maps;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
 
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

           //Do stuff here when you tap, i.e. :
           AlertDialog.Builder dialog = new AlertDialog.Builder(context);
           dialog.setTitle("Stop sequence number: " + item.getTitle());
           dialog.setMessage(item.getSnippet());
           dialog.show();

           //return true to indicate we've taken care of it
           return true;
       }
 
       @Override
       public void draw(android.graphics.Canvas canvas, MapView mapView, boolean shadow) {
           super.draw(canvas, mapView, shadow);
           int mTextSize = 30;
           int dx = 0;
           int dy = 0;
        
               //cycle through all overlays
               for (int index = 0; index < mapOverlays.size(); index++) {
                   OverlayItem item = mapOverlays.get(index);

                   // Converts lat/lng-Point to coordinates on the screen
                   GeoPoint point = item.getPoint();
                   Point ptScreenCoord = new Point() ;
                   mapView.getProjection().toPixels(point, ptScreenCoord);

                   //Paint
                   Paint paint = new Paint();
                   paint.setTextAlign(Paint.Align.CENTER);
                   paint.setTextSize(mTextSize);
                   paint.setARGB(255, 148, 0, 211); // alpha, r, g, b (Purple, semi see-through)
                   //show text to the above of the icon
                   canvas.drawText(item.getTitle(), ptScreenCoord.x, ptScreenCoord.y  + mTextSize, paint);
                   
                  /* Rect rectBounds = new Rect();
                   paint.getTextBounds(item.getTitle(), 0, item.getTitle().length(), rectBounds);
                   rectBounds.inset(dx, dy);
                   paint.setColor(Color.BLACK);
                   paint.setStyle(Style.STROKE);
                   paint.setStrokeWidth(3);
              
                   canvas.drawRect(rectBounds, paint);
              */     
                   
                   
               
           }
       }
       
       public void addOverlay(OverlayItem overlay) {
          mapOverlays.add(overlay);
           this.populate();
       }
       @Override
       public boolean onTouchEvent(MotionEvent event, MapView mapView) {   
           if (event.getAction() == 1) {
        	   
               Toast.makeText(context, "For details, just click on a station", Toast.LENGTH_SHORT).show();
           }
           return false;
       }
 
    }