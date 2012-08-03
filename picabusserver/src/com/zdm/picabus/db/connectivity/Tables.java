package com.zdm.picabus.db.connectivity;

public enum Tables {
	
	AGENCY("agency"),
	CALENDAR("calendar"),
	ROUTES("routes"),
	SHAPES("shapes"),
	STOPTIMES("stop_times"),
	STOPS("stops"),
	TRIPS("trips");
	
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