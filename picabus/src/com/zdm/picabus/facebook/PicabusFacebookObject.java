package com.zdm.picabus.facebook;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

public class PicabusFacebookObject {

	private final String FACEBOOK_PICABUS_APP_ID = "271655816273062";

	private static PicabusFacebookObject instance = null;
	public Facebook facebook;
	public AsyncFacebookRunner mAsyncRunner;
	public SharedPreferences mPrefs;

	// user details
	public String name;
	String email;
	public String facebookId;
	public Bitmap profilePicture;

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

	/*
	 * public static Boolean isLoggedInToFacebook(){ if (instance==null){ return
	 * false; } else{ return true; } }
	 */

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
	 * 
	 * @param name
	 */
	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
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
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Getter for profile picture
	 * 
	 * @return
	 */
	public Bitmap getProfilePicture() {

		return profilePicture;
	}

	/**
	 * Setter for profile picture
	 * 
	 * @param picture
	 */
	public void setProfilePicture(Bitmap picture) {
		this.profilePicture = picture;
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



}
