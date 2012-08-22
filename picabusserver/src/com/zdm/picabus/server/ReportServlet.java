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

public class ReportServlet extends HttpServlet {

	private final static String TASK_NAME_HEADER = "Task-name";
	private final static int ERROR_CODE = 500;
	private static final long serialVersionUID = 1L;

	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String taskName = req.getHeader(TASK_NAME_HEADER);
		if (taskName == null) {
			resp.sendError(ERROR_CODE, ServerError.MISSING_TASK_NAME_HEADER_ERROR_MSG.toString());
			return;
		}
		
		// dispatching requests for handling according task name
		if (taskName.equalsIgnoreCase(com.zdm.picabus.utils.Service.REPORT_LOCATION.getTaskName())) {

			JsonObject jsonObject = RequestUtils.extractRequestPayload(req);
			if (jsonObject == null) {
				resp.sendError(ERROR_CODE,
						ServerError.UNSUPPOTED_PAYLOAD_TYPE_ERROR_MSG
								.toString());
				return;
			}

			JsonElement userId = jsonObject.getAsJsonObject().get("userId");
			JsonElement lat = jsonObject.getAsJsonObject().get("latitude");
			JsonElement lng = jsonObject.getAsJsonObject().get("longitude");
			JsonElement tripId = jsonObject.getAsJsonObject().get("tripId");

			if (userId == null || lat == null || lng == null || tripId == null) {
				resp.sendError(ERROR_CODE,
						ServerError.UNSUPPOTED_JSON_PARAMS_ERROR_MSG.toString());
				return;
			}

			Long userIdValue = userId.getAsLong();
			Double latValue = lat.getAsDouble();
			Double lngValue = lng.getAsDouble();
			Long tripIdValue = tripId.getAsLong();

			RequestHandler rh = new RequestHandler();
			boolean result = false;
			JsonObject responseJson = null;
			result = rh.reportLocation(userIdValue, latValue, lngValue,
					tripIdValue);
			if (result == false) {
				resp.sendError(ERROR_CODE,
						ServerError.DB_CONNECTION_ISSUES_ERROR_MSG.toString());
				return;
			} else { // result == true
				responseJson = generateStatusResultsJson(result);
			}

			// send back the response
			resp.setContentType("application/json; charset=UTF-8");
			PrintWriter out = resp.getWriter();
			out.print(responseJson.toString());

			// Mock Data
			// String mockData =
			// "{\"data\": {\"tripCount\": 1,\"stopHeadsign\": \"דרארליך/שבטיישראל\",\"bidirectional\": false,\"trip0\": {\"direction\": 1,\"id\": 646734120110512,\"destination\": \"מתחםגי/ילדיטהרן-ראשוןלציון<->ת.רכבתמרכז-תלאביביפו\",\"lineNumber\": 10,\"eta\": \"08: 28: 18\",\"companyName\": \"דן\",\"stopID\": 29335,\"stopSequence\": 34,\"serviceID\": 1619376,\"routeID\": 1026368}}}";
			// out.print(mockData);

		}
		
		else if (taskName.equalsIgnoreCase(com.zdm.picabus.utils.Service.REPORT_CHECKOUT.getTaskName())) {
			JsonObject jsonObject = RequestUtils.extractRequestPayload(req);
			if (jsonObject == null) {
				resp.sendError(ERROR_CODE,
						ServerError.UNSUPPOTED_PAYLOAD_TYPE_ERROR_MSG
								.toString());
				return;
			}

			JsonElement userId = jsonObject.getAsJsonObject().get("userId");
			JsonElement tripId = jsonObject.getAsJsonObject().get("tripId");


			if (userId == null || tripId == null) {
				resp.sendError(ERROR_CODE,
						ServerError.UNSUPPOTED_JSON_PARAMS_ERROR_MSG.toString());
				return;
			}

			Long userIdValue = userId.getAsLong();
			Long tripIdValue = tripId.getAsLong();

			RequestHandler rh = new RequestHandler();
			boolean result = false;
			JsonObject responseJson = null;
			result = rh.reportCheckout(userIdValue,	tripIdValue);
			if (result == false) {
				resp.sendError(ERROR_CODE,
						ServerError.DB_CONNECTION_ISSUES_ERROR_MSG.toString());
				return;
			} else { // result == true
				responseJson = generateStatusResultsJson(result);
			}

			// send back the response
			resp.setContentType("application/json; charset=UTF-8");
			PrintWriter out = resp.getWriter();
			out.print(responseJson.toString());

			// Mock Data
			// String mockData =
			// "{\"data\": {\"tripCount\": 1,\"stopHeadsign\": \"דרארליך/שבטיישראל\",\"bidirectional\": false,\"trip0\": {\"direction\": 1,\"id\": 646734120110512,\"destination\": \"מתחםגי/ילדיטהרן-ראשוןלציון<->ת.רכבתמרכז-תלאביביפו\",\"lineNumber\": 10,\"eta\": \"08: 28: 18\",\"companyName\": \"דן\",\"stopID\": 29335,\"stopSequence\": 34,\"serviceID\": 1619376,\"routeID\": 1026368}}}";
			// out.print(mockData);

		}
		
		else if (taskName.equalsIgnoreCase(com.zdm.picabus.utils.Service.REPORT_TEXTUAL_MSG.getTaskName())) {
			JsonObject jsonObject = RequestUtils.extractRequestPayload(req);
			if (jsonObject == null) {
				resp.sendError(ERROR_CODE,
						ServerError.UNSUPPOTED_PAYLOAD_TYPE_ERROR_MSG
								.toString());
				return;
			}

			JsonElement userId = jsonObject.getAsJsonObject().get("userId");
			JsonElement tripId = jsonObject.getAsJsonObject().get("tripId");
			JsonElement reportMessage = jsonObject.getAsJsonObject().get("reportMessage");


			if (userId == null || tripId == null || reportMessage == null) {
				resp.sendError(ERROR_CODE,
						ServerError.UNSUPPOTED_JSON_PARAMS_ERROR_MSG.toString());
				return;
			}

			Long userIdValue = userId.getAsLong();
			Long tripIdValue = tripId.getAsLong();
			String reportMessageValue = reportMessage.getAsString();
			
			RequestHandler rh = new RequestHandler();
			boolean result = false;
			JsonObject responseJson = null;
			result = rh.reportTextualMessage(userIdValue, tripIdValue, reportMessageValue);
			if (result == false) {
				resp.sendError(ERROR_CODE,
						ServerError.DB_CONNECTION_ISSUES_ERROR_MSG.toString());
				return;
			} else { // result == true
				responseJson = generateStatusResultsJson(result);
			}

			// send back the response
			resp.setContentType("application/json; charset=UTF-8");
			PrintWriter out = resp.getWriter();
			out.print(responseJson.toString());

			// Mock Data
			// String mockData =
			// "{\"data\": {\"tripCount\": 1,\"stopHeadsign\": \"דרארליך/שבטיישראל\",\"bidirectional\": false,\"trip0\": {\"direction\": 1,\"id\": 646734120110512,\"destination\": \"מתחםגי/ילדיטהרן-ראשוןלציון<->ת.רכבתמרכז-תלאביביפו\",\"lineNumber\": 10,\"eta\": \"08: 28: 18\",\"companyName\": \"דן\",\"stopID\": 29335,\"stopSequence\": 34,\"serviceID\": 1619376,\"routeID\": 1026368}}}";
			// out.print(mockData);

		}
		
		else if (taskName.equalsIgnoreCase(com.zdm.picabus.utils.Service.GET_LAST_REPORTED_LOCATION.getTaskName())) {
			JsonObject jsonObject = RequestUtils.extractRequestPayload(req);
			if (jsonObject == null) {
				resp.sendError(ERROR_CODE,
						ServerError.UNSUPPOTED_PAYLOAD_TYPE_ERROR_MSG
								.toString());
				return;
			}
			JsonElement tripId = jsonObject.getAsJsonObject().get("tripId");

			if (tripId == null ) {
				resp.sendError(ERROR_CODE,
						ServerError.UNSUPPOTED_JSON_PARAMS_ERROR_MSG.toString());
				return;
			}

			Long tripIdValue = tripId.getAsLong();

			RequestHandler rh = new RequestHandler();
			JsonObject responseData;
			
			try {
				responseData = rh.getRealtimeLocation(tripIdValue);
				if (responseData == null) {
					resp.sendError(ERROR_CODE, ServerError.DB_CONNECTION_ISSUES_ERROR_MSG.toString());
					return;
				}
				
			} catch (EmptyResultException e) {
				responseData = RequestUtils.generateEmptyResultsJson();
			} 			

			// send back the response
			resp.setContentType("application/json; charset=UTF-8");
			PrintWriter out = resp.getWriter();
			out.print(responseData.toString());
		}
		
		else if (taskName.equalsIgnoreCase(com.zdm.picabus.utils.Service.GET_USER_SCORE.getTaskName())) {
			JsonObject jsonObject = RequestUtils.extractRequestPayload(req);
			if (jsonObject == null) {
				resp.sendError(ERROR_CODE,
						ServerError.UNSUPPOTED_PAYLOAD_TYPE_ERROR_MSG
								.toString());
				return;
			}
			JsonElement userId = jsonObject.getAsJsonObject().get("userId");

			if (userId == null ) {
				resp.sendError(ERROR_CODE,
						ServerError.UNSUPPOTED_JSON_PARAMS_ERROR_MSG.toString());
				return;
			}

			Long userIdValue = userId.getAsLong();

			RequestHandler rh = new RequestHandler();
			JsonObject responseData;
			
			responseData = rh.getUserScore(userIdValue);
			if (responseData == null) {
				resp.sendError(ERROR_CODE, ServerError.DB_CONNECTION_ISSUES_ERROR_MSG.toString());
				return;
			} 			

			// send back the response
			resp.setContentType("application/json; charset=UTF-8");
			PrintWriter out = resp.getWriter();
			out.print(responseData.toString());
		}
		else { // case this is an unsupported task
			resp.sendError(ERROR_CODE, ServerError.UNSUPPOTED_TASK_ERROR_MSG.toString());
		}
		
		
		
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("Picabus server is up and running! Please refer to our services API");
	}
	
	private JsonObject generateStatusResultsJson(boolean succes) {	
		JsonObject result = new JsonObject();
		if (succes){
			result.addProperty("data", "success");	
		}
		else {
			result.addProperty("data", "failure");
		}
		return result;
	}
}
