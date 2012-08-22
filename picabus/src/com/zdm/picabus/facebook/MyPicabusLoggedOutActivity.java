package com.zdm.picabus.facebook;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.zdm.picabus.R;
import com.zdm.picabus.utilities.ErrorsHandler;

public class MyPicabusLoggedOutActivity extends Activity implements Runnable{

	ImageButton login;
	Context c;
	PicabusFacebookObject facebookObject;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		c=this;
		setContentView(R.layout.my_picabus_logged_out_screen);
		login = (ImageButton) findViewById(R.id.loginFacebookMyPicabus);	
		login.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v) {
			if (PicabusFacebookObject.isLoggedInToFacebook()==false){
				loginToFacebook();
			}
			else{
				ErrorsHandler.createFacebookMyPicabusLoginErrorAlert(c);
			}
	}
		});
	}
	/**
	 * Login the user's facebook acount
	 */
	public void loginToFacebook() {
		
		facebookObject = PicabusFacebookObject.getFacebookInstance();
		
		
		facebookObject.mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = facebookObject.mPrefs.getString("access_token", null);
		long expires = facebookObject.mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebookObject.facebook.setAccessToken(access_token);
		}

		if (expires != 0) {
			facebookObject.facebook.setAccessExpires(expires);
		}

         //Only call authorize if the access_token has expired.
		if (!facebookObject.facebook.isSessionValid()) {
			facebookObject.facebook.authorize(this,
				//	new String[] { "email", "publish_stream" }, //update permission according to what we will use
					new DialogListener() {

						public void onCancel() {
						}

						public void onComplete(Bundle values) {
							// Function to handle complete event
							// Edit Preferences and update facebook acess_token
							SharedPreferences.Editor editor = facebookObject.mPrefs.edit();
							editor.putString("access_token",
									facebookObject.facebook.getAccessToken());
							editor.putLong("access_expires",
									facebookObject.facebook.getAccessExpires());
							editor.commit();

							updateProfilePicture(facebookObject.facebookId);
							//open mypicabus for logged in users
							Intent intent = new Intent(
									"com.zdm.picabus.facebook.MyPicabusActivity");
							startActivity(intent);
							finish();//finish current activity

						}

						public void onError(DialogError error) {
							// Function to handle error
							ErrorsHandler.createFacebookMyPicabusLoginErrorAlert(c);
						}

						public void onFacebookError(FacebookError fberror) {
							ErrorsHandler.createFacebookMyPicabusLoginErrorAlert(c);
						}

					});
		}
		else{//access_token still valid, don't call authorize
			//should be logged in but appears as not
			ErrorsHandler.createFacebookMyPicabusLoginErrorAlert(c);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebookObject.facebook.authorizeCallback(requestCode, resultCode, data);
	}

	
	/**
	 * Function loads the users facebook profile pic
	 * 
	 * @param userID
	 */
	public void updateProfilePicture(String userID) {
	    
	    Thread t = new Thread(new MyPicabusLoggedOutActivity());
	    t.start();

	}
	
	/**
	 * get profile picture - thread function
	 */
    public void run()
    {
        try
        {
        	String imageURL;
    	    Log.d("TAG", "Loading Picture");
    	    imageURL = "http://graph.facebook.com/"+facebookObject.facebookId+"/picture?type=small";
    	    
        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageURL).getContent());
    	    if (bitmap!=null){
    	    	facebookObject.profilePicture=bitmap;
    	    }
        }
        catch(Exception e)
        {
	        Log.d("TAG", "Loading Picture FAILED");
	        e.printStackTrace();
        }
    }
	
	
	
	
	
	
	
}
