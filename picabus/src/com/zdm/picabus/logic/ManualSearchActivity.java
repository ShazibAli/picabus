package com.zdm.picabus.logic;

import java.util.ArrayList;
import java.util.List;

import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.locationservices.GpsResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ManualSearchActivity extends Activity {

	ImageButton submitBtn;
	TextView textField;
	int line_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual_search);

		submitBtn = (ImageButton) findViewById(R.id.button_submit_ft);
		textField = (TextView) findViewById(R.id.freetext_line_num);

		submitBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Get line number that was clicked
				String line_str = textField.getText().toString();
				line_number = Integer.parseInt(line_str);

				// Pass the line number to lines list intent
				List<Integer> linesList = new ArrayList<Integer>();
				linesList.add(line_number);
				Intent intent = new Intent(
						"com.zdm.picabus.logic.BusLinesListActivity");
				intent.putIntegerArrayListExtra("linesList",
						(ArrayList<Integer>) linesList);
				startActivity(intent);

			}
		});
	}
}