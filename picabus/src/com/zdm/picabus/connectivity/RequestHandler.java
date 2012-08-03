package com.zdm.picabus.connectivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class RequestHandler {

	public static void sendData(String time, Bitmap image, double LATITUDE,
			double LONGITUDE) {

		HttpClient httpclient = new DefaultHttpClient();
		String address = "http://picabusapp.appspot.com/picabusserver?mirror=true";
		HttpPost httppost = new HttpPost(address);

		try {
			// build xml
			StringBuilder sb = new StringBuilder();
			sb.append("<string>").append("the_command").append("</string>");
			sb.append("<ArrayOfString>");
			sb.append("<string>").append("the_command").append("</string>");
			sb.append("</ArrayOfString>");

			// set xml
			StringEntity entity = new StringEntity(sb.toString(), "UTF-8");
			entity.setContentType("text/xml");
			httppost.setEntity(entity);
			// set http headers
			httppost.addHeader("Accept", "application/xml");
			httppost.addHeader("Task-name", "getDepartureTimes");

			// add the file
			/*
			 * ByteArrayOutputStream bos = new ByteArrayOutputStream();
			 * image.compress(CompressFormat.JPEG, 75, bos); byte[] data =
			 * bos.toByteArray(); ByteArrayBody bab = new ByteArrayBody(data,
			 * "forest.jpg");
			 */

			// send request and get response
			HttpResponse httpresponse = httpclient.execute(httppost);
			// HttpEntity resEntity = httpresponse.getEntity();
			// tvData.setText(EntityUtils.toString(resEntity));
			String responseXml = EntityUtils.toString(httpresponse.getEntity());

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ee) {
			// TODO Auto-generated catch block
			ee.printStackTrace();
		}

	}
	
	
	public static void sendImage(Bitmap busSign) {
		InputStream is;
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream();

		busSign.compress(Bitmap.CompressFormat.JPEG, 90, bao);

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
			
			Toast.makeText(null, is.toString(), Toast.LENGTH_SHORT).show();

		} catch (Exception e) {

			Log.e("log_tag", "Error in http connection " + e.toString());

		}
		
		
		
	}

}
