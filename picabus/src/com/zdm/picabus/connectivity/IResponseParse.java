package com.zdm.picabus.connectivity;

import java.util.Collection;

import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.Stop;

public interface IResponseParse {

	public Line parseGetDepJsonResponse(String responseJsonString);
	
	public Collection<Stop> parseGetRouteJsonResponse(String responseJsonString);
	
}
