package com.zdm.picabus.cameraservices;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.locationservices.GpsCorrdinates;

public class CameraActivity extends Activity {
    /** Called when the activity is first created. */
    
	private static final int CAMERA_PIC_REQUEST = 1337;
	private ImageView imageView;
	private Button btnTakePic;
	private TextView txtvwText;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST); 
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_PIC_REQUEST) {  
            // get image taken 
        	Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        	
        	//get current time
			Calendar calendar = new GregorianCalendar();
			int  hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			String time = "Time:\nHour:"+ Integer.toString(hour) + "Minute" + Integer.toString(minute) + "\n";

			//get coordinates
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			GpsCorrdinates gps = new GpsCorrdinates(locationManager);
			gps.getCurrentLocation();
			Double lat = gps.getLatitude();
			Double lng = gps.getLongitude();
			
        	//send data to server
			HttpCaller.sendData(time, thumbnail, lat, lng);
        	//HttpCaller.sendImage(thumbnail);
        	//imageView.setImageBitmap(thumbnail); 
        }  
    } 
    
}