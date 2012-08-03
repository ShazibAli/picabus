package com.zdm.picabus.tempmoran;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ScheduleResults extends ListActivity {

	String classes[] = {"Line: 68     Estimated time: 12:45","Line: 71     Estimated time: 12:27" ,"example2" ,
						"example3" ,"example4" ,"example5" ,"example6"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(ScheduleResults.this, android.R.layout.simple_list_item_1, classes));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		
		Thread sendRequest = new Thread(new Runnable() {
			
		public void run() {
			
		/*	int line = 68;*/
			
			 String lineTmp = classes[position].split("Line: ")[1];
				String line2 = lineTmp.split("Estimated")[0];
				String line = line2.split(" ")[0];
			String lineStr = "Details:\nLine:"+line;
			String busCompany = "Dan";
			String routeRequest = lineStr + "Company:"+busCompany+"\n";
			
			Bitmap imageRes = postGetRoute("http://10.0.2.2:5001",routeRequest);

			//imView.setImageBitmap(result2);
			
			
			//Bitmap imageRes = BitmapFactory.decodeResource(getResources(),R.drawable.ab);
			String extStorageDirectory = Environment.getExternalStorageDirectory().toString(); 
		    OutputStream outStream = null; 
		    File file = new File(extStorageDirectory, "route.PNG"); 
		    try { 
		     outStream = new FileOutputStream(file); 
		     imageRes.compress(Bitmap.CompressFormat.PNG, 100, outStream); 
		     outStream.flush();
		     outStream.close();
		    }
		    catch(Exception e) 
		    {			e.printStackTrace();} 


			Intent ourIntent = null;
		//	try {
			//	routClass = Class.forName("com.zdm.picabus.GetRout"); 
				// ourIntent = new Intent(ClientTestActivity.this,routClass);
				 ourIntent = new Intent("com.zdm.picabus.GetRout");
			//	ourIntent.putExtra("BitmapImage", imageRes); 
				startActivity(ourIntent);
	/*		}		catch(ClassNotFoundException e){
				e.printStackTrace();
			}
	*/		


			

			//in the new intent
			//Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage"); 


		}
	});
		
		
		sendRequest.start();
		
		
		
		
		
		
		
		
		
		
	}


	public static Bitmap postGetRoute(String targetURL, String urlParameters) {

		URL url;
		// String lineEnd = "\r\n";
		String lineEnd = "\n";
		String twoHyphens = "--";
		String boundary = "*****";

		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			/* connection.setRequestProperty("Content-Type","picabus/getRoute"); */
			connection.setRequestProperty("Content-Type",
					"picabus/getRoute;boundary=" + boundary);

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			// connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			/*
			 * String str = urlParameters+lineEnd+twoHyphens + boundary +
			 * twoHyphens + lineEnd; wr.writeBytes(str);
			 */
			wr.writeBytes(urlParameters);
			// wr.writeBytes(lineEnd);
			/* wr.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd); */
			wr.flush();
			wr.close();
			// Get Response
			InputStream is = connection.getInputStream();
			
/*			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();*/
			Bitmap bmImg = BitmapFactory.decodeStream(is);
			int num = bmImg.getByteCount();
			//return response.toString();
			return bmImg;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}

