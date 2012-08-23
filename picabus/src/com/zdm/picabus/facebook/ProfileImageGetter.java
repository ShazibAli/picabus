package com.zdm.picabus.facebook;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

	public class ProfileImageGetter  extends AsyncTask<String, Integer, Bitmap> {
	    
		Context context;
		String facebookId;
		
		public ProfileImageGetter(Context c) {
			this.context=c;
		}
		
	        @Override
	        protected void onPostExecute(Bitmap result) {
	             PicabusFacebookObject facebookObject = PicabusFacebookObject.getFacebookInstance();
	             facebookObject.setProfilePicture(result);
	             //Goto mypicabus page
	             Intent intent = new Intent(
							"com.zdm.picabus.facebook.MyPicabusActivity");
				 context.startActivity(intent);
	        }

			@Override
			public Bitmap doInBackground(String... params) {
				// TODO Auto-generated method stub
				PicabusFacebookObject facebookObject = PicabusFacebookObject.getFacebookInstance();
				String facebookId = facebookObject.getFacebookId();
				
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
	

