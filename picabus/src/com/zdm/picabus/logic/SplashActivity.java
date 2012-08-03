package com.zdm.picabus.logic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zdm.picabus.R;

public class SplashActivity extends Activity {
   private static final int splashDurationMS = 1500;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        
        // Setting the splash
        Thread timer = new Thread() {
        	public void run(){
        		try {
					sleep(splashDurationMS);
					Intent openMainScreen = new Intent("com.zdm.picabus.MAINSCREEN");
        			startActivity(openMainScreen);
        			finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					
				}
        	}
        };
        timer.start();
    }
}