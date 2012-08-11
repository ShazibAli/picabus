package com.zdm.picabus.db;

import java.sql.Time;
import java.util.Collection;
import java.util.Set;


import com.zdm.picabus.server.entities.Line;
import com.zdm.picabus.server.entities.Station;

public interface IDBServices {

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
	public Line getNextDepartureTimePerLine(int lineNumber, double latitude, double longitude, String clientTimeString);
	
	public Collection<Line> getRouteDetails();
	/**
	 * 
	 * @param tripId
	 *            the id of the chosen trip
	 * @param stopId
	 *            the id of the current stop
	 * @param direction
	 *            the direction (can be 1 or 0)
	 * @return return all the stations data in the requested root
	 */
	public Set<Station> getRoute(Long tripId, Long stopId, Integer direction);

}
