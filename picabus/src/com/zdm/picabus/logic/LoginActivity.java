package com.zdm.picabus.logic;

import com.zdm.picabus.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * 
 * login activity- user should choose if to login using facebook or not
 *
 */
public class LoginActivity extends Activity {
	
	ImageButton facebookLogin;
	Button dontLoginBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);
		
		facebookLogin = (ImageButton) findViewById(R.id.facebookLoginButton);
		dontLoginBtn = (Button) findViewById(R.id.dontLoginBtn);
		
		facebookLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//Open facebook login activity
				Intent intent = new Intent(
						"com.zdm.picabus.facebook.FacebookLoginActivity");
				startActivity(intent);
				finish();
			}
			
		});
		
		dontLoginBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//Open facebook login activity
            	Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
        		startActivity(intent);
			}
			
		});
	}
}
