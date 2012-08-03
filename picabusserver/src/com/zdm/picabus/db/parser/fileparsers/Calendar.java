package com.zdm.picabus.db.parser.fileparsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import com.zdm.picabus.db.connectivity.Tables;
import com.zdm.picabus.db.parser.IFileParser;

public class Calendar implements IFileParser {

	private String gtfsFilePath;
	private Connection dbConnection;

	public Calendar(Connection dbConnection, String gtfsFilePath) {
		this.gtfsFilePath = gtfsFilePath;
		this.dbConnection = dbConnection;
	}

	@Override
	public String parseAndUpdateDB() {

		File file = new File(gtfsFilePath);
		String line = null;
		BufferedReader br = null;
		Boolean[] days = new Boolean[8]; // first place is always empty
		Long serviceID = (long) 0;
		Date startDate = null;
		Date endDate = null;
		DateFormat format = new SimpleDateFormat("yyyyMMdd");

		try {
			int totalLinesCount = 1;

			// we want to batch 1000 inserts each time
			dbConnection.setAutoCommit(false);
			PreparedStatement pstmt;
			pstmt = dbConnection
					.prepareStatement("INSERT INTO "
							+ Tables.CALENDAR
							+ "(service_id,sunday,monday,tuesday,wednesday,thursday,friday,saturday,start_date,end_date) VALUES(?,?,?,?,?,?,?,?,?,?)");
			br = new BufferedReader(new FileReader(file));
			// move over the headline
			br.readLine();

			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				int i = 0;
				serviceID = (long) 0;

				while (st.hasMoreTokens()) {
					// get next token and store it in the array
					String currentData = st.nextToken();

					if (i == 0) {
						serviceID = Long.parseLong(currentData);
					} else if (0 < i && i < 8) {
						days[i] = (currentData.equals("0") ? false : true);
					} else if (i == 8) {
						java.util.Date date = format.parse(currentData);
						startDate = new java.sql.Date(date.getTime());
					} else if (i == 9) {
						java.util.Date date = format.parse(currentData);
						endDate = new java.sql.Date(date.getTime());
					}

					i++;
				}

				// insert the singleRow data into the appropriate table in DB

				pstmt.setLong(1, serviceID);
				pstmt.setBoolean(2, days[1]);
				pstmt.setBoolean(3, days[2]);
				pstmt.setBoolean(4, days[3]);
				pstmt.setBoolean(5, days[4]);
				pstmt.setBoolean(6, days[5]);
				pstmt.setBoolean(7, days[6]);
				pstmt.setBoolean(8, days[7]);
				pstmt.setDate(9, startDate);
				pstmt.setDate(10, endDate);
				pstmt.addBatch();

				totalLinesCount++;

				if (totalLinesCount % 1000 == 0) {
					System.out.println("*** Log *** Number of lines inserted: "
							+ totalLinesCount);
					pstmt.executeBatch();
					dbConnection.commit();
					pstmt.clearBatch();
					pstmt = dbConnection
							.prepareStatement("INSERT INTO "
									+ Tables.CALENDAR
									+ "(service_id,sunday,monday,tuesday,wednesday,thursday,friday,saturday,start_date,end_date) VALUES(?,?,?,?,?,?,?,?,?,?)");

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
