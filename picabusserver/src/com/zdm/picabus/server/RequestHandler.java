package com.zdm.picabus.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.Image.Format;
import com.zdm.picabus.server.entities.Line;

public class RequestHandler {
		
		private HttpServletRequest req;
		private HttpServletResponse resp;
		
		
		public HttpServletResponse getRoute() {
			return null;
		}
		
		
		public Set<Line> getDepartueTime(int lineNumber, double latitude, double longitude, String clientTimeString) {
			return null;


		}
		
	
	
}
