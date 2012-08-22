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
import com.zdm.picabus.utilities.ErrorsHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MyPicabusActivity extends Activity {

	
	int numPoints=25;//get from app
	
	Context c;
	PicabusFacebookObject facebookObject;
	
	//layout objects
	ImageView profilePic;
	TextView name;
	ImageButton postToFacebookBtn;
	ImageButton logoutFacebookBtn;
	TextView userPoints;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_picabus_screen);
			
		
		//Get the facebook instance
		facebookObject = PicabusFacebookObject.getFacebookInstance();
		c=this;
		updateProfileInformation();
		

		//Name
		name = (TextView) findViewById(R.id.facebookUsername);
		name.setText(facebookObject.getName());
		
		//user's points
		userPoints = (TextView) findViewById(R.id.pointsMyPicabus);
		userPoints.setText("You have earned "+numPoints+" points!");
		
		//post to wall button
		postToFacebookBtn = (ImageButton) findViewById(R.id.postToFacebook);
		postToFacebookBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				postToWall();
			}
		});
		
		//logout
		logoutFacebookBtn = (ImageButton) findViewById(R.id.logoutFacebook);
		logoutFacebookBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				logoutFromFacebook();
			}
		});
		
		//profile picture
		profilePic = (ImageView) findViewById(R.id.facebookProfilePic);
		profilePic.setImageBitmap(facebookObject.getProfilePicture());
		
		
		
	}



	
	/**
	 * Post to user's facebook wall
	 */
	public void postToWall() {
		// post on user's wall.
		facebookObject.facebook.dialog(this, "feed", new DialogListener() {

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
	 * Logout user from facebook
	 */
	public void logoutFromFacebook() {
		facebookObject.mAsyncRunner.logout(this, new RequestListener() {

			public void onComplete(String response, Object state) {
				Log.d("Logout from Facebook", response);
				if (Boolean.parseBoolean(response) == true) {
					// User successfully Logged out
					Intent intent = new Intent(
							"com.zdm.picabus.facebook.MyPicabusLoggedOutActivity");
					startActivity(intent);
					finish();
				}
			}

			public void onIOException(IOException e, Object state) {
				ErrorsHandler.createFacebookMyPicabusLogoutErrorAlert(c);
			}

			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
				ErrorsHandler.createFacebookMyPicabusLogoutErrorAlert(c);
			}

			public void onMalformedURLException(MalformedURLException e,
					Object state) {
				ErrorsHandler.createFacebookMyPicabusLogoutErrorAlert(c);
			}

			public void onFacebookError(FacebookError e, Object state) {
				ErrorsHandler.createFacebookMyPicabusLogoutErrorAlert(c);
			}
		});
	}

	/**
	 * Get information from facebook regarding to the user
	 */
	
	public void updateProfileInformation() {
		facebookObject.mAsyncRunner.request("me", new RequestListener() {

			public void onComplete(String response, Object state) {

				Log.d("Profile", response);
				String json = response;
				try {
					JSONObject profile = new JSONObject(json);
					// getting name of the user
					facebookObject.name = profile.getString("name");
					// getting email of the user
					facebookObject.email = profile.getString("email");
					// getting facebook id of the user
					facebookObject.facebookId = profile.getString("id");
										
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

		facebookObject.facebook.authorizeCallback(requestCode, resultCode, data);
	}

}

