package com.zdm.picabus.tempmoran;

import com.zdm.picabus.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SlidingDrawer.OnDrawerScrollListener;
import android.widget.Toast;
 
public class slider extends Activity implements OnClickListener {
 
    Button slideButton,b1, b2,b3,b4;
    SlidingDrawer slidingDrawer;
 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.setting_slider);
        slideButton = (Button) findViewById(R.id.slideButton);
        slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);
        b1 = (Button) findViewById(R.id.Button01);
        b2 = (Button) findViewById(R.id.Button02);

 
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);

 
  /*      slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
            public void onDrawerOpened() {
                slideButton.setBackgroundResource(R.drawable.green_down_arrow);
            }
        });
        slidingDrawer.setOnDrawerScrollListener(new OnDrawerScrollListener() {
			
			public void onScrollStarted() {
				if (slidingDrawer.isOpened()) {
					slideButton.setBackgroundResource(R.drawable.green_down_arrow);
				}
				else slideButton.setBackgroundResource(R.drawable.green_up_arrow);;
			}
			
			public void onScrollEnded() {
				if (slidingDrawer.isOpened()) {
					slideButton.setBackgroundResource(R.drawable.green_down_arrow);
				}
				else slideButton.setBackgroundResource(R.drawable.green_up_arrow);;
				
			}
		});
 
        slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {


            public void onDrawerClosed() {
                slideButton.setBackgroundResource(R.drawable.green_up_arrow);
            }
        });
 */
    }
 

    public void onClick(View v) {
        Button b = (Button)v;
        Toast.makeText(slider.this, b.getText() + " Clicked", Toast.LENGTH_SHORT).show();
    }
}