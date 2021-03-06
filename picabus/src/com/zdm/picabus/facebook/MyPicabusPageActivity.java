package com.zdm.picabus.facebook;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;
import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.HttpCaller;
import com.zdm.picabus.connectivity.IHttpCaller;
import com.zdm.picabus.facebook.SessionEvents.AuthListener;
import com.zdm.picabus.facebook.SessionEvents.LogoutListener;
import com.zdm.picabus.facebook.BaseDialogListener;

/**
 * 
 * 'My picabus' page
 *
 */
public class MyPicabusPageActivity extends Activity {

	private LoginButton mLoginButton;
	private ImageButton postToFacebookBtn;
	private TextView mText;
	private TextView numberOfPointsTillnext;
	private ImageView mUserPic;
	private Handler mHandler;
	private ImageView medal;
	private Context c = this;
	private final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
	private ProgressDialog pd;
	TextView userPoints;
	private boolean cameFromLoginPage = false;
	IHttpCaller ihc = null;
	String[] permissions = {};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_picabus_screen);
		mHandler = new Handler();
		medal = (ImageView) findViewById(R.id.imageMedal);
		medal.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.question_mark_medal));
		mText = (TextView) MyPicabusPageActivity.this
				.findViewById(R.id.txtFacebookUser);
		mText.setText("User");
		mUserPic = (ImageView) MyPicabusPageActivity.this
				.findViewById(R.id.facebookProfilePic);
		mUserPic.setBackgroundDrawable(getApplicationContext().getResources().getDrawable(R.drawable.application_icon));
		mLoginButton = (LoginButton) findViewById(R.id.login);
		userPoints = (TextView) findViewById(R.id.pointsMyPicabus);
		userPoints.setText("0");
		numberOfPointsTillnext = (TextView) findViewById(R.id.numberOfPointsTillNext);
		numberOfPointsTillnext.setText("1000");

		// Create facebook objects, restore session if exists, define
		// login/logout actions and get user's data
		initFacebookSessionAndDefinitions();

		// set post to wall and send app request buttons
		setPostFunctionality();

		// get user's score - result will update UI
		getUserScore();

		// Check is user arrived from login page or for main menu
		// in order to handle 'back' button
		getBooleanCameFromLoginPage();

	}

	/**
	 * set field 'cameFromLoginPage' to true if came from login page. false
	 * otherwise
	 */
	private void getBooleanCameFromLoginPage() {
		Intent currIntent = getIntent();
		cameFromLoginPage = currIntent.getBooleanExtra("fromLogin", false);
	}

	/**
	 * Handle 'on resume' on activity life cycle, handle access token if needed
	 */
	@Override
	public void onResume() {
		super.onResume();
		if (FacbookIdentity.mFacebook != null) {
			if (!FacbookIdentity.mFacebook.isSessionValid()) {
				mText.setText("You are logged out! ");
				mUserPic.setImageBitmap(null);
			} else {
				FacbookIdentity.mFacebook.extendAccessTokenIfNeeded(this, null);
			}
		}
	}

	/**
	 * Facebook's activity result handler
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// activity result from authorization flow
		case AUTHORIZE_ACTIVITY_RESULT_CODE: {
			FacbookIdentity.mFacebook.authorizeCallback(requestCode,
					resultCode, data);
			break;
		}

		}
	}

	/**
	 * Set UI and functionality of 'post to wall' and 'send app request'
	 */
	private void setPostFunctionality() {

		// post to wall button
		postToFacebookBtn = (ImageButton) findViewById(R.id.postToFacebook);
		postToFacebookBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				postToWall();
			}
		});

	}

	/**
	 * get user's points using async task update UI from async task afterwards
	 */
	private void getUserScore() {
		pd = new ProgressDialog(this);

		ihc = HttpCaller.getInstance();
		ihc.getUserScore(c, pd, FacbookIdentity.getUserId());

	}

	/**
	 * Create facebook objects, restore session if exists, define login/logout
	 * actions and get user's data
	 */
	private void initFacebookSessionAndDefinitions() {

		// Create the Facebook Object using the app id.
		FacbookIdentity.mFacebook = new Facebook(FacbookIdentity.APP_ID);
		// Instantiate the asynrunner object for asynchronous api calls.
		FacbookIdentity.mAsyncRunner = new AsyncFacebookRunner(
				FacbookIdentity.mFacebook);

		// restore session if one exists
		SessionStore.restore(FacbookIdentity.mFacebook, this);
		SessionEvents.addAuthListener(new FbAPIsAuthListener());
		SessionEvents.addLogoutListener(new FbAPIsLogoutListener());

		// login logout button
		ihc = HttpCaller.getInstance();
		pd = new ProgressDialog(this);
		mLoginButton.init(this, AUTHORIZE_ACTIVITY_RESULT_CODE,
				FacbookIdentity.mFacebook, permissions,ihc,pd,c);

		// get user data if valid session
		if (FacbookIdentity.mFacebook.isSessionValid()) {
			requestUserData();
		}

	}

	/**
	 * Post to user's facebook wall
	 */
	public void postToWall() {
		// post on user's wall.
		if (!FacbookIdentity.mFacebook.isSessionValid()) {
			Util.showAlert(this, "Information", "You must first log in.");
		} else {
			FacbookIdentity.mFacebook.dialog(this, "feed",
					new DialogListener() {

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
	}

	/**
	 * Send an app request to friends
	 * 
	 */

	protected void sendAppRequest() {
		if (!FacbookIdentity.mFacebook.isSessionValid()) {
			Util.showAlert(this, "Information", "You must first log in.");
		} else {
			Bundle params = new Bundle();
			params.putString("message",
					"Hi, I'm using Picabus when waiting for a bus!");// getString(R.string.request_message));
			FacbookIdentity.mFacebook.dialog(MyPicabusPageActivity.this,
					"apprequests", params, new AppRequestsListener());
		}
	}

	/**
	 * callback for the apprequests dialog which sends an app request to user's
	 */
	public class AppRequestsListener extends BaseDialogListener {

		public void onComplete(Bundle values) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"App request sent", Toast.LENGTH_SHORT);
			toast.show();
		}

		public void onFacebookError(FacebookError error) {
			Toast.makeText(getApplicationContext(),
					"Facebook Error: " + error.getMessage(), Toast.LENGTH_SHORT)
					.show();
		}

		public void onCancel() {
			Toast toast = Toast.makeText(getApplicationContext(),
					"App request cancelled", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * Callback for fetching current user's name, picture, user id.
	 */
	public class UserRequestListener extends BaseRequestListener {

		public void onComplete(final String response, final Object state) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(response);
				final String name = jsonObject.getString("name");
				FacbookIdentity.userUID = jsonObject.getString("id");

				mHandler.post(new Runnable() {
					public void run() {
						mText.setText("Hey " + name + "!");
						ProfileImageGetter profilePicRequest = new ProfileImageGetter(
								c);
						profilePicRequest.execute();
					}
				});

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * The Callback for notifying the application when authorization succeeds or
	 * fails.
	 */
	public class FbAPIsAuthListener implements AuthListener {

		public void onAuthSucceed() {
			requestUserData();
		}

		public void onAuthFail(String error) {
			mText.setText("Failed to login to facebook");
		}
	}

	/**
	 * The Callback for notifying the application when log out starts and
	 * finishes.
	 */
	public class FbAPIsLogoutListener implements LogoutListener {

		public void onLogoutBegin() {
			mText.setText("Logging out...");
		}

		public void onLogoutFinish() {
			mText.setText("Login to use Social features ! ");
			mUserPic.setImageBitmap(null);
			// TODO: change medal and score
			numberOfPointsTillnext.setText("1000");
			userPoints.setText("0");
			medal.setImageDrawable(getResources().getDrawable(R.drawable.question_mark_medal));
		}
	}

	/**
	 * Request user name and picture to show on 'my picabus'.
	 */
	public void requestUserData() {
		Bundle params = new Bundle();
		params.putString("fields", "name, picture");
		FacbookIdentity.mAsyncRunner.request("me", params,
				new UserRequestListener());
	}

	/**
	 * Handle back button action - if user came from login page, return user to
	 * main menu
	 */
	@Override
	public void onBackPressed() {

		if (cameFromLoginPage) {
			Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
			startActivity(intent);
			finish();
		} else {
			super.onBackPressed();
		}
	}

}
