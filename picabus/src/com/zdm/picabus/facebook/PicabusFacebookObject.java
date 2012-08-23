package com.zdm.picabus.facebook;

import java.io.InputStream;
import java.net.URL;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

public class PicabusFacebookObject {
	
	private final String FACEBOOK_PICABUS_APP_ID = "271655816273062";

	private static PicabusFacebookObject instance = null;
	public Facebook facebook;
	public AsyncFacebookRunner mAsyncRunner;
	public SharedPreferences mPrefs;

	//user details
	String name;
	String email;
	String facebookId;
	Bitmap profilePicture;
	
	/**
	 * Private constructor
	 */
	private PicabusFacebookObject() {
		facebook = new Facebook(FACEBOOK_PICABUS_APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
	}

	/**
	 * Get instance - for singelton implementation
	 * 
	 * @return
	 */
	public static PicabusFacebookObject getFacebookInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new PicabusFacebookObject();
			return instance;
		}
	}

	public static Boolean isLoggedInToFacebook(){
		if (instance==null){
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * Getter for facebook id
	 * 
	 * @return facebookId
	 */
	public String getFacebookId() {
		return facebookId;
	}
	
	/**
	 * Setter for facebook id
	 * @param name
	 */
	public void setFacebookId(String facebookId){
		this.facebookId=facebookId;
	}
	
	/**
	 * Getter for name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Setter for name
	 * @param name
	 */
	public void setName(String name){
		this.name=name;
	}
	
	/**
	 * Getter for email
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Setter for email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email=email;
	}
	
	
	/**
	 * Getter for profile picture
	 * @return
	 */
	public Bitmap getProfilePicture(){
		
		return profilePicture;
	}
	
	
	/**
	 * Setter for profile picture
	 * @param picture
	 */
	public void setProfilePicture(Bitmap picture){
		this.profilePicture= picture;
	}
	
	
	
	
	
	
	/**
	 * Getter for facebook field
	 * 
	 * @return FacebookObject
	 */
	public Facebook getFacebook() {
		return facebook;
	}

	/**
	 * Setter for facebook field
	 * 
	 * @param facebook
	 */

	public void setFacebook(Facebook facebook) {
		this.facebook = facebook;
	}

	/**
	 * Getter for AsyncFacebookRunner field
	 * 
	 * @return
	 */
	public AsyncFacebookRunner getAsyncFacebookRunner() {
		return mAsyncRunner;
	}

	/**
	 * Setter for AsyncFacebookRunner field
	 * 
	 * @param mAsyncRunner
	 */
	public void setAsyncFacebookRunner(AsyncFacebookRunner mAsyncRunner) {
		this.mAsyncRunner = mAsyncRunner;
	}

	/**
	 * Getter for SharedPreferences field
	 * 
	 * @return mPrefs
	 */
	public SharedPreferences getSharedPreferences() {
		return mPrefs;
	}

	/**
	 * Setter for SharedPreferences field
	 * 
	 * @param mPrefs
	 */

	public void setSharedPreferences(SharedPreferences mPrefs) {
		this.mPrefs = mPrefs;
	}

	
	/**
	 * Get information from facebook regarding to the user
	 */
	
/*	public void updateProfileInformation() {
		mAsyncRunner.request("me", new RequestListener() {

			public void onComplete(String response, Object state) {

				Log.d("Profile", response);
				String json = response;
				try {
					JSONObject profile = new JSONObject(json);
					// getting name of the user
					name = profile.getString("name");
					// getting email of the user
					email = profile.getString("email");
					// getting facebook id of the user
					facebookId = profile.getString("id");
										
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
	}*/
	
	
	/**
	 * Function loads the users facebook profile pic
	 * 
	 * @param userID
	 */
	/*public void updateProfilePicture(String userID) {
	    
	    Thread t = new Thread(new PicabusFacebookObject());
	    t.start();

	}*/
	
	/**
	 * get profile picture - thread function
	 *//*
    public void run()
    {
        try
        {
        	String imageURL;
    	    Log.d("TAG", "Loading Picture");
    	    imageURL = "http://graph.facebook.com/"+facebookId+"/picture?type=small";
    	    
        	Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageURL).getContent());
    	    if (bitmap!=null){
    	    	profilePicture=bitmap;
    	    }
        }
        catch(Exception e)
        {
	        Log.d("TAG", "Loading Picture FAILED");
	        e.printStackTrace();
        }
    }*/
	
	
}
