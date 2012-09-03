package com.zdm.picabus.facebook;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.zdm.picabus.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * Class is used for getting user's facebook profile image from network, using async task
 *
 */
	public class ProfileImageGetter  extends AsyncTask<String, Integer, Bitmap> {
	    
		Context context;
		String facebookId;
		
		/**
		 * Constructor
		 * @param c - context
		 */
		public ProfileImageGetter(Context c) {
			this.context=c;
		}
		
		/**
		 * Update UI according to result
		 */
	        @Override
	        protected void onPostExecute(Bitmap result) {

	        	ImageView mUserPic = (ImageView)  ((Activity) context).findViewById(R.id.facebookProfilePic);
	        	mUserPic.setImageBitmap(result);
	        }

	        /**
	         * Get profile picture from URL
	         */
			@Override
			public Bitmap doInBackground(String... params) {
				
				String facebookId = FacbookIdentity.userUID;
				
				URL facebookGraphUrl = null;
		        Bitmap profileImageBitmap = null;
		            try {
			    	    Log.d("TAG", "Loading Picture");
		                facebookGraphUrl = new URL("http://graph.facebook.com/"+facebookId+"/picture?type=small");
		                profileImageBitmap = BitmapFactory.decodeStream(facebookGraphUrl.openConnection().getInputStream());
		            } catch (MalformedURLException e) {
				        Log.d("TAG", "Loading Picture FAILED");
		            	e.printStackTrace();
		            } catch (IOException e) {
				        Log.d("TAG", "Loading Picture FAILED");
		                e.printStackTrace();
		            }
		            return profileImageBitmap;
			}

	   
			
	}
	

