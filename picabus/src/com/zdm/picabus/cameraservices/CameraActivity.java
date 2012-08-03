package com.zdm.picabus.cameraservices;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdm.picabus.R;

public class CameraActivity extends Activity {
    /** Called when the activity is first created. */
    
	private static final int CAMERA_PIC_REQUEST = 1337;
	private ImageView imageView;
	private Button btnTakePic;
	private TextView txtvwText;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        
        getViewsById();
        
        this.btnTakePic.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				  Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST); 
				
			}
		});

    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_PIC_REQUEST) {  
            // do something  
        	Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        	  
        	//imageView.setImageBitmap(thumbnail); 
        }  
    } 
    
    private void getViewsById() {
    	imageView = (ImageView) findViewById(R.id.photoResultView);
    	btnTakePic = (Button) findViewById(R.id.btnPicture);
    	txtvwText = (TextView) findViewById(R.id.textView1);
    }
}