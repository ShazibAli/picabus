/**
 * 
 */
package com.zdm.picabus.server.datastore.mockdata;

import java.util.Arrays;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;


public class MockDataHandler {
	
	
	/**
	 * Used to populate mock data and store it in the data store
	 */
	private void generateMockData() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity line1 = new Entity("Line", "");
		Entity line2 = new Entity("Line", "");
		Entity line3 = new Entity("Line", "");
		
		// batch the lines as a single transaction
		List<Entity> lines = Arrays.asList(line1, line2, line3);
		datastore.put(lines);
	}
	
	/**
	 * Used to delete all the mock data from the data store
	 */	
	private void deleteAllMockData() {
		
	}
}
