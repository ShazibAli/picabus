package com.zdm.picabus.locationservices;

public class GpsResult {

	private Double lat;
	private Double lng;

	public GpsResult(Double lat, Double lng) {
		this.lat = lat;
		this.lng = lng;

	}

	public Double getLng() {
		return lng;
	}

	public Double getLat() {
		return lat;
	}

}
