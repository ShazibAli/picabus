package com.zdm.picabus.cameraservices;

import java.util.ArrayList;
import java.util.List;

import com.zdm.picabus.imageprocessing.costumizeImg;
import com.zdm.picabus.utilities.ErrorsHandler;

import android.app.Activity;
import android.content.Context;
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {

			if (resultCode == RESULT_OK) {
				// get image taken
				Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
				
				Context con = getApplicationContext();
				// Send image to open cv and get result
				List<Integer> linesList = costumizeImg.processImage(thumbnail, con);
				// null check - dummy values for open cv for now
/*				if (linesList == null) {
					linesList = new ArrayList<Integer>();
					linesList.add(45);
					linesList.add(10);
					linesList.add(114);
					linesList.add(67);
					linesList.add(624);
				}*/

				//null check - if OPEN CV result is null
				if (linesList == null) {
					ErrorsHandler.createNullLinesListErrorAlert(this);
				}
				else{
				// open results page
				Intent intent = new Intent(
						"com.zdm.picabus.logic.BusLinesListActivity");
				intent.putIntegerArrayListExtra("linesList",
						(ArrayList<Integer>) linesList);
				startActivity(intent);
				}

			}

			else if (resultCode == RESULT_CANCELED) {
				finish();
			} else {
			}
		}
		else{
			finish();
		}
	}
}
