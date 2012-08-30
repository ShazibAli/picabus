package com.zdm.picabus.facebook;

import android.app.Application;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

public class FacbookIdentity extends Application {
	
	public static final String APP_ID = "271655816273062";
    public static Facebook mFacebook;
    public static AsyncFacebookRunner mAsyncRunner;
    public static String userUID = null;
 
    /**
     * 
     * @return facebook user id
     */
    public static String getUserId(){
    	return userUID;
    }
    
}
