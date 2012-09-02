package com.zdm.picabus.connectivity;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONObject;

import com.zdm.picabus.enitities.Line;
import com.zdm.picabus.enitities.RealtimeLocationReport;
import com.zdm.picabus.enitities.Report;
import com.zdm.picabus.enitities.ReportResult;
import com.zdm.picabus.enitities.Stop;

public interface IResponseParser {

	public Line parseGetDepJsonResponse(JSONObject responseJson);
	
	public Collection<Stop> parseGetRouteJsonResponse(JSONObject responseJson);

	public long parseGetScoreResponse(JSONObject json);
	
	public ReportResult parseReportResult(JSONObject json);

	public RealtimeLocationReport parseRealtimeLocationResponse(JSONObject json);

	public ArrayList<Report> parseTripReports(JSONObject json);
	
}
