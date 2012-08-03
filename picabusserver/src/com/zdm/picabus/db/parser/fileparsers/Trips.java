package com.zdm.picabus.db.parser.fileparsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;

import com.zdm.picabus.db.connectivity.Tables;
import com.zdm.picabus.db.parser.IFileParser;

public class Trips implements IFileParser {

	private String gtfsFilePath;
	private Connection dbConnection;
	
	public Trips(Connection dbConnection, String gtfsFilePath) {
		this.gtfsFilePath = gtfsFilePath;
		this.dbConnection = dbConnection;
	}
	
	@Override
	public String parseAndUpdateDB() {
		
		File file = new File(gtfsFilePath); 
		String line = null;
		BufferedReader br = null;
		Long[] singleRow = new Long[5];
		
		try {
			br  = new BufferedReader(new FileReader(file));
			// move over the headline
			br.readLine();
			int totalLinesCount = 1;
			
			// we want to batch 1000 inserts each time
			dbConnection.setAutoCommit(false);
			PreparedStatement pstmt;
			pstmt = dbConnection.prepareStatement("INSERT INTO " + Tables.TRIPS + "(route_id,service_id,trip_id,direction_id,shape_id) VALUES(?,?,?,?,?)");
			
			while((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line,",");
				int i = 0;
				boolean skip = false;
				while (st.hasMoreTokens()) {
					//get next token and store it in the array
					String currentData = st.nextToken();
					
					if (currentData != null && !currentData.isEmpty()) {
						if (i==2 && currentData.contains("#")) {
							
							currentData = (currentData.split("#"))[0] + (currentData.split("#"))[1];
							singleRow[i] = Long.parseLong(currentData);
						} 
						else if (currentData.matches("[0-9]+")) {
							singleRow[i] = Long.parseLong(currentData);
						}
						else {
							skip = true;
							continue;
						}
						
					}
					
					else singleRow[i] = Long.parseLong(currentData);
					i++;
				}

				
				if (skip) {
					skip = false;
					continue;
				}
				
				 
				
				pstmt.setLong(1, singleRow[0]);
				pstmt.setLong(2, singleRow[1]);
				pstmt.setLong(3, singleRow[2]);
				pstmt.setLong(4, singleRow[3]);
				pstmt.setLong(5, singleRow[4]);
				pstmt.addBatch();
				
				totalLinesCount++;
				
				if (totalLinesCount % 1000 == 0) {
					System.out.println("*** Log *** Number of lines inserted: " + totalLinesCount);
					pstmt.executeBatch();
					dbConnection.commit();
					pstmt.clearBatch();
					pstmt = dbConnection.prepareStatement("INSERT INTO " + Tables.TRIPS + "(route_id,service_id,trip_id,direction_id,shape_id) VALUES(?,?,?,?,?)");
					
				}
				
			}
			
			pstmt.executeBatch();
			dbConnection.commit();
			pstmt.close();
			//close the file
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return null;
	}

}
