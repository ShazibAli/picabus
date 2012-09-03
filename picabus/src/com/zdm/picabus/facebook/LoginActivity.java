package com.zdm.picabus.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.zdm.picabus.R;

/**
 * 
 * login activity- user should choose if to login using facebook or not
 * 
 */
public class LoginActivity extends Activity {

	private Button dontLoginBtn;
	private ImageButton gotoMyPicabusBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_screen);

		// dont login UI and definition
		dontLoginToFacebookUiAndDef();
		
		// goto my picabus to login - UI and definitions
		gotoMyPicabusUiAndDef();
	}

	/**
	 * init 'goto my picabus' button UI and functionality
	 */
	private void gotoMyPicabusUiAndDef() {
		
		gotoMyPicabusBtn = (ImageButton) findViewById(R.id.loginAtMyPicabusButton);
		
		gotoMyPicabusBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(
						"com.zdm.picabus.facebook.MyPicabusPageActivity");
				intent.putExtra("fromLogin", true);
				startActivity(intent);
				finish();
			}
		});
		
	}

	/**
	 * init dontlogin button and functionality
	 */
	private void dontLoginToFacebookUiAndDef() {

		dontLoginBtn = (Button) findViewById(R.id.dontLoginBtn);
		dontLoginBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("com.zdm.picabus.MAINSCREEN");
				startActivity(intent);
				finish();
			}
		});
	}

}
