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
import android.widget.Button;
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
import com.zdm.picabus.facebook.SessionEvents.AuthListener;
import com.zdm.picabus.facebook.SessionEvents.LogoutListener;
import com.zdm.picabus.facebook.BaseDialogListener;

public class MyPicabusPageActivity extends Activity {

	private LoginButton mLoginButton;
	private ImageButton postToFacebookBtn;
	private Button sendAppRequest;
	private TextView mText;
	private ImageView mUserPic;
	private Handler mHandler;
	private Context c = this;
	private final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
	private ProgressDialog pd;
	TextView userPoints;
	private boolean cameFromLoginPage=false;
	
	// String[] permissions = {"publish_stream"};
	String[] permissions = {};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_picabus_screen);

		mHandler = new Handler();

		mText = (TextView) MyPicabusPageActivity.this
				.findViewById(R.id.txtFacebookUser);
		mUserPic = (ImageView) MyPicabusPageActivity.this
				.findViewById(R.id.facebookProfilePic);
		mLoginButton = (LoginButton) findViewById(R.id.login);
		userPoints = (TextView) findViewById(R.id.pointsMyPicabus);

		// Create facebook objects, restore session if exists, define
		// login/logout actions and get user's data
		initFacebookSessionAndDefinitions();

		// set post to wall and send app request buttons
		setPostAndAppRequestFunctionality();

		// get user's score - result will update UI
		// TODO: uncomment inside!
		getUserScore();
		
		getBooleanCameFromLoginPage();

	}

	/**
	 * set field 'cameFromLoginPage' to true if came from login page. false otherwise
	 */
	private void getBooleanCameFromLoginPage() {
		Intent currIntent=getIntent();
		cameFromLoginPage = currIntent.getBooleanExtra("fromLogin", false);
	}

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		// activity result from authorization flow
		case AUTHORIZE_ACTIVITY_RESULT_CODE: {
			FacbookIdentity.mFacebook.authorizeCallback(requestCode, resultCode,
					data);
			break;
		}

		}
	}

	/**
	 * Set UI and functionality of 'post to wall' and 'send app request'
	 */
	private void setPostAndAppRequestFunctionality() {

		// post to wall button
		postToFacebookBtn = (ImageButton) findViewById(R.id.postToFacebook);
		postToFacebookBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				postToWall();
			}
		});

		// send app request
		sendAppRequest = (Button) findViewById(R.id.sendAppRequest);
		sendAppRequest.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				sendAppRequest();
			}
		});

	}

	/**
	 * get user's points using async task update UI from async task afterwards
	 */
	private void getUserScore() {
		pd = new ProgressDialog(this);
		// TODO:uncomment and check when server available
		// ihc = HttpCaller.getInstance();
		// ihc.getUserScore(c, pd, facebookObject.getFacebookId());

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
		mLoginButton.init(this, AUTHORIZE_ACTIVITY_RESULT_CODE,
				FacbookIdentity.mFacebook, permissions);

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
			Util.showAlert(this, "Warning", "You must first log in.");
		} else {
			FacbookIdentity.mFacebook.dialog(this, "feed", new DialogListener() {

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
	 *  Send an app request to friends
	 * 
	 */

	protected void sendAppRequest() {
		if (!FacbookIdentity.mFacebook.isSessionValid()) {
			Util.showAlert(this, "Warning", "You must first log in.");
		} else {
			Bundle params = new Bundle();
			params.putString("message", "Hi, I'm using Picabus when waiting for a bus!");// getString(R.string.request_message));
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

				// final String picURL = jsonObject.getString("picture");
				final String name = jsonObject.getString("name");
				FacbookIdentity.userUID = jsonObject.getString("id");

				mHandler.post(new Runnable() {
					public void run() {
						mText.setText("Welcome " + name + "!");
						ProfileImageGetter profilePicRequest = new ProfileImageGetter(
								c);
						profilePicRequest.execute();
					}
				});

			} catch (JSONException e) {
				// TODO Auto-generated catch block
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
			mText.setText("You have logged out! ");
			mUserPic.setImageBitmap(null);
		}
	}

	/**
	 * Request user name and picture to show on 'my picabus'.
	 */
	public void requestUserData() {
		mText.setText("Fetching username and profile picture...");
		Bundle params = new Bundle();
		params.putString("fields", "name, picture");
		FacbookIdentity.mAsyncRunner.request("me", params,
				new UserRequestListener());
	}
	
	@Override
	public void onBackPressed() {

		if (cameFromLoginPage){
			Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
			startActivity(intent);
			finish();
		}
		else{
		super.onBackPressed();
		}
	}

}
