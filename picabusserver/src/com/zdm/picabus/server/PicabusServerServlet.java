package com.zdm.picabus.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PicabusServerServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;
	private final static String TASK_NAME_HEADER = "Task-name";
	private final static int ERROR_CODE = 500;
	private final static String UNSUPPOTED_TASK_ERROR_MSG = "Supplied Task-name is not supported by Picabus server";
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String taskName = req.getHeader(TASK_NAME_HEADER);
		if (taskName == null) {
			resp.sendError(ERROR_CODE, UNSUPPOTED_TASK_ERROR_MSG);
		}
		
		// dispatching requests for handling according task name
		if (taskName.equalsIgnoreCase(Service.GET_DEPARTURE_TIMES.getTaskName())) {
			
				JsonObject jsonObject = extractRequestPayload(req);
			    int lineNumber =  jsonObject.getAsJsonObject().get("lineNumber").getAsInt();
			    double lat = jsonObject.getAsJsonObject().get("latitude").getAsDouble();
			    double lng = jsonObject.getAsJsonObject().get("longitude").getAsDouble();
			    String clientTimeString = jsonObject.getAsJsonObject().get("clientTime").getAsString();
			    String timeIntervalString = jsonObject.getAsJsonObject().get("timeInterval").getAsString(); //TODO: add time interval
			   
			    RequestHandler rh = new RequestHandler();
			    JsonObject responeData = rh.getDepartueTimePerLine(lineNumber, lat, lng, clientTimeString); 
			    
			    // send back the response
			    resp.setHeader("Content-Type", "application/json; charset=UTF-8");
			    PrintWriter out = resp.getWriter();
				out.print(responeData.toString());
			
			//  Mock Data
			//  out.print("{\"data\": {\"tripCount\": 1,\"stopHeadsign\": \"דרארליך/שבטיישראל\",\"bidirectional\": false,\"trip0\": {\"direction\": 1,\"id\": 646734120110512,\"destination\": \"מתחםגי/ילדיטהרן-ראשוןלציון<->ת.רכבתמרכז-תלאביביפו\",\"lineNumber\": 10,\"eta\": \"08: 28: 18\",\"companyName\": \"דן\",\"stopID\": 29335,\"stopSequence\": 34,\"serviceID\": 1619376,\"routeID\": 1026368}}}");
		}

		else if (taskName.equalsIgnoreCase(Service.GET_ROUTE_DETAILS.getTaskName())) {
			JsonObject jsonObject = extractRequestPayload(req);
		    int currentStopSequenceNumber =  jsonObject.getAsJsonObject().get("currentStopSequenceNumber").getAsInt();
		    long tripID = jsonObject.getAsJsonObject().get("tripID").getAsLong();
		    
			RequestHandler rh = new RequestHandler();
			JsonObject responeData = rh.getRouteDetails(tripID, currentStopSequenceNumber); 
		
			 // send back the response
		    resp.setHeader("Content-Type", "application/json; charset=UTF-8");
		    PrintWriter out = resp.getWriter();
			out.print(responeData.toString());
		}

		else { // case this is an unsupported task
			resp.sendError(ERROR_CODE, UNSUPPOTED_TASK_ERROR_MSG);
		}
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
		} catch (Exception e) { /* report an error */
		}

		String jsonAsString = sb.toString();
		JsonParser parser = new JsonParser();
		return (JsonObject) parser.parse(jsonAsString);
	}
	
	public void doPost2(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {



		/*
		 * Map<String, String> headers = getHeadersAndCookies();
		 * resp.setContentType("text/plain");
		 * 
		 * for (Entry<String, String> entry : headers.entrySet()) {
		 * resp.getWriter().println(entry.getKey() + " : " + entry.getValue());
		 * }
		 * 
		 * String directions = getDirections(FREE_TEXT, headers);
		 */



		PrintWriter out = resp.getWriter();
		/*
		 * Connection c = null; try { DriverManager.registerDriver(new
		 * AppEngineDriver()); c = (Connection) DriverManager
		 * .getConnection("jdbc:google:rdbms://test/sakila");
		 * 
		 * String statement = "SELECT * FROM actor LIMIT 100"; PreparedStatement
		 * stmt = c.prepareStatement(statement); ResultSet rs =
		 * stmt.executeQuery(); while (rs.next()) { int guestName =
		 * rs.getInt("actor_id"); String content = rs.getString("first_name");
		 * String id = rs.getString("last_name"); } } catch (SQLException e) {
		 * e.printStackTrace(); } finally { if (c != null) try { c.close(); }
		 * catch (SQLException ignore) { } }
		 */
		/*
		 * JSONObject myjson = null; try { myjson = new
		 * JSONObject("{\"a\": 5,\"b\": 6}"); } catch (JSONException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */

		String reqUrl = req.getRequestURL().toString();
		String queryString = req.getQueryString(); // d=789

		resp.setContentType("text/html");

		String httpHeadersString = "Some Headers";
		String requestPayloadString = "Some ayload";

		out.println("<BODY BGCOLOR=\"#FDF5E6\">\n"
				+ "<H1 > Request Details</H1>\n" + "<B>Request Method: </B>"
				+ req.getMethod() + "<BR>\n" + "<B>Request URI: </B>"
				+ req.getRequestURI() + "<BR>\n" + "<B>Request Protocol: </B>"
				+ req.getProtocol() + "<BR><BR>\n" + "<TABLE BORDER=1>\n"
				+ "<TR BGCOLOR=\"#FFAD00\">\n"
				+ "<TH>Header Name<TH>Header Value");
		Enumeration<String> headerNames = req.getHeaderNames();

		while (headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			out.println("<TR><TD>" + headerName);
			out.println("    <TD>" + req.getHeader(headerName));
		}

		out.println("</TABLE>\n");
		out.println("<H1 >Requset Payload: </H1>\n");
		BufferedReader buff = req.getReader();
		char[] buf = new char[4 * 1024]; // 4 KB char buffer
		int len;
		while ((len = buff.read(buf, 0, buf.length)) != -1) {
			out.write(buf, 0, len);
		}
		out.println("</BODY></HTML>");

	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		PrintWriter out = resp.getWriter();
		out.println("Hello Get");
	}


	public Map<String, String> getHeadersAndCookies() throws IOException {

		URL url = new URL(
				"http://bus.gov.il/WebForms/wfrmMain.aspx?width=1024&company=1&language=he&state=");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		Map<String, String> responseHeaders = new HashMap<String, String>();

		// Get all headers from the server.
		// Note: The first call to getHeaderFieldKey() will implicit send
		// the HTTP request to the server.
		for (int i = 0;; i++) {
			String headerName = conn.getHeaderFieldKey(i);
			String headerValue = conn.getHeaderField(i);

			if (headerName == null && headerValue == null) {
				// No more headers
				break;
			} else {
				responseHeaders.put(headerName, headerValue);
			}

		}

		return responseHeaders;
	}

	/**
	 * Enum representing the available services (each contains it's task name)
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
