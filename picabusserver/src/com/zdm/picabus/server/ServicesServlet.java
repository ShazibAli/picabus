package com.zdm.picabus.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zdm.picabus.server.exceptions.EmptyResultException;
import com.zdm.picabus.utils.RequestUtils;
import com.zdm.picabus.utils.ServerError;
import com.zdm.picabus.utils.Service;

public class ServicesServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;
	private final static String TASK_NAME_HEADER = "Task-name";
	private final static int ERROR_CODE = 500;

	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String taskName = req.getHeader(TASK_NAME_HEADER);
		if (taskName == null) {
			resp.sendError(ERROR_CODE, ServerError.MISSING_TASK_NAME_HEADER_ERROR_MSG.toString());
			return;
		}
		
		// dispatching requests for handling according task name
		if (taskName.equalsIgnoreCase(Service.GET_DEPARTURE_TIMES.getTaskName())) {
			
				JsonObject jsonObject = RequestUtils.extractRequestPayload(req);
				if (jsonObject == null) {
					resp.sendError(ERROR_CODE, ServerError.UNSUPPOTED_PAYLOAD_TYPE_ERROR_MSG.toString());
					return;
				}
				
			    JsonElement lineNumber =  jsonObject.getAsJsonObject().get("lineNumber");
			    JsonElement lat = jsonObject.getAsJsonObject().get("latitude");
			    JsonElement lng = jsonObject.getAsJsonObject().get("longitude");
			    JsonElement clientTimeString = jsonObject.getAsJsonObject().get("clientTime");
			    JsonElement timeIntervalString = jsonObject.getAsJsonObject().get("timeInterval"); 
			   
			    if (lineNumber==null || lat == null || lng == null || clientTimeString == null || timeIntervalString == null){
			    	resp.sendError(ERROR_CODE, ServerError.UNSUPPOTED_JSON_PARAMS_ERROR_MSG.toString());
					return;
			    }
			    
			    Integer lineNumberValue =  lineNumber.getAsInt();
			    Double latValue = lat.getAsDouble();
			    Double lngValue = lng.getAsDouble();
			    String clientTimeStringValue = clientTimeString.getAsString();
			    Integer timeIntervalStringValue = timeIntervalString.getAsInt(); 
			    
			    RequestHandler rh = new RequestHandler();
			    JsonObject responseData = null;
				try {
					responseData = rh.getDepartueTimePerLine(lineNumberValue, latValue, lngValue, clientTimeStringValue, timeIntervalStringValue);
					if (responseData == null) {
						resp.sendError(ERROR_CODE, ServerError.DB_CONNECTION_ISSUES_ERROR_MSG.toString());
						return;
					}
				} catch (EmptyResultException e) {
					e.printStackTrace();
					responseData = generateEmptyResultsJson();
				} 
			    
				RequestUtils.sendBackResponse(resp, responseData);
			
			//  Mock Data
				//String mockData = "{\"data\": {\"tripCount\": 1,\"stopHeadsign\": \"דרארליך/שבטיישראל\",\"bidirectional\": false,\"trip0\": {\"direction\": 1,\"id\": 646734120110512,\"destination\": \"מתחםגי/ילדיטהרן-ראשוןלציון<->ת.רכבתמרכז-תלאביביפו\",\"lineNumber\": 10,\"eta\": \"08: 28: 18\",\"companyName\": \"דן\",\"stopID\": 29335,\"stopSequence\": 34,\"serviceID\": 1619376,\"routeID\": 1026368}}}";
			    //out.print(mockData);
		}

		else if (taskName.equalsIgnoreCase(Service.GET_ROUTE_DETAILS.getTaskName())) {
			JsonObject jsonObject = RequestUtils.extractRequestPayload(req);
			if (jsonObject == null) {
				resp.sendError(ERROR_CODE, ServerError.UNSUPPOTED_PAYLOAD_TYPE_ERROR_MSG.toString());
				return;
			}
			JsonElement currentStopSequenceNumber =  jsonObject.getAsJsonObject().get("currentStopSequenceNumber");
			JsonElement tripID = jsonObject.getAsJsonObject().get("tripID");
		    if (currentStopSequenceNumber == null || tripID == null) {
		    	resp.sendError(ERROR_CODE, ServerError.UNSUPPOTED_JSON_PARAMS_ERROR_MSG.toString());
				return;
		    }
		    
		    Integer currentStopSequenceNumberValue =  currentStopSequenceNumber.getAsInt();
		    Long tripIDValue = tripID.getAsLong();
		    
			RequestHandler rh = new RequestHandler();
			JsonObject responseData;
			try {
				responseData = rh.getRouteDetails(tripIDValue, currentStopSequenceNumberValue);
				if (responseData == null) {
					resp.sendError(ERROR_CODE, ServerError.DB_CONNECTION_ISSUES_ERROR_MSG.toString());
					return;
				}
			} catch (EmptyResultException e) {
				e.printStackTrace();
				responseData = generateEmptyResultsJson();
			} 
		
			RequestUtils.sendBackResponse(resp, responseData);
		
		//  Mock Data
		//	String mockData = "{\"data\": {\"stop0\": {\"stopName\": \"ביהס לב יפו/שד ירושלים\",\"stopAddress\": \"כתובת:שדרות ירושלים 99 תל אביב יפו\",\"stopSequence\": 35,\"latitude\": 32.046738,\"longitude\": 34.758574,\"departureTime\": \"08:29:16\",\"stop_code\": 25241},\"stop1\": {\"stopName\": \"שד ירושלים/יהודה הימית\",\"stopAddress\": \"כתובת:שדרות ירושלים 67 תל אביב יפו\",\"stopSequence\": 36,\"latitude\": 32.048999,\"longitude\": 34.758889,\"departureTime\": \"08:29:51\",\"stop_code\": 25304},\"stopCount\": 2}}";
		//	out.print(mockData);
		}
		else { // case this is an unsupported task
			resp.sendError(ERROR_CODE, ServerError.UNSUPPOTED_TASK_ERROR_MSG.toString());
		}
	}
	
	private JsonObject generateEmptyResultsJson() {
		JsonObject empty = new JsonObject();
		empty.addProperty("data", "empty");
		return empty;
	}
	

	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("Picabus server is up and running! Please refer to our services API");
	}
}
