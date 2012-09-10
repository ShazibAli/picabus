package com.zdm.picabus.logic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.zdm.picabus.R;

/**
 * 
 * Activity for handling 'about us'
 * 
 */
public class AboutUsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutus_screen);

		// share picabus in several medias
		ImageButton shareBtn = (ImageButton) findViewById(R.id.btnShare);

		shareBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT,
						getResources().getText(R.string.share_message));
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, getResources()
						.getText(R.string.share_title)));
			}
		});
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        Runtime.getRuntime().gc();
    }
}
