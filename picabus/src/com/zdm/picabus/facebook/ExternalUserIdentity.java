package com.zdm.picabus.facebook;

import android.graphics.Bitmap;

/**
 * 
 * Used to store name and picture of different facebook users
 * 
 */
public class ExternalUserIdentity {

	String userName;
	Bitmap profilePic;

	/**
	 * constructor
	 * 
	 * @param userName
	 * @param profilePic
	 */
	public ExternalUserIdentity(String userName, Bitmap profilePic) {
		this.userName = userName;
		this.profilePic = profilePic;
	}

	/**
	 * getter for user name
	 * 
	 * @return username
	 */

	public String getUserName() {
		return userName;
	}

	/**
	 * getter for profile pic
	 * 
	 * @return profilePic
	 */
	public Bitmap getUserProfilePic() {
		return profilePic;
	}
}
