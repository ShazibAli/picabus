package com.zdm.picabus.utils;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;

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
}
