package com.zdm.picabus.logic;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.zdm.picabus.R;
import com.zdm.picabus.facebook.BaseRequestListener;
import com.zdm.picabus.facebook.FacbookIdentity;
import com.zdm.picabus.facebook.SessionStore;


public class SplashActivity extends Activity implements AnimationListener {

	Context c;

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
		LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
		l.clearAnimation();
		l.startAnimation(anim);

		anim = AnimationUtils.loadAnimation(this, R.anim.logo_entrance);
		anim.reset();
		ImageView iv = (ImageView) findViewById(R.id.logo);
		iv.clearAnimation();
		iv.startAnimation(anim);

	}

	public void onAnimationEnd(Animation animation) {
		if (!checkFacebookLogin()) {
			Intent intent = new Intent("com.zdm.picabus.facebook.LoginActivity");
			startActivity(intent);
			finish();
		} else {
			requestUserData();//get user's id
			Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
			startActivity(intent);
			finish();
		}

	}



	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	private boolean checkFacebookLogin() {

		// Create the Facebook Object using the app id.
		FacbookIdentity.mFacebook = new Facebook(FacbookIdentity.APP_ID);
		// Instantiate the asynrunner object for asynchronous api calls.
		FacbookIdentity.mAsyncRunner = new AsyncFacebookRunner(
				FacbookIdentity.mFacebook);

		// restore session if one exists
		SessionStore.restore(FacbookIdentity.mFacebook, this);

		// get user data if valid session
		if (FacbookIdentity.mFacebook.isSessionValid()) {
			return true;
		}
		else{
			return false;
		}

	}


	/**
	 * Request user details (to get user id)
	 */
	public void requestUserData() {
		FacbookIdentity.mAsyncRunner.request("me", new UserRequestListener());
	}

	/**
	 * Callback for fetching current user's id.
	 */
	public class UserRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(response);
				FacbookIdentity.userUID = jsonObject.getString("id");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		if (FacbookIdentity.mFacebook != null) {
			if (FacbookIdentity.mFacebook.isSessionValid()) {
				FacbookIdentity.mFacebook.extendAccessTokenIfNeeded(this, null);
			}
		}
	}
}