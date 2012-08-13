package com.zdm.picabus.cameraservices;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;

import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.locationservices.GpsResult;
import com.zdm.picabus.utilities.DataCollector;

public class CameraActivity extends Activity {
	/** Called when the activity is first created. */

	private static final int CAMERA_PIC_REQUEST = 1337;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {
			// get image taken
			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

			// TODO: send to openCV, and open "BusLinesListActivity" from there*afterwards
			// for now-fake data from open cv
			List<Integer> linesList = new ArrayList<Integer>();
			linesList.add(13);
			linesList.add(232);
			linesList.add(73);
			linesList.add(45);
			linesList.add(53);

			Intent intent = new Intent(
					"com.zdm.picabus.logic.BusLinesListActivity");
			intent.putIntegerArrayListExtra("linesList",
					(ArrayList<Integer>) linesList);
			startActivity(intent);

		}
	}

}