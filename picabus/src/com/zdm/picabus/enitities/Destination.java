package com.zdm.picabus.enitities;

public class Destination {

	private String directionA;
	private String directionB;
	
	/**
	 * 
	 */
	public Destination() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param directionA
	 * @param directionB
	 */
	public Destination(String directionA, String directionB)  {
		// TODO Auto-generated constructor stub
		this.directionA=directionA;
		this.directionB=directionB;
	}

	/**
	 * 
	 * @return direction A
	 */
	public String getDestinationA() {
		return directionA;
	}
	
	/**
	 * 
	 * @return direction B
	 */
	public String getDestinationB() {
		return directionB;
	}
}
