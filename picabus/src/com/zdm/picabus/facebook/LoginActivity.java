package com.zdm.picabus.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;
import com.zdm.picabus.R;
import com.zdm.picabus.facebook.PicabusFacebookObject;
import com.zdm.picabus.utilities.ErrorsHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * 
 * login activity- user should choose if to login using facebook or not
 * 
 */
public class LoginActivity extends Activity {

	ImageButton facebookLogin;
	Button dontLoginBtn;

	Context c;
	PicabusFacebookObject facebookObject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		c = this;

		facebookLogin = (ImageButton) findViewById(R.id.facebookLoginButton);
		dontLoginBtn = (Button) findViewById(R.id.dontLoginBtn);

		facebookLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// facebook login
				facebookObject = PicabusFacebookObject.getFacebookInstance();
				loginToFacebook();
			}

		});

		dontLoginBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Open facebook login activity
				Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
				startActivity(intent);
				finish();
			}

		});
	}

	/**
	 * Login the user's facebook acount
	 */
	public void loginToFacebook() {

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

		// Only call authorize if the access_token has expired.
		if (!facebookObject.facebook.isSessionValid()) {
			facebookObject.facebook.authorize(this, new String[] { "email",
					"publish_stream", "user_about_me" }, // update permission
															// according to what
															// we will use
					new DialogListener() {

						public void onCancel() {
							// Function to handle cancel event

						}

						public void onComplete(Bundle values) {
							// Function to handle complete event
							// Edit Preferences and update facebook acess_token
							SharedPreferences.Editor editor = facebookObject.mPrefs
									.edit();
							editor.putString("access_token",
									facebookObject.facebook.getAccessToken());
							editor.putLong("access_expires",
									facebookObject.facebook.getAccessExpires());
							editor.commit();

							// get information regarding to user
							updateProfileInformation();
							// facebookObject.updateProfilePicture(facebookObject.facebookId);

							// goto main menu activity and close current
							// activities
							Intent intent = new Intent(
									"com.zdm.picabus.MAINSCREEN");
							intent.putExtra("loggedIn", true);
							startActivity(intent);
							finish();

						}

						public void onError(DialogError error) {
							// Function to handle error
							ErrorsHandler.createFacebookFirstLoginErrorAlert(c);
						}

						public void onFacebookError(FacebookError fberror) {
							ErrorsHandler.createFacebookFirstLoginErrorAlert(c);
						}

					});
		} else {// access_token still valid, don't call authorize

			// update user info
			updateProfileInformation();
			// facebookObject.updateProfilePicture(facebookObject.facebookId);

			// goto main menu activity and close all other activities
			Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
			intent.putExtra("loggedIn", true);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * Get information from facebook regarding to the user
	 */

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebookObject.facebook
				.authorizeCallback(requestCode, resultCode, data);
	}

}
