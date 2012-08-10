/**
 * 
 */
package com.zdm.picabus.server.datastore;

import java.util.Collection;

import com.zdm.picabus.server.entities.Line;


public interface IDataStoreHandler {
	
	/**
	 * 
	 * @param lineNumber
	 *            the line number 
	 * @param latitude
	 *            the latitude value of the bus station
	 * @param longitude
	 *            the longitude value of the bus station
	 * @param clientTime
	 *            the time of the client (can differ from the server time)
	 * 
	 * @return bus line containing the relevant data
	 */
	public Line getDepartueTime(int lineNumber, double latitude, double longitude, String clientTimeString);
	
	public Collection<Line> getRouteDetails();
	
	
}
