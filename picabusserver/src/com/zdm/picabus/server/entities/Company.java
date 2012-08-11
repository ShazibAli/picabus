package com.zdm.picabus.server.entities;

public enum Company {
	EGGED("Egged"),
	DAN("Dan"),
	Metropolin("METROPOLIN");
	
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
}
