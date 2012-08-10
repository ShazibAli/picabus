package com.zdm.picabus.server;

import com.google.gson.JsonObject;
import com.zdm.picabus.server.datastore.DataStoreHandler;
import com.zdm.picabus.server.datastore.IDataStoreHandler;
import com.zdm.picabus.server.entities.Line;

public class RequestHandler {

	public JsonObject getDepartueTime(int lineNumber, double lat, double lng,
			String clientTimeString) {

		IDataStoreHandler idss = new DataStoreHandler();
		Line requestedLData = idss.getDepartueTime(lineNumber, lat, lng, clientTimeString); 
		// TODO: handle return value
		
		
		return null;
	}

}
