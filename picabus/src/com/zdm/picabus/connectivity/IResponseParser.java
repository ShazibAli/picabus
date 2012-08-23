package com.zdm.picabus.connectivity;

import java.util.Collection;

import org.json.JSONObject;

import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.Stop;

public interface IResponseParser {

	public Line parseGetDepJsonResponse(JSONObject responseJson);
	
	public Collection<Stop> parseGetRouteJsonResponse(JSONObject responseJson);

	public long parseGetScoreResponse(JSONObject json);
	
}
