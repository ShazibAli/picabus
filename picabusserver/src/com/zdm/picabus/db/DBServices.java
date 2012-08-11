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
import com.zdm.picabus.server.entities.Company;
import com.zdm.picabus.server.entities.Line;
import com.zdm.picabus.server.entities.Station;
import com.zdm.picabus.server.entities.Trip;


public class DBServices implements IDBServices {

	private static String URL = "jdbc:google:rdbms://test/picabusdata";



	@Override
	public Line getNextDepartureTimePerLine(int lineNumber, double latitude,
			double longitude, String clientTimeString) {
		Connection c = null;
		boolean [] bidirectional = {false, false};
		String stopHeadsign = null;
		Line line = null;
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = (Connection) DriverManager.getConnection(URL);

			// build SQL statement
			String statement = "SELECT  trips.trip_id, arrival_time, stop_id, stop_sequence, stop_headsign, routes.route_id, service_id, direction_id, route_short_name as \"Line Number\", route_long_name, agency_name "
					+ "FROM `picabusdata`.`stop_times`, `picabusdata`.`trips`,  `picabusdata`.`routes`, `picabusdata`.`agency` "
					+ "WHERE stop_id=29335 and departure_time > \"08:24:00\" "
					+ "and departure_time < \"08:40:00\" "
					+ "and stop_times.trip_id = trips.trip_id "
					+ "and trips.route_id = routes.route_id "
					+ "and agency.agency_id = routes.agency_id "
					+ "and route_short_name = " + lineNumber
					+ " GROUP BY route_short_name, direction_id";
			PreparedStatement stmt = c.prepareStatement(statement);
			ResultSet rs = stmt.executeQuery();
			
			List<Trip> trips = new ArrayList<Trip>();
			line = new Line();
			
			while (rs.next()) {
				
				// extract values from the result set
				stopHeadsign = rs.getString("stop_headsign");
				long tripID = rs.getLong("trip_id");
				Time arrivalTime = rs.getTime("arrival_time");
				long stopID = rs.getLong("stop_id");
				int stop_sequence = rs.getInt("stop_sequence");
				long routeID = rs.getLong("route_id");
				long serviceID = rs.getLong("service_id");
				int directionID = rs.getInt("direction_id");
				int currentLineNumber = rs.getInt("Line Number");
				String routeLongName = rs.getString("route_long_name");
				String agency = rs.getString("agency_name");
				
				// create a new Trip and set it's values
				Trip trip = new Trip(tripID, routeLongName, directionID, currentLineNumber, arrivalTime, Company.getCompanyByString(agency), stopID, stop_sequence, routeID, serviceID);		
				trips.add(trip);
				
				// update direction (assuming directionID possible values are 0 or 1)
				bidirectional[directionID] = true;
								
			}
			line.setStopHeadsign(stopHeadsign);
			line.setTrips(trips);
			line.setBiDirectional(bidirectional[0] && bidirectional[1]);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (SQLException ignore) {
				}
		}
		return line;
	}


	@Override
	public Set<Line> getDepartureTimes(double latitude, double longitude, String clientTimeString) {
		Connection c = null;
		Set<Line> lines = null;
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = (Connection) DriverManager
					.getConnection(URL);

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
			lines = new HashSet<Line>();
			while (rs.next()) {
				
				String stopHeadsign = rs.getString("stop_headsign");
				long tripID = rs.getLong("trip_id");
				Time arrivalTime = rs.getTime("arrival_time");
				long stopID = rs.getLong("stop_id");
				int stop_sequence = rs.getInt("stop_sequence");
				long routeID = rs.getLong("route_id");
				long serviceID = rs.getLong("service_id");
				int directionID = rs.getInt("direction_id");
				int currentLineNumber = rs.getInt("Line Number");
				String routeLongName = rs.getString("route_long_name");
				String agency = rs.getString("agency_name");
					
				Line line = new Line();
				Trip trip = new Trip(tripID, routeLongName, directionID, currentLineNumber, arrivalTime, Company.valueOf(agency), stopID, stop_sequence, routeID, serviceID);	
				List<Trip> trips = new ArrayList<Trip>();
				trips.add(trip);
				line.setTrips(trips);
				line.setBiDirectional(directionID == 1 ? true : false);
				line.setStopHeadsign(stopHeadsign);
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
		return lines;
	}

	@Override
	public Set<Station> getRoute(Long tripId, Long stopId, Integer direction) {
		// TODO Auto-generated method stub
		return null;
	}

}
