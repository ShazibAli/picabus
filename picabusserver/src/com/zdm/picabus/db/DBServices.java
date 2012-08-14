package com.zdm.picabus.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.google.cloud.sql.jdbc.Connection;
import com.google.cloud.sql.jdbc.PreparedStatement;
import com.google.cloud.sql.jdbc.ResultSet;
import com.zdm.picabus.db.connectivity.Tables;
import com.zdm.picabus.server.entities.Company;
import com.zdm.picabus.server.entities.Line;
import com.zdm.picabus.server.entities.Stop;
import com.zdm.picabus.server.entities.Trip;

public class DBServices implements IDBServices {

	private static final String URL = "jdbc:google:rdbms://test/picabusdata";
	private static final int minuteToMsFactor = 60000;

	@Override
	public Line getNextDepartureTimePerLine(int lineNumber, double latitude,
			double longitude, String clientTimeString, int timeIntervalInMinutes) {
		
		Stop stop = getNearestStop(latitude, longitude, false, 1);
		Connection c = null;
		boolean [] bidirectional = {false, false};
		String stopHeadsign = null;
		Line line = null;
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = (Connection) DriverManager.getConnection(URL);
			Time departureTime = Time.valueOf(clientTimeString);
			long upperLimitTimeInMs = departureTime.getTime() + (timeIntervalInMinutes * minuteToMsFactor);
			Time upperLimitTime = new Time(upperLimitTimeInMs);
				
			// build SQL statement
			String statement = "SELECT  trips.trip_id, arrival_time, stop_id, stop_sequence, stop_headsign, " +
					"routes.route_id, service_id, direction_id, route_short_name as \"Line Number\", route_long_name, agency_name "
					+ "FROM " + Tables.STOPTIMES.getTableName() +", " + Tables.TRIPS.getTableName() + ", " 
					+ Tables.ROUTES.getTableName() + ", " + Tables.AGENCY.getTableName() + " "
					+ "WHERE stop_id = ? and departure_time > ? "
					+ "and departure_time < ? "
					+ "and stop_times.trip_id = trips.trip_id "
					+ "and trips.route_id = routes.route_id "
					+ "and agency.agency_id = routes.agency_id "
					+ "and route_short_name = ? "
					+ "GROUP BY route_short_name, direction_id";
			
			PreparedStatement stmt = c.prepareStatement(statement);
			
			stmt.setLong(1, stop.getStopID());
			stmt.setTime(2, departureTime);
			stmt.setTime(3, upperLimitTime);
			stmt.setInt(4, lineNumber);
			
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
					// ignoring this exception
				}
		}
		return line;
	}

	@Override
	public Stop getNearestStop(double latitude, double longitude, boolean isRecursive, int maxNumOfIterations) {
		double radiusInKM = 0.005;
		int numOfIterations = 1;
		Stop stop = getNearestStop(latitude, longitude, radiusInKM);
		if (!isRecursive) {
			return stop;
		}
		else {
			while (stop == null && numOfIterations < maxNumOfIterations) {
				numOfIterations++;
				radiusInKM *= 2;
				stop = getNearestStop(latitude, longitude, radiusInKM);
			}
		}
		return stop;
	}
	
	private Stop getNearestStop(double latitude, double longitude, double radiusInKM) {
		Connection c = null;
		final int limit = 1;
		Stop stop = null;
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = (Connection) DriverManager
					.getConnection(URL);

			String statement = "SELECT stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_lon, " +
					"( 6371 * acos( cos( radians(?) ) * cos( radians(stop_lat ) ) * cos( radians( stop_lon ) " +
					"- radians(?) ) + sin( radians(?) ) * sin( radians( stop_lat ) ) ) )  AS distance "
					+ " FROM " + Tables.STOPS.getTableName()  
					+ " HAVING distance < ?" 
					+ " ORDER BY distance ASC"
					+ " LIMIT " + limit;
			
			PreparedStatement stmt = c.prepareStatement(statement);
			
			stmt.setDouble(1, latitude);
			stmt.setDouble(2, longitude);
			stmt.setDouble(3, latitude);
			stmt.setDouble(4, radiusInKM);
			
			ResultSet rs = stmt.executeQuery();
						
			while (rs.next()) {
				long stopID = rs.getLong("stop_id");
				int stopCode = rs.getInt("stop_code");
				String stopName = rs.getString("stop_name");
				String stopDescription = rs.getString("stop_desc");
				stop = new Stop(stopID, stopCode, stopName, stopDescription, latitude, longitude, -1);
				stop.setDepartureTimeString(null);
	
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (SQLException ignore) {
					// ignoring this exception
				}
		}
		return stop;
	}

	@Override
	public List<Stop> getRouteDetails(long tripID,
			int currentStopSequenceNumber) {
		Connection c = null;
		Stop stop = null;
		List<Stop> stops = new ArrayList<Stop>();
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = (Connection) DriverManager
					.getConnection(URL);

			String statement = "SELECT  * FROM " + Tables.STOPTIMES.getTableName() + ", " + Tables.STOPS.getTableName() + 
					" where stop_times.trip_id = ? and stop_sequence > ? and stop_times.stop_id = stops.stop_id ORDER BY stop_sequence ASC";		
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setLong(1, tripID);
			stmt.setInt(2,currentStopSequenceNumber);
			ResultSet rs = stmt.executeQuery();
						
			while (rs.next()) {
				long stopID = rs.getLong("stop_id");
				int stopCode = rs.getInt("stop_code");
				String stopName = rs.getString("stop_name");
				String stopDescription = rs.getString("stop_desc");
				int stopSequenceNumber = rs.getInt("stop_sequence");
				double latitude = rs.getDouble("stop_lat");
				double longitude = rs.getDouble("stop_lon");
				String departureTimeString = rs.getTime("departure_time").toString();
				
				stop = new Stop(stopID, stopCode, stopName, stopDescription, latitude, longitude, stopSequenceNumber);
				stop.setDepartureTimeString(departureTimeString);
				stops.add(stop);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (SQLException ignore) {
					// ignoring this exception
				}
		}
		return stops;
	}
}