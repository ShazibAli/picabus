package com.zdm.picabus.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.zdm.picabus.server.exceptions.EmptyResultException;

public class PicabusServerServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;
	private final static String TASK_NAME_HEADER = "Task-name";
	private final static int ERROR_CODE = 500;
	private final static String UNSUPPOTED_TASK_ERROR_MSG = "Supplied Task-name is not supported by Picabus server";
	private final static String UNSUPPOTED_PAYLOAD_TYPE_ERROR_MSG = "Supplied payload data type is not supported by Picabus server";
	private final static String UNSUPPOTED_JSON_PARAMS_ERROR_MSG = "Payload data does not match the API for this request";
	private final static String DB_CONNECTION_ISSUES_ERROR_MSG = "An error has occured while trying to connect the DB";
	private final static String MISSING_TASK_NAME_HEADER_ERROR_MSG = "Task-name header is missing. Please refer to Picabus Server API";
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String taskName = req.getHeader(TASK_NAME_HEADER);
		if (taskName == null) {
			resp.sendError(ERROR_CODE, MISSING_TASK_NAME_HEADER_ERROR_MSG);
			return;
		}
		
		// dispatching requests for handling according task name
		if (taskName.equalsIgnoreCase(Service.GET_DEPARTURE_TIMES.getTaskName())) {
			
				JsonObject jsonObject = extractRequestPayload(req);
				if (jsonObject == null) {
					resp.sendError(ERROR_CODE, UNSUPPOTED_PAYLOAD_TYPE_ERROR_MSG);
					return;
				}
				
			    JsonElement lineNumber =  jsonObject.getAsJsonObject().get("lineNumber");
			    JsonElement lat = jsonObject.getAsJsonObject().get("latitude");
			    JsonElement lng = jsonObject.getAsJsonObject().get("longitude");
			    JsonElement clientTimeString = jsonObject.getAsJsonObject().get("clientTime");
			    JsonElement timeIntervalString = jsonObject.getAsJsonObject().get("timeInterval"); 
			   
			    if (lineNumber==null || lat == null || lng == null || clientTimeString == null || timeIntervalString == null){
			    	resp.sendError(ERROR_CODE, UNSUPPOTED_JSON_PARAMS_ERROR_MSG);
					return;
			    }
			    
			    Integer lineNumberValue =  lineNumber.getAsInt();
			    Double latValue = lat.getAsDouble();
			    Double lngValue = lng.getAsDouble();
			    String clientTimeStringValue = clientTimeString.getAsString();
			    Integer timeIntervalStringValue = timeIntervalString.getAsInt(); 
			    
			    RequestHandler rh = new RequestHandler();
			    JsonObject responeData = null;
				try {
					responeData = rh.getDepartueTimePerLine(lineNumberValue, latValue, lngValue, clientTimeStringValue, timeIntervalStringValue);
					if (responeData == null) {
						resp.sendError(ERROR_CODE, DB_CONNECTION_ISSUES_ERROR_MSG);
						return;
					}
				} catch (EmptyResultException e) {
					e.printStackTrace();
					responeData = generateEmptyResultsJson();
				} 
			    
			    // send back the response
			    resp.setHeader("Content-Type", "application/json; charset=UTF-8");
			    PrintWriter out =  resp.getWriter();
				out.print(responeData.toString());
			
			//  Mock Data
				//String mockData = "{\"data\": {\"tripCount\": 1,\"stopHeadsign\": \"דרארליך/שבטיישראל\",\"bidirectional\": false,\"trip0\": {\"direction\": 1,\"id\": 646734120110512,\"destination\": \"מתחםגי/ילדיטהרן-ראשוןלציון<->ת.רכבתמרכז-תלאביביפו\",\"lineNumber\": 10,\"eta\": \"08: 28: 18\",\"companyName\": \"דן\",\"stopID\": 29335,\"stopSequence\": 34,\"serviceID\": 1619376,\"routeID\": 1026368}}}";
			    //out.print(mockData);
		}

		else if (taskName.equalsIgnoreCase(Service.GET_ROUTE_DETAILS.getTaskName())) {
			JsonObject jsonObject = extractRequestPayload(req);
			if (jsonObject == null) {
				resp.sendError(ERROR_CODE, UNSUPPOTED_PAYLOAD_TYPE_ERROR_MSG);
				return;
			}
			JsonElement currentStopSequenceNumber =  jsonObject.getAsJsonObject().get("currentStopSequenceNumber");
			JsonElement tripID = jsonObject.getAsJsonObject().get("tripID");
		    if (currentStopSequenceNumber == null || tripID == null) {
		    	resp.sendError(ERROR_CODE, UNSUPPOTED_JSON_PARAMS_ERROR_MSG);
				return;
		    }
		    
		    Integer currentStopSequenceNumberValue =  currentStopSequenceNumber.getAsInt();
		    Long tripIDValue = tripID.getAsLong();
		    
			RequestHandler rh = new RequestHandler();
			JsonObject responeData;
			try {
				responeData = rh.getRouteDetails(tripIDValue, currentStopSequenceNumberValue);
				if (responeData == null) {
					resp.sendError(ERROR_CODE, DB_CONNECTION_ISSUES_ERROR_MSG);
					return;
				}
			} catch (EmptyResultException e) {
				e.printStackTrace();
				responeData = generateEmptyResultsJson();
			} 
		
			 // send back the response
		    resp.setHeader("Content-Type", "application/json; charset=UTF-8");
		    PrintWriter out = resp.getWriter();
			out.print(responeData.toString());
		
		//  Mock Data
		//	String mockData = "{\"data\": {\"stop0\": {\"stopName\": \"ביהס לב יפו/שד ירושלים\",\"stopAddress\": \"כתובת:שדרות ירושלים 99 תל אביב יפו\",\"stopSequence\": 35,\"latitude\": 32.046738,\"longitude\": 34.758574,\"departureTime\": \"08:29:16\",\"stop_code\": 25241},\"stop1\": {\"stopName\": \"שד ירושלים/יהודה הימית\",\"stopAddress\": \"כתובת:שדרות ירושלים 67 תל אביב יפו\",\"stopSequence\": 36,\"latitude\": 32.048999,\"longitude\": 34.758889,\"departureTime\": \"08:29:51\",\"stop_code\": 25304},\"stopCount\": 2}}";
		//	out.print(mockData);
		}
		else { // case this is an unsupported task
			resp.sendError(ERROR_CODE, UNSUPPOTED_TASK_ERROR_MSG);
		}
	}
	
	private JsonObject generateEmptyResultsJson() {
		JsonObject empty = new JsonObject();
		empty.addProperty("data", "empty");
		return empty;
	}
	
	/**
	 * Extract the request content (assumed to be in JSON format)
	 * @param req the HttpServletRequest 
	 * @return request payload as JSON object 
	 */
	private JsonObject extractRequestPayload(HttpServletRequest req) {

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
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("Picabus server is up and running! Please refer to our services API");
	}


	/**
	 * Enum representing the available services (each contains it's own task name)
	 * 
	 * @author user
	 * 
	 */
	public enum Service {
		GET_DEPARTURE_TIMES("getDepartureTimes"), GET_ROUTE_DETAILS("getRouteDetails");

		private String taskName;

		private Service(String value) {
			this.taskName = value;
		}

		public String getTaskName() {
			return this.taskName;
		}
	};

}
