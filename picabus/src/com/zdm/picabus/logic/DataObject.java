package com.zdm.picabus.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int tripCount;
	String stopHeadsign;
	Boolean bidirectional;
	List<TripObject> tripsList;

	public class TripObject {
		int direction;
		double id;
		String destinationA;
		String destinationB;
		int lineNumber;
		String eta;
		String companyName;
		int stopID;
		int stopSequence;
		double serviceID;
		double routeID;
	}

	public DataObject(int tripCount) {

		// TODO Auto-generated constructor stub
		tripsList = new ArrayList<DataObject.TripObject>(tripCount + 1);
	}
}
