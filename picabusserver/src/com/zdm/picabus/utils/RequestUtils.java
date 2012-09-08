package com.zdm.picabus.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class RequestUtils {
	/**
	 * Extract the request content (assumed to be in JSON format)
	 * @param req the HttpServletRequest 
	 * @return request payload as JSON object 
	 */
	public static JsonObject extractRequestPayload(HttpServletRequest req) {

		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			req.setCharacterEncoding("UTF-8");
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				sb.append(line);
		} catch (Exception e) { 
			return null;
		}
		
		String jsonAsString = sb.toString();
		JsonParser parser = new JsonParser();
		JsonObject requestPaylod = null;
		try {
			requestPaylod = (JsonObject) parser.parse(jsonAsString);
		} catch (JsonSyntaxException jse) {
			return null;
		}
		return requestPaylod;
	}
	
	public static JsonObject generateEmptyResultsJson() {
		JsonObject empty = new JsonObject();
		empty.addProperty("data", "empty");
		return empty;
	}
	
	public static void sendBackResponse(HttpServletResponse resp,
			JsonObject responseData) throws IOException {
		// send back the response
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.print(responseData.toString());
	}
	
	
	public static JsonObject generateStatusResultsJson(boolean succes) {	
		JsonObject result = new JsonObject();
		if (succes){
			result.addProperty("data", "success");	
		}
		else {
			result.addProperty("data", "failure");
		}
		return result;
	}
	
	public static JsonObject generateStatusResultsJson(boolean status, String reportType, long currentNumOfPoints) {	
		JsonObject result = new JsonObject();
		if (status){
			result.addProperty("status", "success");
			result.addProperty("Task-name", reportType);
			result.addProperty("currentPoints", currentNumOfPoints);
			
		}
		else {
			result.addProperty("status", "failure");
		}
		return result;
	}
}
