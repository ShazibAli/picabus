package com.zdm.picabus.db.parser.fileparsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;

import com.zdm.picabus.db.connectivity.Tables;
import com.zdm.picabus.db.parser.IFileParser;

public class Routes implements IFileParser {

	private String gtfsFilePath;
	private Connection dbConnection;

	public Routes(Connection dbConnection, String gtfsFilePath) {
		this.gtfsFilePath = gtfsFilePath;
		this.dbConnection = dbConnection;
	}

	@Override
	public String parseAndUpdateDB() {

		File file = new File(gtfsFilePath);
		String line = null;
		BufferedReader br = null;

		Long routeID = (long) 0;
		Integer agencyID = 0;
		String routeShortName = null;
		String routeLongName = null;
		Integer routeType = null;

		try {

			// we want to batch 1000 inserts each time
			dbConnection.setAutoCommit(false);
			PreparedStatement pstmt;
			pstmt = dbConnection
					.prepareStatement("INSERT INTO "
							+ Tables.ROUTES
							+ "(route_id,agency_id,route_short_name,route_long_name,route_type) VALUES(?,?,?,?,?)");
			FileInputStream in = new FileInputStream(file);
			br  = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			int totalLinesCount = 1;
			
			// move over the headline
			br.readLine();

			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				int i = 0;

				while (st.hasMoreTokens()) {
					// get next token and store it in the array
					String currentData = st.nextToken();
					switch (i) {
					case 0:
						routeID = Long.parseLong(currentData);
						i++;
						break;
					case 1:
						agencyID = Integer.parseInt(currentData);
						i++;
						break;
					case 2:
						routeShortName = currentData;
						i++;
						break;
					case 3:
						routeLongName = currentData;
						i++;
						break;
					case 4:
						routeType = Integer.parseInt(currentData);
						i++;
						break;
					}
				}

				// insert the singleRow data into the appropriate table in DB

				pstmt.setLong(1, routeID);
				pstmt.setInt(2, agencyID);
				pstmt.setString(3, routeShortName);
				pstmt.setString(4, routeLongName);
				pstmt.setInt(5, routeType);
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
									+ Tables.ROUTES
									+ "(route_id,agency_id,route_short_name,route_long_name,route_type) VALUES(?,?,?,?,?)");

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
		}

		return null;
	}

}
