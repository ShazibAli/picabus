package com.zdm.picabus.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.zdm.picabus.R;

/**
 * 
 * login activity- user should choose if to login using facebook or not
 * 
 */
public class LoginActivity extends Activity {

	//private boolean loginActivity=true;
	//private final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
	private Button dontLoginBtn;
	//private LoginButton mLoginButton;
	private ImageButton gotoMyPicabusBtn;
	//private String[] permissions = {};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);

		// facebook login UI and functionality definitions
		//initFacebookSessionAndDefinitions();

		// dont login UI and definition
		dontLoginToFacebookUiAndDef();
		gotoMyPicabusUiAndDef();
	}

	private void gotoMyPicabusUiAndDef() {
		
		gotoMyPicabusBtn = (ImageButton) findViewById(R.id.loginAtMyPicabusButton);
		
		gotoMyPicabusBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(
						"com.zdm.picabus.facebook.MyPicabusPageActivity");
				intent.putExtra("fromLogin", true);
				startActivity(intent);
				finish();
			}
		});
		
	}

	/**
	 * init dontlogin button and functionality
	 */
	private void dontLoginToFacebookUiAndDef() {

		dontLoginBtn = (Button) findViewById(R.id.dontLoginBtn);
		dontLoginBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
				startActivity(intent);
				finish();
			}
		});
	}

/*	*//**
	 * Create facebook objects, restore session if exists, define login/logout
	 * actions and get user's data
	 *//*
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
		mLoginButton = (LoginButton) findViewById(R.id.facebookLoginButton);
		mLoginButton.init(this, AUTHORIZE_ACTIVITY_RESULT_CODE,
				FacbookIdentity.mFacebook, permissions);
	}

	*//**
	 * Request user details (to get user id)
	 *//*
	public void requestUserData() {
		FacbookIdentity.mAsyncRunner.request("me", new UserRequestListener());
	}

	*//**
	 * Callback for fetching current user's id.
	 *//*
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

	*//**
	 * The Callback for notifying the application when authorization succeeds or
	 * fails.
	 *//*
	public class FbAPIsAuthListener implements AuthListener,Serializable{

		*//**
		 * 
		 *//*
		private static final long serialVersionUID = 1L;

		public void onAuthSucceed() {
			if (loginActivity){
			requestUserData();
			Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
			startActivity(intent);
			finish();
			}
		}

		public void onAuthFail(String error) {
			if (loginActivity){
			Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
			startActivity(intent);
			finish();
			}
		}
	}

	*//**
	 * The Callback for notifying the application when log out starts and
	 * finishes.
	 *//*

	public class FbAPIsLogoutListener implements LogoutListener {

		public void onLogoutBegin() {
		}
		public void onLogoutFinish() {
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
	}*/

}
