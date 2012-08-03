package com.zdm.picabus.db.parser;


public interface IFileParser {
	
	
	/**
	 * 
	 * Parses and inserts the specific file data into the DB 
	 * 
	 * @return <code>String</code> containing all the errors occurred during parse process of this file
	 */
	public String parseAndUpdateDB();
}
