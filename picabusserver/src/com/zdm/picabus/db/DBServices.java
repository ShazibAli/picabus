/**
 * 
 */
package com.zdm.picabus.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.google.cloud.sql.jdbc.Connection;
import com.google.cloud.sql.jdbc.PreparedStatement;
import com.google.cloud.sql.jdbc.ResultSet;
import com.zdm.picabus.server.entities.Line;
import com.zdm.picabus.server.entities.Station;
import com.zdm.picabus.server.entities.Trip;


public class DBServices implements IDBServices {



	/* (non-Javadoc)
	 * @see com.zdm.picabus.db.IDBServices#getRoute(java.lang.Long, java.lang.Long, java.lang.Integer)
	 */
	@Override
	public Set<Station> getRoute(Long tripId, Long stopId, Integer direction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Line getNextDepartureTimePerLine(int lineNumber, double latitude,
			double longitude, String clientTimeString) {
		Connection c = null;
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = (Connection) DriverManager
					.getConnection("jdbc:google:rdbms://test/picabusdata");

			String statement = "SELECT  trips.trip_id, arrival_time, stop_id, stop_sequence, stop_headsign, routes.route_id, service_id, direction_id, route_short_name as \"Line Number\", route_long_name, agency_name "
					+ "FROM `picabusdata`.`stop_times`, `picabusdata`.`trips`,  `picabusdata`.`routes`, `picabusdata`.`agency` "
					+ "WHERE stop_id=29335 and departure_time > \"08:24:00\" "
					+ "and departure_time < \"08:40:00\" "
					+ "and stop_times.trip_id = trips.trip_id "
					+ "and trips.route_id = routes.route_id "
					+ "and agency.agency_id = routes.agency_id "
					+ "GROUP BY route_short_name";
			PreparedStatement stmt = c.prepareStatement(statement);
			ResultSet rs = stmt.executeQuery();
			Set<Line> lines = new HashSet<Line>();
			while (rs.next()) {
				
				long tripID = rs.getLong("trip_id");
				Time arrivalTime = rs.getTime("arrival_time");
				int stopID = rs.getInt("stop_id");
				int stop_sequence = rs.getInt("stop_sequence");
				String stopHeadsign = rs.getString("stop_headsign");
				long routeID = rs.getLong("route_id");
				long serviceID = rs.getLong("service_id");
				int directionID = rs.getInt("direction_id");
				int currentLineNumber = rs.getInt("Line Number");
				String routeLongName = rs.getString("route_long_name");
				String agency = rs.getString("agency_name");
				
				
				
				Line line = new Line();
				//Trip trip = new Trip(tripID, routeLongName, directionID, currentLineNumber, arrivalTime, agency, stopID);	
				Trip trip = new Trip();
				List trips = new ArrayList<Trip>();
				trips.add(trip);
				line.setTrips(trips);
				line.setBiDirectional(directionID == 1 ? true : false);
				lines.add(line);
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

		return null;
	}

	@Override
	public Collection<Line> getRouteDetails() {
		// TODO Auto-generated method stub
		return null;
	}

}
