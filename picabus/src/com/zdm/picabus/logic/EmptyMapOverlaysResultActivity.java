package com.zdm.picabus.logic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zdm.picabus.R;

/**
 * 
 * Activity for displaying no map after "get route" operation
 *
 */
public class EmptyMapOverlaysResultActivity  extends Activity{
		Button backToResults;
		private static final String NO_ROUTE_STR = "Sorry, couldn't retrieve map locations of the current bus line";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_busarrival_empty);
				
		//Text - change to route text if it's empty route activity
		TextView text = (TextView) findViewById(R.id.emptyResultsText);
		text.setText(NO_ROUTE_STR);
		
		//Back to menu button
		backToResults = (Button) findViewById(R.id.buttonBackToMenu);
		backToResults.setText("Back to results page");
		backToResults.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//goto main menu activity and close all other activities
				finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	
	
}
