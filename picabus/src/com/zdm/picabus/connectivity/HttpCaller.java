package com.zdm.picabus.connectivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.os.Looper;

public class HttpCaller {

	public static final String serverURL = "http://picabusapp.appspot.com/";

	public static String readContentFromIS(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(in, "UTF-8"), 8);
		StringBuffer sb = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		in.close();
		return sb.toString();		
	}
	


	public static void getDepartureTime(final int lineNumber, final double latitude, final double longitude, final String clientTime, final int timeInterval) {
 
		Thread t = new Thread() {
			public void run() {
				Looper.prepare(); // For Preparing Message Pool for the child Thread TODO: verify looper use
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(),
						10000); // Timeout Limit
				HttpResponse response;
				JSONObject json = new JSONObject();
				try {
					HttpPost post = new HttpPost(serverURL + "picabusserver");
					json.put("lineNumber", lineNumber);
					json.put("latitude", latitude);
					json.put("longitude", longitude);
					json.put("clientTime", clientTime);
					json.put("timeInterval", timeInterval);
					
					StringEntity se = new StringEntity(json.toString());
					post.addHeader(CustomHeader.TASK_NAME.getHeaderName(),
							Request.GET_DEPARTURE_TIMES.getTaskName());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
					post.setEntity(se);
					response = client.execute(post);
					/* Checking response */
					if (response != null) {
						InputStream in = response.getEntity().getContent(); // Get the data in the entity
						String responseContent = readContentFromIS(in);
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Error: Cannot Estabilish Connection"); // TODO: replace with popup
																		
				}
				
				Looper.loop(); // Loop in the message queue
			}
		};
		t.start();


	}



}
