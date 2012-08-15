package com.zdm.picabus.cameraservices;

import java.util.ArrayList;
import java.util.List;

import com.zdm.picabus.imageprocessing.costumizeImg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

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
			List<Integer> linesList = costumizeImg.processImage(thumbnail);
			// TODO: send to openCV, and open "BusLinesListActivity" from there*afterwards
			// for now-fake data from open cv

			Intent intent = new Intent(
					"com.zdm.picabus.logic.BusLinesListActivity");
			intent.putIntegerArrayListExtra("linesList",
					(ArrayList<Integer>) linesList);
			startActivity(intent);

		}
	}

}