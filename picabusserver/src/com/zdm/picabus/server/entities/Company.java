package com.zdm.picabus.server.entities;

public enum Company {
	EGGED("אגד"),
	EGGED_TAAVURA("אגד תעבורה"),
	DAN("דן"),
	SHEEM("שאמ"),
	NESIUT_AND_TAYARUT("נסיעות ותיירות"),
	G_B_TOURS("גי.בי.טורס"),
	OMNI_EXP("אומני אקספרס "),
	ILLIT("עילית"),
	NATEEV_EXPRESS("נתיב אקספרס"),
	METROPOLIN("מטרופולין"),
	SUPERBUS("סופרבוס"),
	CONNEX("קונקס"),
	KAVIM("קווים"),
	METRO_DAN("מטרודן"),
	GALIM("גלים"),
	MOAZA_EZ_GOLAN("מועצה אזורית גולן"),
	AFIKIM("אפיקים"),
	DAN_NORTH("דן בצפון"),
	EAST_JERUSALEM("מזרח ירושלים"),
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
