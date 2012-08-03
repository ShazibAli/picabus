package com.zdm.picabus;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class GetRout extends Activity {


ImageView imView;

/** Called when the activity is first created. */ 
@Override
public void onCreate(Bundle icicle) {
super.onCreate(icicle);
setContentView(R.layout.get_rout);

imView = (ImageView)findViewById(R.id.imview);
String extStorageDirectory = Environment.getExternalStorageDirectory().toString(); 
//Bitmap bmImg = (Bitmap) getIntent().getParcelableExtra("BitmapImage");
//Bitmap imageRes = BitmapFactory.decodeResource(getResources(),R.drawable.ab);

Bitmap imageRes = BitmapFactory.decodeFile(extStorageDirectory+"/"+"route.PNG");
imView.setImageBitmap(imageRes);
/*Bitmap imageRes2 = BitmapFactory.decodeFile(extStorageDirectory+"//"+"route.PNG");
imView.setImageBitmap(imageRes2);
Bitmap imageRes3 = BitmapFactory.decodeFile("/sdcard/route.PNG");
imView.setImageBitmap(imageRes3);*/
}

}
//Read more: http://getablogger.blogspot.com/2008/01/android-download-image-from-server-and.html#ixzz1tBhTsIvP*/