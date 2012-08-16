package com.zdm.picabus.logic;

import com.zdm.picabus.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SlidingDrawer.OnDrawerScrollListener;
import android.widget.Spinner;

public class MainScreenActivity extends Activity {

	ImageButton cameraBtn;
	ImageButton historyBtn;
	ImageButton aboutUsBtn;
	ImageButton searchBtn;
	Button slideButton;
	SlidingDrawer slidingDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu_screen);

		cameraBtn = (ImageButton) findViewById(R.id.button_camera);
		historyBtn = (ImageButton) findViewById(R.id.button_history);
		aboutUsBtn = (ImageButton) findViewById(R.id.button_aboutus);
		searchBtn = (ImageButton) findViewById(R.id.button_search);

		cameraBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						"com.zdm.picabus.cameraservices.CameraActivity");
				startActivity(intent);
			}
		});

		aboutUsBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						"com.zdm.picabus.logic.AboutUsActivity");
				startActivity(intent);
			}
		});

		searchBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						"com.zdm.picabus.logic.ManualSearchActivity");
				startActivity(intent);
			}
		});

		// slidingDrawer - preferences part
		slideButton = (Button) findViewById(R.id.slideButton);
		slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);

		//inside sliding drawer
		final Spinner spNotification = (Spinner) findViewById(R.id.SpinnerNotification);
		final Spinner spTimeInterval = (Spinner) findViewById(R.id.SpinnerTimeInterval);
		CheckBox checkboxTimeInterval = (CheckBox) findViewById(R.id.notifyCheckBox);
		
		//Spinner Time interval
		String intervalTimes[]={"5 minutes","10 minutes","15 minutes","30 minutes","1 hour","2 hours", "5 hours", "1 day"};
		final ArrayAdapter<String> adapterInterval = new ArrayAdapter<String>(this,
                                         android.R.layout.simple_spinner_item, intervalTimes);
		spTimeInterval.setAdapter(adapterInterval);

		spTimeInterval.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
			//	String choice = spNotification.getSelectedItem().toString();
				//TODO: parse that result, get the number only
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		//Spinner Notification times
		String notificationTimes[]={"1 minute","5 minutes","10 minutes","15 minutes","30 minutes","1 hour"};
		final ArrayAdapter<String> adapterNotification = new ArrayAdapter<String>(this,
                                         android.R.layout.simple_spinner_item, notificationTimes);
		spNotification.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//String choice = spNotification.getSelectedItem().toString();
				//TODO: parse that result, get the number only
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		//checkbox time interval
		checkboxTimeInterval.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked){
					spNotification.setAdapter(adapterNotification);
				}
				else{
					//spTimeInterval.set
				}
			}
		});
		
		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			public void onDrawerOpened() {
				slideButton
						.setBackgroundResource(R.drawable.settings_icon_down);
			}
		});
		slidingDrawer.setOnDrawerScrollListener(new OnDrawerScrollListener() {

			public void onScrollStarted() {
				if (slidingDrawer.isOpened()) {
					slideButton
							.setBackgroundResource(R.drawable.settings_icon_down);
				} else
					slideButton
							.setBackgroundResource(R.drawable.settings_icon_up);
				;
			}

			public void onScrollEnded() {
				if (slidingDrawer.isOpened()) {
					slideButton
							.setBackgroundResource(R.drawable.settings_icon_down);
				} else
					slideButton
							.setBackgroundResource(R.drawable.settings_icon_up);
				;

			}
		});

		slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			public void onDrawerClosed() {
				slideButton.setBackgroundResource(R.drawable.settings_icon_up);
			}
		});
	}

}
