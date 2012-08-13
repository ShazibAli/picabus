package com.zdm.picabus.logic;

import com.zdm.picabus.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SlidingDrawer.OnDrawerScrollListener;

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
		setContentView(R.layout.menu_screen);

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
