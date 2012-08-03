package com.zdm.picabus.db.parser;

import java.sql.Connection;

import com.zdm.picabus.db.connectivity.ConnectionPool;
import com.zdm.picabus.db.parser.fileparsers.Agency;
import com.zdm.picabus.db.parser.fileparsers.Calendar;
import com.zdm.picabus.db.parser.fileparsers.Routes;
import com.zdm.picabus.db.parser.fileparsers.StopTimes;
import com.zdm.picabus.db.parser.fileparsers.Stops;
import com.zdm.picabus.db.parser.fileparsers.Trips;

public class FileParsersFactory {

	public static IFileParser getFileParser(Connection dbConnection,
			Files fileName) {

		Connection c = ConnectionPool.getConnection();
		if (c == null) {
			System.err.println("Error creating new connection");
		}

		String gtfsRootDirPath = "C:/Users/user/Desktop/GTFS"; // TODO: get this
																// from the ini
																// file

		if (fileName.equals(Files.AGENCY))
			return new Agency(c, gtfsRootDirPath + "/" + Files.AGENCY);

		else if (fileName.equals(Files.CALENDAR))
			return new Calendar(c, gtfsRootDirPath + "/" + Files.CALENDAR);

		else if (fileName.equals(Files.ROUTES))
			return new Routes(c, gtfsRootDirPath + "/" + Files.ROUTES);

		else if (fileName.equals(Files.STOPTIMES))
			return new StopTimes(c, gtfsRootDirPath + "/" + Files.STOPTIMES);
		
		else if (fileName.equals(Files.STOPS))
			return new Stops(c, gtfsRootDirPath + "/" + Files.STOPS);
		
		else if (fileName.equals(Files.TRIPS))
			return new Trips(c, gtfsRootDirPath + "/" + Files.TRIPS);

		else
			return null;
	}
}
