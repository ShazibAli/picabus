package com.zdm.picabus.db.parser.fileparsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import com.zdm.picabus.db.connectivity.Tables;
import com.zdm.picabus.db.parser.IFileParser;

public class StopTimes implements IFileParser {

	private String gtfsFilePath;
	private Connection dbConnection;
	private final int BATCH_SIZE = 50000;

	public StopTimes(Connection dbConnection, String gtfsFilePath) {
		this.gtfsFilePath = gtfsFilePath;
		this.dbConnection = dbConnection;
	}

	@Override
	public String parseAndUpdateDB() {

		BufferedReader br = null;
		try {
			File file = new File(gtfsFilePath);
			FileInputStream in = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {

			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;

		Long tripID = (long) 0, stopID = (long) 0;
		Time arrivalTime = null, departureTime = null;
		String stopHeadsign = null;
		int stopSequence = 0;
		Boolean pickupType = null, dropOffType = null;
		java.util.Date time;
		DateFormat format = new SimpleDateFormat("HH:mm:ss");

		try {
			int totalLinesCount = 1;

			// we want to batch 1000 inserts each time
			dbConnection.setAutoCommit(false);
			PreparedStatement pstmt;
			pstmt = dbConnection
					.prepareStatement("INSERT INTO "
							+ Tables.STOPTIMES
							+ "(trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type) VALUES(?,?,?,?,?,?,?,?)");

			// move over the headline
			br.readLine();

			Boolean skip = false;

			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				int i = 0;

				while (st.hasMoreTokens()) {
					// get next token and store it in the array
					String currentData = st.nextToken();

					if (currentData != null && !currentData.isEmpty()) {
						switch (i) {
						case 0:
							if (currentData.contains("#")) {
								currentData = (currentData.split("#"))[0]
										+ (currentData.split("#"))[1];
								tripID = Long.parseLong(currentData);
							} else if (currentData.matches("[0-9]+")) {
								tripID = Long.parseLong(currentData);
							} else {
								skip = true;
							}
							i++;
							break;
						case 1:
							time = format.parse(currentData);
							arrivalTime = new java.sql.Time(time.getTime());
							i++;
							break;
						case 2:
							time = format.parse(currentData);
							departureTime = new java.sql.Time(time.getTime());
							i++;
							break;
						case 3:
							stopID = Long.parseLong(currentData);
							i++;
							break;
						case 4:
							stopSequence = Integer.parseInt(currentData);
							i++;
							break;
						case 5:
							stopHeadsign = currentData;
							i++;
							break;
						case 6:
							pickupType = (currentData.equals("0") ? false
									: true);
							i++;
							break;
						case 7:
							dropOffType = (currentData.equals("0") ? false
									: true);
							i++;
							break;
						}
					}
					
				}

				if (skip) {
					skip = false;
					continue;
				}
				pstmt.setLong(1, tripID);
				pstmt.setTime(2, arrivalTime);
				pstmt.setTime(3, departureTime);
				pstmt.setLong(4, stopID);
				pstmt.setInt(5, stopSequence);
				pstmt.setString(6, stopHeadsign);
				pstmt.setBoolean(7, pickupType);
				pstmt.setBoolean(8, dropOffType);

				pstmt.addBatch();
				totalLinesCount++;
				if (totalLinesCount % BATCH_SIZE == 0) {
					System.out.println("*** Log *** Number of lines inserted: "
							+ totalLinesCount);
					pstmt.executeBatch();
					dbConnection.commit();
					pstmt.clearBatch();
					pstmt = dbConnection
							.prepareStatement("INSERT INTO "
									+ Tables.STOPTIMES
									+ "(trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type) VALUES(?,?,?,?,?,?,?,?)");

				}

			}

			pstmt.executeBatch();
			dbConnection.commit();
			pstmt.close();

			// close the file
			br.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
