package com.zdm.picabus.db;

import java.sql.Time;
import java.util.Set;

import com.zdm.picabus.db.entities.BusLine;
import com.zdm.picabus.db.entities.BusStation;

public interface IDBServices {

	/**
	 * 
	 * @param longitude
	 *            the longitude value of the bus station
	 * @param latitude
	 *            the latitude value of the bus station
	 * @param lineNumbers
	 *            all the line numbers passing in this station
	 * @param clientTime
	 *            the time of the client (can be different from the server time)
	 * 
	 * @return all the bus line and their relevant data
	 */
	public Set<BusLine> getLinesData(Double longitude, Double latitude,
			Set<Integer> lineNumbers, Time clientTime);

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
	public Set<BusStation> getRoute(Long tripId, Long stopId, Integer direction);

}
