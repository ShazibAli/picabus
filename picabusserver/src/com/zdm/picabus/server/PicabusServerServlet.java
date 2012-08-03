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

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;

@SuppressWarnings("serial")
public class PicabusServerServlet extends HttpServlet {
	private final static String FREE_TEXT = "תחנת רכבת סוקולוב כפר סבא לאזור תעשייה רעננה";

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
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
		/*Connection c = null;
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = (Connection) DriverManager
					.getConnection("jdbc:google:rdbms://test/sakila");

			String statement = "SELECT * FROM actor LIMIT 100";
			PreparedStatement stmt = c.prepareStatement(statement);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int guestName = rs.getInt("actor_id");
				String content = rs.getString("first_name");
				String id = rs.getString("last_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (SQLException ignore) {
				}
		}
		
*/
		/*JSONObject myjson = null;
		try {
			myjson = new JSONObject("{\"a\": 5,\"b\": 6}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	    String reqUrl = req.getRequestURL().toString();
	    String queryString = req.getQueryString();   // d=789
	    
	    
	    resp.setContentType("text/html");
	    
	    String httpHeadersString = "Some Headers";
	    String requestPayloadString = "Some ayload";
	    
		
	    out.println("<BODY BGCOLOR=\"#FDF5E6\">\n" +
                "<H1 > Request Details</H1>\n" +
                "<B>Request Method: </B>" +
                req.getMethod() + "<BR>\n" +
                "<B>Request URI: </B>" +
                req.getRequestURI() + "<BR>\n" +
                "<B>Request Protocol: </B>" +
                req.getProtocol() + "<BR><BR>\n" +
                "<TABLE BORDER=1>\n" +
                "<TR BGCOLOR=\"#FFAD00\">\n" +
                "<TH>Header Name<TH>Header Value");
	    Enumeration headerNames = req.getHeaderNames();
	    while(headerNames.hasMoreElements()) {
	      String headerName = (String)headerNames.nextElement();
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
	
	
	
	public void createRequestReport(HttpServletRequest request, PrintWriter out) {

	}

	public String getDirections(String freeText,
			Map<String, String> originalHeaders) throws IOException {
		// StringBuffer sb = null;

		int indexOfAttr = originalHeaders.get("set-cookie").indexOf(
				"ASP.NET_SessionId=");
		indexOfAttr += "ASP.NET_SessionId=".length();
		int indexOfSemiColon = originalHeaders.get("set-cookie").indexOf(";",
				indexOfAttr);
		String sessionID = "mot"
				+ originalHeaders.get("set-cookie").substring(indexOfAttr,
						indexOfSemiColon);
		String payload = "{\"sParams\":\"" + FREE_TEXT + "\",\"strSession\":\""
				+ sessionID + "\"}";

		URL url = new URL(
				"http://bus.gov.il/WebForms/wfrmMain.aspx?width=1024&company=1&language=he&state=");
		HTTPRequest request = new HTTPRequest(url, HTTPMethod.POST);

		// URLFetchService fetcher =
		// URLFetchServiceFactory.getURLFetchService();

		request.addHeader(new HTTPHeader("Content-type", originalHeaders
				.get("content-type")));
		request.addHeader(new HTTPHeader("Content-length", Integer
				.toString(payload.length())));
		request.setPayload(payload.getBytes());

		// HTTPResponse response = fetcher.fetch(request);
		// String responseString = (response.getContent());

		/*
		 * HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		 * conn.setDoOutput(true); conn.setDoInput(true);
		 * conn.setRequestMethod("POST"); //conn.setRequestProperty("Cookie",
		 * originalHeaders.get("Set-Cookie")); conn.setRequestProperty(
		 * "Content-type", originalHeaders.get("content-type") );
		 * conn.setRequestProperty("Content-length",
		 * Integer.toString(payload.length()));
		 * 
		 * 
		 * sb = new StringBuffer(); InputStream rawInStream =
		 * conn.getInputStream();
		 * 
		 * // get response BufferedReader rdr = new BufferedReader(new
		 * InputStreamReader( rawInStream)); String line;
		 * 
		 * while ((line = rdr.readLine()) != null) { sb.append(line); } } catch
		 * (IOException e) {
		 * 
		 * e.printStackTrace(); } return sb.toString();
		 */

		return null;
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

}
