package com.zdm.connectivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;

public class RequestHandler {

	public void sendData(String time, Bitmap image, double LATITUDE, double LONGITUDE){
	
	HttpClient httpclient = new DefaultHttpClient();
	String address = "http://picabusapp.appspot.com/picabusserver?mirror=true";
    HttpPost httppost = new HttpPost(address);

    try {
    	//build xml
    	StringBuilder sb = new StringBuilder();
    	sb.append("<ArrayOfString>");
    	sb.append("<string>").append("the_command").append("</string>");
    	sb.append("</ArrayOfString>");
    	
    	//set xml 
    	StringEntity entity = new StringEntity(sb.toString(), "UTF-8");
        entity.setContentType("text/xml");
        httppost.setEntity(entity);
        //set http headers
        httppost.addHeader("Accept", "application/xml");
        httppost.addHeader("Task-name", "getDepartureTimes");
        
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
	
}
