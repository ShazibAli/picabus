package com.zdm.picabus.utils;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

public class XMLBuilderTest {

	/**
	 * @param args
	 * @throws TransformerException
	 */
	public static void main(String[] args) {
		
		// XML response generation example
		Map<String, String> dataNodes = new HashMap<String, String>();
	
		dataNodes.put("StationAddress", "Rashi 67, Tel Aviv");
		dataNodes.put("Longitude", "33.34567");
		dataNodes.put("Latitude", "45.69234");
		dataNodes.put("BusNumber", "25");
		dataNodes.put("Company", "Egged");
		dataNodes.put("ArrivalTime", "16:59");
		dataNodes.put("HebrewValue", "דניאל מורן ועמרי");
		String xmlString = XMLBuilder.createXmlDoc("path", "recommended path for the user", dataNodes);;
		
		System.out.println("Here's the xml:\n\n" + xmlString);

	}

}
