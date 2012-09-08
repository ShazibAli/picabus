/*
 * Copyright 2010 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zdm.picabus.facebook;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.zdm.picabus.R;
import com.zdm.picabus.connectivity.IHttpCaller;
import com.zdm.picabus.facebook.BaseRequestListener;
import com.zdm.picabus.facebook.SessionEvents.AuthListener;
import com.zdm.picabus.facebook.SessionEvents.LogoutListener;

public class LoginButton extends ImageButton {

    private Facebook mFb;
    private Handler mHandler;
    private SessionListener mSessionListener = new SessionListener();
    private String[] mPermissions;
    private Activity mActivity;
    private int mActivityCode;
    private Context context;
    
	private ProgressDialog pd;
	IHttpCaller ihc = null;
    
    public LoginButton(Context context) {
        super(context);
    }

    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

/*    public void init(final Activity activity, final int activityCode, final Facebook fb) {
        init(activity, activityCode, fb, new String[] {});
    }*/
    
	/**
	 * get user's points using async task update UI from async task afterwards
	 */
	private void getUserScore() {
		ihc.getUserScore(context, pd, FacbookIdentity.getUserId());
	}

    public void init(final Activity activity, final int activityCode, final Facebook fb,
            final String[] permissions, IHttpCaller ihc, ProgressDialog pd, Context c) {
    	
        mActivity = activity;
        mActivityCode = activityCode;
        mFb = fb;
        mPermissions = permissions;
        mHandler = new Handler();
        this.ihc=ihc;
        this.pd=pd;
        this.context=c;
        
        setBackgroundColor(Color.TRANSPARENT);
        //login / logout image icon
        setImageResource(fb.isSessionValid() ? R.drawable.facebook_logout : R.drawable.facebook_login);
        drawableStateChanged();

        SessionEvents.addAuthListener(mSessionListener);
        SessionEvents.addLogoutListener(mSessionListener);
        setOnClickListener(new ButtonOnClickListener());
    }

    /**
     * Login/logout button click listener
     *
     */
    private final class ButtonOnClickListener implements OnClickListener {
    	
        public void onClick(View arg0) {
            if (mFb.isSessionValid()) {
                SessionEvents.onLogoutBegin();
                AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(mFb);
                asyncRunner.logout(getContext(), new LogoutRequestListener());
            } else {
                mFb.authorize(mActivity, mPermissions, mActivityCode, new LoginDialogListener());
                getUserScore();
            }
        }
    }

    /**
     * facebook login dialog listener
     */
    private final class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
            SessionEvents.onLoginSuccess();
        }

        public void onFacebookError(FacebookError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        public void onError(DialogError error) {
            SessionEvents.onLoginError(error.getMessage());
        }

        public void onCancel() {
            SessionEvents.onLoginError("Action Canceled");
        }
    }

    /**
     * facebook logout dialog listener
     */
    private class LogoutRequestListener extends BaseRequestListener {
        public void onComplete(String response, final Object state) {
            /*
             * callback should be run in the original thread, not the background
             * thread
             */
            mHandler.post(new Runnable() {
                public void run() {
                    SessionEvents.onLogoutFinish();
                }
            });
        }
    }

    private class SessionListener implements AuthListener, LogoutListener {

        public void onAuthSucceed() {
            setImageResource(R.drawable.facebook_logout);
            SessionStore.save(mFb, getContext());
        }

        public void onAuthFail(String error) {
        }

        public void onLogoutBegin() {
        }

        public void onLogoutFinish() {
            SessionStore.clear(getContext());
            setImageResource(R.drawable.facebook_login);
        }
    }

}
