package com.zdm.logic;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.zdm.picabus.R;
import com.zdm.picabus.R.id;
import com.zdm.picabus.R.layout;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ClientTestActivity extends Activity {

	Button btnSendRequest,btnGetRoute;
	TextView txtResultText;
	ImageView imView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.client_test);

		Toast.makeText(getBaseContext(), "Test", 3000);
		btnSendRequest = (Button) findViewById(R.id.sendRequest);
		btnGetRoute = (Button) findViewById(R.id.bGetRoute);
		txtResultText = (TextView) findViewById(R.id.resultContainer);
		imView = (ImageView) findViewById(R.id.imview);
		
		btnSendRequest.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Thread sendRequest = new Thread(new Runnable() {

					public void run() {
						
						/*File anroid_photo2 = new File("/sdcard/download/600px-US_5.svg.png");*/

						/*1. get current time*/
						Calendar calendar = new GregorianCalendar();
						int  hour = calendar.get(Calendar.HOUR_OF_DAY);
						int minute = calendar.get(Calendar.MINUTE);
						String time = "Time:\nHour:"+ Integer.toString(hour) + "Minute" + Integer.toString(minute) + "\n";
						
						/*2. image*/
									
						/* image as bytes - to string */
						//	byte[] imageTakenAndroid = { 123 };// should be a photo
						/*String imageAsStr = Base64.encodeToString(imageTakenAndroid, Base64.DEFAULT);
						signImage += imageAsStr + "\n";*/

						
						
						
						
						
/*						Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),R.drawable.splash);
						ByteArrayOutputStream bao = new ByteArrayOutputStream();
						bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
						byte [] ba = bao.toByteArray();
						String imageTakenAndroid=Base64.encodeToString(ba, Base64.DEFAULT);
*/

								
						String imageTakenAndroid = "blaasasasasa";
						String dataForTime = time + "Sign Image:\n"+ imageTakenAndroid+"\n";
						
						String result = postSendSign("http://10.0.2.2:5001",dataForTime);

						
						Intent ourIntent = null;
						//	try {
							//	routClass = Class.forName("com.zdm.picabus.GetRout"); 
								// ourIntent = new Intent(ClientTestActivity.this,routClass);
								 ourIntent = new Intent("com.zdm.picabus.ScheduleResults");
//								ourIntent.putExtra("BitmapImage", imageRes);
							//	 ourIntent.putExtra("ResultStr", result);
								startActivity(ourIntent);
						// txtResultText.setText("result");						 
					}
					

					}

				);

				sendRequest.start();
			}

		});
		
		
		btnGetRoute.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Thread sendRequest = new Thread(new Runnable() {
					
				public void run() {
					
					int line = 68;
					String lineStr = "Details:\nLine:"+Integer.toString(line);
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
		//				ourIntent.putExtra("BitmapImage", imageRes); 
						startActivity(ourIntent);
			/*		}		catch(ClassNotFoundException e){
						e.printStackTrace();
					}
			*/		


					

					//in the new intent
					//Bitmap bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage"); 


				}
			}
		);
				sendRequest.start();
	}
		});
	}
	
	
	public static String postSendSign2(String targetURL, String urlParameters) {

		URL url;
		String pathToOurFile = "/data/image.jpg";

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		HttpURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			/*
			 * connection.setRequestProperty("Content-Type","picabus/signpicture"
			 * );
			 */
			connection.setRequestProperty("Content-Type",
					"picabus/signpicture;boundary=" + boundary);
			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// file photo
			int maxBufferSize = 2056;
			String pathToFile = "/sdcard/download/600px-US_5.svg.png";
			File checkExist = new File(pathToFile);
			if (checkExist.exists())
				System.out.println("lala");
			
			FileInputStream fileInputStream = new FileInputStream(new File(pathToFile));
			int bytesAvailable = fileInputStream.available();
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];

			// open out
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());

			wr.writeBytes(urlParameters);
			// Read file + send
			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				wr.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			wr.writeBytes(lineEnd);
			wr.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			wr.flush();
			wr.close();
			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
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

	public static String postSendSign(String targetURL, String urlParameters) {

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
			/*
			 * connection.setRequestProperty("Content-Type","picabus/signpicture"
			 * );
			 */
			connection.setRequestProperty("Content-Type",
					"picabus/signpicture;boundary=" + boundary);

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
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
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



	public static void excutePost222copiedwasntinfunc(String data) {

		// Make connection
		URL url = null;
		try {
			url = new URL("http://10.0.2.2:5000");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URLConnection urlConnection = null;
		try {
			urlConnection = url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		urlConnection.setDoOutput(true);

		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(urlConnection.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Write query string to request body

		try {
			out.write(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Read the response

		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		try {
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}