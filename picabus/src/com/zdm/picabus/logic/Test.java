package com.zdm.picabus.logic;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.zdm.picabus.R;

public class Test extends Activity {

	InputStream is;

	@Override
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);

	//	setContentView(R.layout.main);

		Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

		ByteArrayOutputStream bao = new ByteArrayOutputStream();

		bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);

		byte[] ba = bao.toByteArray();

		String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);

		ArrayList<NameValuePair> nameValuePairs = new

		ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("image", ba1));

		try {

			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new

			HttpPost("http://localhost:8888/picabusserver");

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();

			is = entity.getContent();

		} catch (Exception e) {

			Log.e("log_tag", "Error in http connection " + e.toString());

		}

	}

}
