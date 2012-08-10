/**
 * 
 */
package com.zdm.picabus.server.datastore;

import java.util.Collection;

import com.zdm.picabus.server.entities.Line;

/**
 * @author user
 *
 */
public class DataStoreHandler implements IDataStoreHandler {

	/* (non-Javadoc)
	 * @see com.zdm.picabus.server.datastore.IDataStoreServices#getDepartueTime(int, double, double, java.lang.String)
	 */
	@Override
	public Line getDepartueTime(int lineNumber, double latitude,
			double longitude, String clientTimeString) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.zdm.picabus.server.datastore.IDataStoreServices#getRouteDetails()
	 */
	@Override
	public Collection<Line> getRouteDetails() {
		// TODO Auto-generated method stub
		return null;
	}

}
