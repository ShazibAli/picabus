package com.zdm.picabus.logic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zdm.picabus.R;
 
public class SplashActivity extends Activity implements AnimationListener {
     public void onAttachedToWindow() {
            super.onAttachedToWindow();
            Window window = getWindow();
            window.setFormat(PixelFormat.RGBA_8888);
        }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        StartAnimations();
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.setAnimationListener(this);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);
 
        anim = AnimationUtils.loadAnimation(this, R.anim.logo_entrance);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);
 
    }
	public void onAnimationEnd(Animation animation) {
		Intent intent = new Intent("com.zdm.picabus.logic.LoginActivity");
		startActivity(intent);
		finish();
		
	}
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
 
}