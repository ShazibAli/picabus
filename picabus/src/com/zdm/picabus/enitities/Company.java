package com.zdm.picabus.enitities;

public enum Company  {
	EGGED("אגד"),
	DAN("דן"),
	METROPOLIN("מטרופולין"),
	UNKNOWN("Unknown");
	
	private String companyName;
	
	
	private Company (String companyName) {
		this.companyName = companyName;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	@Override
	public String toString(){
		return companyName;
	}
	
	public static Company getCompanyByString (String companyName) {
		if (EGGED.getCompanyName().equalsIgnoreCase(companyName)) {
			return EGGED;
		} 
		else if (DAN.getCompanyName().equalsIgnoreCase(companyName)) {
			return DAN;
		}
		else if (METROPOLIN.getCompanyName().equalsIgnoreCase(companyName)) {
			return METROPOLIN;
		}
		else return UNKNOWN;
	}
}
