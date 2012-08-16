package com.zdm.picabus.utilities;

import com.zdm.picabus.enitities.Destination;

public class DestionationParser {

	public static Destination parseDestination(String dest){
		
		String delims = "<->+";
		String[] tokens = dest.split(delims);
		if (tokens.length==2){
				return new Destination(tokens[0],tokens[1]);
		}
		else if(tokens.length==1){
			return new Destination(tokens[0],null);
		}
		else{
			return new Destination();
		}
	}
}
