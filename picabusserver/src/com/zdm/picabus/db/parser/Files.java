package com.zdm.picabus.db.parser;

public enum Files {
	AGENCY("agency.txt"),
	CALENDAR("calendar.txt"),
	ROUTES("routes.txt"),
	SHAPES("shapes.txt"),
	STOPTIMES("stop_times.txt"),
	STOPS("stops.txt"),
	TRIPS("trips.txt");
	
	private String fileName;
	
	private Files (String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	@Override
	public String toString(){
		return fileName;
	}
}
