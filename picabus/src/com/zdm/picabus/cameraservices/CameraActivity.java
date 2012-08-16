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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_PIC_REQUEST) {
			// get image taken
			Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
			
			//Send image to open cv and get result
			List<Integer> linesList = costumizeImg.processImage(thumbnail);
			//null check - dummy values for open cv for now
			if (linesList==null){
				linesList=new ArrayList<Integer>();
				linesList.add(45);
				linesList.add(10);
				linesList.add(114);
				linesList.add(67);
				linesList.add(624);
			}
				
			//open results page
			Intent intent = new Intent(
					"com.zdm.picabus.logic.BusLinesListActivity");
			intent.putIntegerArrayListExtra("linesList",
					(ArrayList<Integer>) linesList);
			startActivity(intent);
			


		}
	}

}