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

public class Agency implements IFileParser {

	private String gtfsFilePath;
	private Connection dbConnection;

	public Agency(Connection dbConnection, String gtfsFilePath) {
		this.gtfsFilePath = gtfsFilePath;
		this.dbConnection = dbConnection;
	}

	@Override
	public String parseAndUpdateDB() {

		File file = new File(gtfsFilePath);
	
		
		String line = null;
		BufferedReader br = null;
		String[] singleRow = new String[5];

		try {
			PreparedStatement pstmt;
			pstmt = dbConnection
					.prepareStatement("INSERT INTO "
							+ Tables.AGENCY
							+ "(agency_id,agency_name,agency_url,agency_timezone,agency_lang) VALUES(?,?,?,?,?)");
			FileInputStream in = new FileInputStream(file);
			br  = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			// move over the headline
			br.readLine();

			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				int i = 0;
				int agencyID = 0;

				while (st.hasMoreTokens()) {
					// get next token and store it in the array
					String currentData = st.nextToken();

					if (i == 0) {
						agencyID = Integer.parseInt(currentData);
					} else {
						singleRow[i] = currentData;
					}

					i++;
				}

				// insert the singleRow data into the appropriate table in DB

				pstmt.setInt(1, agencyID);
				pstmt.setString(2, singleRow[1]);
				pstmt.setString(3, singleRow[2]);
				pstmt.setString(4, singleRow[3]);
				pstmt.setString(5, singleRow[4]);

				pstmt.executeUpdate();
			}

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
