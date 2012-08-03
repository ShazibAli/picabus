package com.zdm.picabus.db.parser;
import java.sql.Connection;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import com.zdm.picabus.db.connectivity.ConnectionPool;

public class Parser {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		
		Connection c = ConnectionPool.getConnection();
		if (c == null) {
			System.err.println("Error creating new connection");
		}
		IFileParser fp;
		String errors;
		
	/*	long startTime = System.currentTimeMillis(); 
		fp = FileParsersFactory.getFileParser(c, Files.TRIPS);
		errors = fp.parseAndUpdateDB();
		String taskTime = getTaskTime(startTime);
		System.out.println("*** Log *** finished parsing and inserting trips.txt, Total time: " + taskTime);
		
		startTime = System.currentTimeMillis();
		fp = FileParsersFactory.getFileParser(c, Files.AGENCY);
		errors = fp.parseAndUpdateDB();
		taskTime = getTaskTime(startTime);
		System.out.println("*** Log *** finished parsing and inserting agency.txt, Total time: " + taskTime);

		startTime = System.currentTimeMillis();
		fp = FileParsersFactory.getFileParser(c, Files.CALENDAR);
		errors = fp.parseAndUpdateDB();
		taskTime = getTaskTime(startTime);
		System.out.println("*** Log *** finished parsing and inserting calendar.txt, Total time: " + taskTime);

		startTime = System.currentTimeMillis();
		fp = FileParsersFactory.getFileParser(c, Files.STOPS);
		errors = fp.parseAndUpdateDB();
		taskTime = getTaskTime(startTime);
		System.out.println("*** Log *** finished parsing and inserting stops.txt, Total time: " + taskTime);
		
		startTime = System.currentTimeMillis();
		fp = FileParsersFactory.getFileParser(c, Files.ROUTES);
		errors = fp.parseAndUpdateDB();
		taskTime = getTaskTime(startTime);
		System.out.println("*** Log *** finished parsing and inserting routes.txt, Total time: " + taskTime); */
		
		long startTime = System.currentTimeMillis();
		fp = FileParsersFactory.getFileParser(c, Files.STOPTIMES);
		errors = fp.parseAndUpdateDB();
		String taskTime = getTaskTime(startTime);
		System.out.println("*** Log *** finished parsing and inserting stop_times.txt, Total time: " + taskTime);
		
		System.out.println("Errors occured suring the parsing process: " + errors);
		
		//handle errors log

	}
	
	static String getTaskTime(long startTime) {
		long millis = (System.currentTimeMillis() - startTime);
		return String.format("%d min, %d sec", 
			    TimeUnit.MILLISECONDS.toMinutes(millis),
			    TimeUnit.MILLISECONDS.toSeconds( millis) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes( millis))
			);
	}

}
