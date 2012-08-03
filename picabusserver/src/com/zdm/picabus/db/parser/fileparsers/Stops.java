package com.zdm.picabus.db.parser.fileparsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;

import com.zdm.picabus.db.connectivity.Tables;
import com.zdm.picabus.db.parser.IFileParser;

public class Stops implements IFileParser {

	private String gtfsFilePath;
	private Connection dbConnection;
	
	public Stops(Connection dbConnection, String gtfsFilePath) {
		this.gtfsFilePath = gtfsFilePath;
		this.dbConnection = dbConnection;
	}
	
	@Override
	public String parseAndUpdateDB() {
		
		File file = new File(gtfsFilePath); 
		String line = null;
		BufferedReader br = null;
		Long stopID = null, stopCode = null;
		String stopName= null, stopDescripstion = null;
		Double stopLat = null, stopLon = null;
		
		
		try {
			FileInputStream in = new FileInputStream(file);
			br  = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			// move over the headline
			br.readLine();
			int totalLinesCount = 1;
			
			// we want to batch 1000 inserts each time
			dbConnection.setAutoCommit(false);
			PreparedStatement pstmt;
			pstmt = dbConnection.prepareStatement("INSERT INTO " + Tables.STOPS + "(stop_id,stop_code,stop_name,stop_desc,stop_lat,stop_lon) VALUES(?,?,?,?,?,?)");
			
			while((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line,",");
				int i = 0;
				while (st.hasMoreTokens()) {
					//get next token and store it in the array
					String currentData = st.nextToken();
					
					if (currentData != null && !currentData.isEmpty()) {
						switch (i) {
						case 0: 
							stopID = Long.parseLong(currentData);
							break;
						case 1:
							stopCode = Long.parseLong(currentData);
							break;
						case 2:
							stopName = currentData;
							break;
						case 3:
							stopDescripstion = currentData;
							break;
						case 4: 
							stopLat = Double.parseDouble(currentData);
							break;
						case 5: 
							stopLon = Double.parseDouble(currentData);
							break;
						}
					}
					i++;
				} 
				
				pstmt.setLong(1, stopID);
				pstmt.setLong(2, stopCode);
				pstmt.setString(3, stopName);
				pstmt.setString(4, stopDescripstion);
				pstmt.setDouble(5, stopLat);
				pstmt.setDouble(6, stopLon);
				pstmt.addBatch();
				
				totalLinesCount++;
				
				if (totalLinesCount % 1000 == 0) {
					System.out.println("*** Log *** Number of lines inserted: " + totalLinesCount);
					pstmt.executeBatch();
					dbConnection.commit();
					pstmt.clearBatch();
					pstmt = dbConnection.prepareStatement("INSERT INTO " + Tables.STOPS + "(stop_id,stop_code,stop_name,stop_desc,stop_lat,stop_lon) VALUES(?,?,?,?,?,?)");
					
				}	
			}
			// execute the modulo
			pstmt.executeBatch();
			dbConnection.commit();
			pstmt.close();
			// close the file
			br.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
	
		} catch (IOException e) {
			e.printStackTrace();
		
		} catch (BatchUpdateException  e) {
			try {
		        dbConnection.rollback();
		      } catch (Exception e2) {
		        e.printStackTrace();
		      }
			e.printStackTrace();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		} 
		
		return null;
	}

}
