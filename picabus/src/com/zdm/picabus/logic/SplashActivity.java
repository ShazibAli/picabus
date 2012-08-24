package com.zdm.picabus.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.zdm.picabus.R;
import com.zdm.picabus.facebook.PicabusFacebookObject;

public class SplashActivity extends Activity implements AnimationListener {

	PicabusFacebookObject facebookObject;
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
			Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
			intent.putExtra("loggedIn", true);
			startActivity(intent);
		}

	}

	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	public Boolean checkFacebookLogin() {

		facebookObject = PicabusFacebookObject.getFacebookInstance();
		facebookObject.mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = facebookObject.mPrefs.getString("access_token",
				null);
		long expires = facebookObject.mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebookObject.facebook.setAccessToken(access_token);
		}

		if (expires != 0) {
			facebookObject.facebook.setAccessExpires(expires);
		}

		if (facebookObject.facebook.isSessionValid()) {// access token valid-no
														// need to login
			// update user info
			updateProfileInformation();
			return true;
		} else {
			return false;
		}

	}

	public void updateProfileInformation() {
		facebookObject.mAsyncRunner.request("me", new RequestListener() {

			public void onComplete(String response, Object state) {

				String name;
				String facebookId;
				Log.d("Profile", response);
				String json = response;
				try {
					JSONObject profile = new JSONObject(json);
					// getting name of the user
					name = profile.getString("name");

					// getting facebook id of the user
					facebookId = profile.getString("id");

					facebookObject.name = name;
					facebookObject.facebookId = facebookId;

					// get the picture
					// facebookObject.updateProfilePicture(facebookObject.facebookId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			public void onIOException(IOException e, Object state) {
			}

			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}
}