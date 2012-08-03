package com.zdm.picabus.logic;

import com.zdm.picabus.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

public class MainScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.main_screen);

	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ButtonAdapter(this));
	    
	    int id = gridview.getCheckedItemPosition();
		
	}
}
