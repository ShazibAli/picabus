package com.zdm.picabus.facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Class is used for getting user's facebook profile image from network, using
 * async task
 * 
 */
public class UserNamePicGetter extends
		AsyncTask<String, Integer, ExternalUserIdentity> {

	private TextView reporterNameTextView; 
	private ImageView reporterImageView;
	private String facebookId;

	public UserNamePicGetter(String userId, TextView reporterNameTextView, ImageView reporterImageView) {
		
		this.facebookId = userId;
		this.reporterImageView = reporterImageView;
		this.reporterNameTextView = reporterNameTextView;
	}

	@Override
	protected void onPostExecute(ExternalUserIdentity result) {

		String userName;
		Bitmap profilePic;

		// get user name and profile pic
		userName = result.getUserName();
		profilePic = result.getUserProfilePic();

		if (userName != null) {
			reporterNameTextView.setText(userName);
		}
		if (profilePic != null) {
			reporterImageView.setImageBitmap(profilePic);
		}
		
		

	}

	@Override
	public ExternalUserIdentity doInBackground(String... params) {

		Bitmap profileImageBitmap = null;
		String name = null;

		// get profile picture
		profileImageBitmap = getProfilePic(); 
		
		// get user name
		name = getUserName();

		return new ExternalUserIdentity(name, profileImageBitmap);
	}

	/**
	 * get username according to id
	 * @return username
	 */
	private String getUserName() {
		InputStream responseStream;
		JSONObject jsonObject;
		String name= null;
		
		try {
			Log.d("TAG", "Getting user name");
			URL facebookGraphUrl = new URL("http://graph.facebook.com/"
					+ facebookId);
			responseStream = facebookGraphUrl.openConnection().getInputStream();
			
			// Read response to string
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					responseStream, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			responseStream.close();
			String responseStr = sb.toString();

			// Convert string to object
			jsonObject = new JSONObject(responseStr);
			name = jsonObject.getString("name");
		} catch (IOException e) {
			Log.d("TAG", "Getting user name FAILED");
			e.printStackTrace();
		} catch (JSONException e) {
			Log.d("TAG", "Getting user name FAILED");
			e.printStackTrace();
		}
		
		return name;
	}

	/**
	 * get profile picture
	 * @return profile picture
	 */
	private Bitmap getProfilePic() {
		
		Bitmap pic = null;
		
		try {
			Log.d("TAG", "Loading Picture");
			URL facebookGraphUrl = new URL("http://graph.facebook.com/"
					+ facebookId + "/picture?type=small");
			pic = BitmapFactory.decodeStream(facebookGraphUrl
					.openConnection().getInputStream());
		} catch (MalformedURLException e) {
			Log.d("TAG", "Loading Picture FAILED");
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("TAG", "Loading Picture FAILED");
			e.printStackTrace();
		}
		
		return pic;
	}

}