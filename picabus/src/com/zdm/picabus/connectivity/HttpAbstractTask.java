package com.zdm.picabus.connectivity;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public abstract class HttpAbstractTask extends AsyncTask<String, String, String> {
   
	private static final int SO_TIMEOUT = 3000;
	private static final int CONNECTION_TIMEOUT = 3000;
	
	private Context mContext;
	private ProgressDialog waitSpinner;
	private String taskName;
	private JSONObject requestPayload;
	
	public HttpAbstractTask(Context mContext, ProgressDialog waitSpinner, String taskName, JSONObject reuqestPayload) {
		this.mContext = mContext;
		this.waitSpinner = waitSpinner;
		this.taskName = taskName;
		this.requestPayload = reuqestPayload;
	}
	
	@Override
	protected String doInBackground(String... arg) { // note: this method is executed off the UI thread
	
		String retreturnVal = "";
		String serviceURL = arg[0];

		/*waitSpinner = ProgressDialog.show(mContext, "Loading",
				"Please wait...", true);*/

		// Create new default http client
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout Limit
		HttpConnectionParams.setSoTimeout(client.getParams(), 10000);
		HttpResponse response;
		HttpPost post = new HttpPost(serviceURL);
			
		try {
			StringEntity se = new StringEntity(requestPayload.toString());
			post.addHeader(CustomHeader.TASK_NAME.getHeaderName(), taskName);
			se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(se);

			// Execute the request
			response = client.execute(post);

			// Get the response status code
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) { // Ok
				if (response != null) { // Checking response
					InputStream in = response.getEntity().getContent(); // Get the data in the entity
					retreturnVal = HttpCaller.readContentFromIS(in);
				}
			}
		} catch (Exception e) {
			Log.e("Error in connectivity layer, stacktrace: ", e.toString());
			return null;
		}
		return retreturnVal; // This value will be returned to your onPostExecute(result) method
	}

    @Override
    protected void onProgressUpdate(String...values) {
    	// do nothing - we can't show dynamic progress
    	return;
    }
	
	
	@Override
    protected abstract void onPostExecute(String result); // note: this method is executed by the UI thread 
    
}

