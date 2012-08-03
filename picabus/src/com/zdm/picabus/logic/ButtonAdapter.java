package com.zdm.picabus.logic;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.zdm.picabus.R;

public class ButtonAdapter extends BaseAdapter implements OnClickListener{

	private Context context;
	// references to our images
	private Integer[] mThumbIds = { 
			R.drawable.camera_icon, R.drawable.search_icon, 
			R.drawable.map_icon, R.drawable.help_icon, 
			R.drawable.settings_icon, R.drawable.aboutus_icon };

	// Gets the context so it can be used later
	public ButtonAdapter(Context c) {
		context = c;
	}

	
	public int getCount() {
		return mThumbIds.length;
	}

	
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	
	public View getView(int position, View convertView, ViewGroup parent) {
		Button btn;
		if (convertView == null) {
			// if it's not recycled, initialize some attributes
			btn = new Button(context);
			btn.setLayoutParams(new GridView.LayoutParams(150, 150));
			btn.setPadding(8, 8, 8, 8);
		} else {
			btn = (Button) convertView;
		}

		btn.setBackgroundResource(mThumbIds[position]);
		btn.setId(position);
		btn.setOnClickListener(this);

		return btn;
	}


	public void onClick(View v) {
		Toast t = Toast.makeText(context, "The button id is: " + v.getId(), Toast.LENGTH_SHORT);
		t.show();
	}
}
