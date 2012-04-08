/**
 * 
 */
package com.zdm.picabus.db.utils;

/**
 * @author user
 *
 */
public enum Tables {
	
	DEPARTURES("departures");
	
	private String tableName;
	
	private Tables (String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	@Override
	public String toString(){
		return tableName;
	}
}
