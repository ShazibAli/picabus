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
import com.zdm.picabus.server.exceptions.EmptyResultException;

public class DBServices implements IDBServices {

	private static final String URL = "jdbc:google:rdbms://test/picabusdata";
	private static final int minuteToMsFactor = 60000;

	@Override
	public Line getNextDepartureTimePerLine(int lineNumber, double latitude,
			double longitude, String clientTimeString, int timeIntervalInMinutes) throws EmptyResultException {
		
		Stop stop = getNearestStop(latitude, longitude, false, 1);
		if (stop == null) {
			return null;
		}
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
			
			boolean rsEmpty = true;
			
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
				
				rsEmpty = false;
			}
			line.setStopHeadsign(stopHeadsign);
			line.setTrips(trips);
			line.setBiDirectional(bidirectional[0] && bidirectional[1]);
			rs.close();
			stmt.close();
			
			// validate that the result set is not empty
			if (rsEmpty) {
				throw new EmptyResultException(
						"Couldn't locate the station based on the given GPS coordinates");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
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
	public Stop getNearestStop(double latitude, double longitude, boolean isRecursive, int maxNumOfIterations) throws EmptyResultException {
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
	
	private Stop getNearestStop(double latitude, double longitude, double radiusInKM) throws EmptyResultException {
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

			
			boolean rsEmpty = true;
			
			while (rs.next()) {
				long stopID = rs.getLong("stop_id");
				int stopCode = rs.getInt("stop_code");
				String stopName = rs.getString("stop_name");
				String stopDescription = rs.getString("stop_desc");
				stop = new Stop(stopID, stopCode, stopName, stopDescription,
						latitude, longitude, -1);
				stop.setDepartureTimeString(null);
				rsEmpty = false;
			}
			rs.close();
			stmt.close();

			// validate that the result set is not empty
			if (rsEmpty) {
				throw new EmptyResultException(
						"Couldn't locate the station based on the givven GPS coordinates");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
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
			int currentStopSequenceNumber) throws EmptyResultException {
		Connection c = null;
		Stop stop = null;
		List<Stop> stops = new ArrayList<Stop>();
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = (Connection) DriverManager
					.getConnection(URL);

			String statement = "SELECT  * FROM " + Tables.STOPTIMES.getTableName() + ", " + Tables.STOPS.getTableName() + 
					" where stop_times.trip_id = ? and stop_sequence >= ? and stop_times.stop_id = stops.stop_id ORDER BY stop_sequence ASC";		
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setLong(1, tripID);
			stmt.setInt(2,currentStopSequenceNumber);
			ResultSet rs = stmt.executeQuery();
			
			boolean rsEmpty = true;
			
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
				
				rsEmpty = false;
			}
			rs.close();
			stmt.close();

			
			// validate that the result set is not empty
			if (rsEmpty) {
				throw new EmptyResultException(
						"Couldn't locate the station based on the givven GPS coordinates");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
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

	@Override
	public long increaseUserPoints(long userId, int numOfPoints) {
		Connection c = null;
		long currentNumberOfPoints = 0;
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = (Connection) DriverManager
					.getConnection(URL);

			// first we will check if the user already has some points
			String statement = "SELECT user_id, points FROM " + Tables.USERS_POINTS.getTableName() + " WHERE user_id = ?";		
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setLong(1, userId);
			ResultSet rs = stmt.executeQuery();
			boolean exists = rs.next();
			
			if (exists) { // add the new sum of points to the user
				currentNumberOfPoints = rs.getLong("points");
				currentNumberOfPoints += numOfPoints;
				statement = "UPDATE " + Tables.USERS_POINTS.getTableName() + " SET points = ? WHERE user_id = ?)";		
				stmt = c.prepareStatement(statement);
				stmt.setLong(1, currentNumberOfPoints);
				stmt.setLong(2, userId);
				stmt.executeUpdate();				
				
			}
			else { //user still has no points
				currentNumberOfPoints = numOfPoints;
				statement = "INSERT INTO " + Tables.USERS_POINTS.getTableName() + " (user_id, points) VALUES (?,?)";		
				stmt = c.prepareStatement(statement);
				stmt.setLong(1, userId);
				stmt.setLong(2, currentNumberOfPoints);
				stmt.executeUpdate();
			}
			stmt.close();

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
		return currentNumberOfPoints;
		
	}



	/**
	 * This method is used to clear the reports entries of the given user identified by the reporter id
	 * 
	 * @param tripId Current trip's ID
	 * @param reporterId Current reporter ID
	 * @return true if the operation was successful, false otherwise
	 */
	private boolean clearReports(long tripId, long reporterId) {
		Connection c = null;
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = (Connection) DriverManager
					.getConnection(URL);

			// clearing the location report
			String statement = "DELETE FROM " + Tables.CURRENT_LOCATION_REPORTS.getTableName() + " WHERE trip_id = ? AND reporter_id = ?";		
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setLong(1, tripId);
			stmt.setLong(2, reporterId);
			stmt.executeUpdate();
			
			// clearing the textual report
			statement = "DELETE FROM " + Tables.TEXTUAL_TRIP_REPORTS.getTableName() + " WHERE trip_id = ? AND reporter_id = ?";		
			stmt = c.prepareStatement(statement);
			stmt.setLong(1, tripId);
			stmt.setLong(2, reporterId);
			
			
			stmt.executeUpdate();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (SQLException ignore) {
					// ignoring this exception
				}
		}
		return true;
		
	}

	@Override
	public boolean reportCurrentLocation(long userId, double longitude, double latitude,
			long tripId) {
		/*
		 * Note: For now we let the DB generate the time-stamp which may be in different time zone. 
		 * The records will be sorted by the last report time (ideally this time should be taken from the client later on) 
		 */
		Connection c = null;
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = (Connection) DriverManager
					.getConnection(URL);

			// first we will check if the user already checked in
			String statement = "SELECT reporter_id, trip_id FROM " + Tables.CURRENT_LOCATION_REPORTS.getTableName() + " WHERE trip_id = ? AND reporter_id = ?";		
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setLong(1, tripId);
			stmt.setLong(2, userId);
			ResultSet rs = stmt.executeQuery();
			boolean exists = rs.next();
			
			if (exists) { // update 

				statement = "UPDATE " + Tables.CURRENT_LOCATION_REPORTS.getTableName() + " SET longitude = ?, latitude = ?  WHERE reporter_id = ? and trip_id = ?";		
				stmt = c.prepareStatement(statement);
				stmt.setDouble(1, longitude);
				stmt.setDouble(2, latitude);
				stmt.setLong(3, userId);
				stmt.setLong(4, tripId);
				stmt.executeUpdate();				
			}
			else { //user has no existing prior report
				
				statement = "INSERT INTO " + Tables.CURRENT_LOCATION_REPORTS.getTableName() + " (trip_id, longitude, latitude, reporter_id) VALUES (?,?,?,?)";		
				stmt = c.prepareStatement(statement);
				stmt.setLong(1, tripId);
				stmt.setDouble(2, longitude);
				stmt.setDouble(3, latitude);
				stmt.setLong(4, userId);
				
				stmt.executeUpdate();
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (SQLException ignore) {
					// ignoring this exception
				}
		}
		return true;
	}

	@Override
	public boolean reportCheckout(long userId, long tripId) {
		/* on checkout we want to clear the location entry and the textual 
		   report made by these user (later on we can do a mechanism that clears the textual reports only when the trip ends) */
		return clearReports(tripId, userId);
	}

	@Override
	public boolean reportTripDescription(long userId, long tripId, String message) {
		// assuming that this method will be called only if the user is checked in
		Connection c = null;
		try {
			DriverManager.registerDriver(new AppEngineDriver());
			c = (Connection) DriverManager
					.getConnection(URL);

			// first we will check if the user already has some report
			String statement = "SELECT reporter_id, trip_id, report FROM " + Tables.TEXTUAL_TRIP_REPORTS.getTableName() + " WHERE trip_id = ? AND reporter_id = ?";		
			PreparedStatement stmt = c.prepareStatement(statement);
			stmt.setLong(1, tripId);
			stmt.setLong(2, userId);
			ResultSet rs = stmt.executeQuery();
			boolean exists = rs.next();
			
			if (exists) { // add the new textual report

				statement = "UPDATE " + Tables.TEXTUAL_TRIP_REPORTS.getTableName() + " SET report = ? WHERE reporter_id = ? and trip_id = ?";		
				stmt = c.prepareStatement(statement);
				stmt.setString(1, message);
				stmt.setLong(2, userId);
				stmt.setLong(3, tripId);
				stmt.executeUpdate();				
			}
			else { //user has no existing report
				
				statement = "INSERT INTO " + Tables.TEXTUAL_TRIP_REPORTS.getTableName() + " (reporter_id, trip_id, report) VALUES (?,?,?)";		
				stmt = c.prepareStatement(statement);
				stmt.setLong(1, userId);
				stmt.setLong(2, tripId);
				stmt.setString(3, message);
				stmt.executeUpdate();
			}
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (c != null)
				try {
					c.close();
				} catch (SQLException ignore) {
					// ignoring this exception
				}
		}
		return true;
	}
}
