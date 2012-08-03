package com.zdm.picabus.tempmoran;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TempClass extends Activity {

	Button btnSendRequest;
	TextView txtResultText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.client_test);

		Toast.makeText(getBaseContext(), "Test", 3000);
		btnSendRequest = (Button) findViewById(R.id.sendRequest);
		txtResultText = (TextView) findViewById(R.id.resultContainer);

		btnSendRequest.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Thread sendRequest = new Thread(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub

						// sendGetRequest("http://10.0.2.2:5000", null);

						/************* encode android image (byte[]) to string ****************/
						/*
						 * String data = Base64.encodeToString(myArrayImage,
						 * Base64.DEFAULT);
						 */

						/***************
						 * send from path in android device-to server (insert in
						 * post execute)
						 *************/

						/*
						 * File("/sdcard/download/600px_US_5.svg");
						 */
						String pathToFile = "/sdcard/download/600px_US_5.svg";


						/*
						 * after photo taken by device- prepare a string (from
						 * the path)
						 */
						/*-----> in the camera part:
						 * File file = new File(Environment.getExternalStorageDirectory(),"sign.jpg");
						 outputFileUri = Uri.fromFile(file);
						 cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
						 startActivityForResult(cameraIntent, PICTURE_ACTIVITY);

						/*-------> here:
						 */
/*						File file = new File(Environment
								.getExternalStorageDirectory(), "sign.jpg");
						if (file.exists()) {
							ByteArrayOutputStream stream = new ByteArrayOutputStream();
							// Enclose this in a scope so that when it’s over we
							// can call the garbage collector (the phone doesn’t
							// have a lot of memory!)
							{
								Bitmap image = BitmapFactory.decodeFile(file
										.getPath());
								Bitmap scaled = Bitmap.createScaledBitmap(
										image, (int) (image.getWidth() * 0.3),
										(int) (image.getHeight() * 0.3), false);
								scaled.compress(Bitmap.CompressFormat.JPEG, 90,
										stream);
							}

							System.gc();

							byte[] byte_arr = stream.toByteArray();
							// String image_str = Base64.encodeBytes(byte_arr);
							String image_str = Base64.encodeToString(
									imageTakenAndroid, Base64.DEFAULT);
						}*/

						/******** take downloadedphoto from android library **********/
						
						/*File anroid_photo2 = new File(
								"/sdcard/download/600px-US_5.svg.png");
						if (anroid_photo2.exists())
						System.out.println("kaf;lskjf");	*/


						/* route request */
						int line = 68;
						String lineStr = "Line: \n"+Integer.toString(line)+"\n";
						String busCompany = "Company: \nDan\n";
						String routeRequest = lineStr + busCompany;

						
						
						/*line time request*/
						
						/*1. get current time*/
						Calendar calendar = new GregorianCalendar();
						int  hour = calendar.get(Calendar.HOUR_OF_DAY);
						int minute = calendar.get(Calendar.MINUTE);
						if (calendar.get(Calendar.AM_PM) != 0)
							hour+=12;
						String time = Integer.toString(hour) + "\n" + Integer.toString(minute) + "\n";
						
						/*2. image*/
						
						
						/* image as bytes - to string */
						//	byte[] imageTakenAndroid = { 123 };// should be a photo
						/*String imageAsStr = Base64.encodeToString(imageTakenAndroid, Base64.DEFAULT);
						signImage += imageAsStr + "\n";*/

						
						String imageTakenAndroid = "blaasasasasa";
						String dataForTime = "Sign Image:\n"+ imageTakenAndroid+"\n";
						
						 String result = postSendSign("http://10.0.2.2:5001",dataForTime);
						 
					}
					

						// String result2 = postGetRoute("http://10.0.2.2:5009",
						// routeRequest);
						// txtResultText.setText("result");
					}

				);

				sendRequest.start();
			}

		});
	}

	
	private static String readFileAsString(String filePath) throws java.io.IOException{
	    byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	    } finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    return new String(buffer);
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

	public static String postGetRoute(String targetURL, String urlParameters) {

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


	public void postData() {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("id", "12345"));
			nameValuePairs.add(new BasicNameValuePair("stringdata",
					"AndDev is Cool!"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	// see
	// http://androidsnippets.com/executing-a-http-post-request-with-httpclient

	private String sendGetRequest(String endpoint, String requestParameters) {
		String result = null;
		if (endpoint.startsWith("http://")) {
			// Send a GET request to the servlet
			try {
				// Construct data
				StringBuffer data = new StringBuffer();

				// Send data
				String urlStr = endpoint;
				if (requestParameters != null && requestParameters.length() > 0) {
					urlStr += "?" + requestParameters;
				}
				URL url = new URL(urlStr);
				URLConnection conn = url.openConnection();

				// conn.connect();
				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String line;
				while ((line = rd.readLine()) != null) {
					sb.append(line);
				}
				rd.close();
				result = sb.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Reads data from the data reader and posts it to a server via POST
	 * request. data - The data you want to send endpoint - The server's address
	 * output - writes the server's response to output
	 * 
	 * @throws Exception
	 */
	public static void postData(Reader data, URL endpoint, Writer output)
			throws Exception {
		HttpURLConnection urlc = null;
		try {
			urlc = (HttpURLConnection) endpoint.openConnection();
			// prepare data?
			try {
				urlc.setRequestMethod("POST");
			} catch (ProtocolException e) {
				throw new Exception(
						"Shouldn't happen: HttpURLConnection doesn't support POST??",
						e);
			}
			urlc.setDoOutput(true);
			urlc.setDoInput(true);
			urlc.setUseCaches(false);
			urlc.setAllowUserInteraction(false);
			urlc.setRequestProperty("Content-type", "text/xml; charset="
					+ "UTF-8");

			// request?
			OutputStream out = urlc.getOutputStream();

			try {
				Writer writer = new OutputStreamWriter(out, "UTF-8");
				pipe(data, writer);
				writer.close();
			} catch (IOException e) {
				throw new Exception("IOException while posting data", e);
			} finally {
				if (out != null)
					out.close();
			}

			// response?
			InputStream in = urlc.getInputStream();
			try {
				Reader reader = new InputStreamReader(in);
				pipe(reader, output);
				reader.close();
			} catch (IOException e) {
				throw new Exception("IOException while reading response", e);
			} finally {
				if (in != null)
					in.close();
			}

		} catch (IOException e) {
			throw new Exception("Connection error (is server running at "
					+ endpoint + " ?): " + e);
		} finally {
			if (urlc != null)
				urlc.disconnect();
		}
	}

	/**
	 * Pipes everything from the reader to the writer via a buffer
	 */
	private static void pipe(Reader reader, Writer writer) throws IOException {
		char[] buf = new char[1024];
		int read = 0;
		while ((read = reader.read(buf)) >= 0) {
			writer.write(buf, 0, read);
		}
		writer.flush();
	}

	/*
	 * // Create query string
	 * 
	 * String queryString = "param1=" + URLEncoder.encode(param1Value, "UTF-8");
	 * queryString += "&param2=" + URLEncoder.encode(param2Value, "UTF-8");
	 * 
	 * // Make connection
	 * 
	 * URL url = new URL("http://www.objects.com.au/"); URLConnection
	 * urlConnection = url.openConnection(); urlConnection.setDoOutput(true);
	 * OutputStreamWriter out = new OutputStreamWriter(
	 * urlConnection.getOutputStream());
	 * 
	 * // Write query string to request body
	 * 
	 * out.write(queryString); out.flush();
	 * 
	 * // Read the response
	 * 
	 * BufferedReader in = new BufferedReader( new
	 * InputStreamReader(urlConnection.getInputStream())); String line = null;
	 * while ((line = in.readLine()) != null) { System.out.println(line); }
	 * out.close(); in.close();
	 */

	/*
	 * String urlParameters = "fName=" + URLEncoder.encode("???", "UTF-8") +
	 * "&lName=" + URLEncoder.encode("???", "UTF-8")
	 */

	/*
	 * 
	 * 
	 * dos.writeBytes(twoHyphens + boundary + lineEnd); dos.writeBytes(
	 * "Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" +
	 * exsistingFileName +"\"" + lineEnd); dos.writeBytes(lineEnd);
	 */

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