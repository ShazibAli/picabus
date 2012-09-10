package com.zdm.picabus.logic;

import com.zdm.picabus.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 
 * Activity for displaying no result page for the line request
 *  after attempting to get result page on line data
 *
 */

public class EmptyBusResultsActivity extends Activity{
	Button backToMainMenu;
	
@Override
protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	setContentView(R.layout.result_busarrival_empty);
	
	//Back to menu button
	backToMainMenu = (Button) findViewById(R.id.buttonBackToMenu);
	backToMainMenu.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			//goto main menu activity and close all other activities
			Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
		}
	});
}

@Override
protected void onDestroy() {
    super.onDestroy();
    System.gc();
    Runtime.getRuntime().gc();
}

}