package com.zdm.picabus.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.zdm.picabus.utilities.ErrorsHandler;

/**
 * 
 * Facebook activity, contains login to facebook and other facebook features
 *
 */
public class FacebookLoginActivity extends Activity {

	private final String FACEBOOK_PICABUS_APP_ID = "271655816273062";

	Context c;
	Facebook facebook;
	AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";//
	private SharedPreferences mPrefs;//

	// mAsyncRunner = new AsyncFacebookRunner(facebook);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);
		c=this;
		facebook = new Facebook(FACEBOOK_PICABUS_APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);

		loginToFacebook();

	}



	/**
	 * Login the user's facebook acount
	 */
	public void loginToFacebook() {
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

         //Only call authorize if the access_token has expired.
		if (!facebook.isSessionValid()) {
			facebook.authorize(this,
				//	new String[] { "email", "publish_stream" }, //update permission according to what we will use
					new DialogListener() {

						public void onCancel() {
							// Function to handle cancel event
							finish();
						}

						public void onComplete(Bundle values) {
							// Function to handle complete event
							// Edit Preferences and update facebook acess_token
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();

							//goto main menu activity and close all other activities
							Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();

						}

						public void onError(DialogError error) {
							// Function to handle error
							ErrorsHandler.createFacebookErrorAlert(c);
						}

						public void onFacebookError(FacebookError fberror) {
							ErrorsHandler.createFacebookErrorAlert(c);
						}

					});
		}
		else{//access_token still valid, don't call authorize
			//goto main menu activity and close all other activities
			Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	/**
	 * Post to user's facebook wall
	 */
	public void postToWall() {
		// post on user's wall.
		facebook.dialog(this, "feed", new DialogListener() {

			public void onFacebookError(FacebookError e) {
			}

			public void onError(DialogError e) {
			}

			public void onComplete(Bundle values) {
			}

			public void onCancel() {
			}
		});

	}

	/**
	 * Get information from facebook regarding to the user
	 */
	public void getProfileInformation() {
		mAsyncRunner.request("me", new RequestListener() {

			public void onComplete(String response, Object state) {
				final String name;
				final String email;

				Log.d("Profile", response);
				String json = response;
				try {
					JSONObject profile = new JSONObject(json);
					// getting name of the user
					name = profile.getString("name");
					// getting email of the user
					email = profile.getString("email");

					runOnUiThread(new Runnable() {

						public void run() {
							Toast.makeText(getApplicationContext(),
									"Name: " + name + "\nEmail: " + email,
									Toast.LENGTH_LONG).show();
						}

					});

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

	/**
	 * Logout user from facebook
	 */
	public void logoutFromFacebook() {
		mAsyncRunner.logout(this, new RequestListener() {

			public void onComplete(String response, Object state) {
				Log.d("Logout from Facebook", response);
				if (Boolean.parseBoolean(response) == true) {
					// User successfully Logged out
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

		facebook.authorizeCallback(requestCode, resultCode, data);
	}
}