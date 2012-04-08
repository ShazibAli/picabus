package com.zdm.picabus.db.utils;

public enum Views {
	BUSES("buses"); //TODO: change to relevant view
	
	private String viewName;
	
	private Views (String viewName) {
		this.viewName = viewName;
	}
	
	public String getViewName() {
		return viewName;
	}
	
	@Override
	public String toString(){
		return viewName;
	}
}
