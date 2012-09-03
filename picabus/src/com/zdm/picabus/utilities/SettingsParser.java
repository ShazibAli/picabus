package com.zdm.picabus.utilities;

/**
 * 
 * Class used to parse data that was entered on setting page
 *
 */
public class SettingsParser {

	/**
	 * 
	 * @param str - format int "minutes" / int "hours" / int "day" 
	 * @return int - number of minutes in the string
	 */
	public static int ParseTimeInMinutes(String str){
		
		int retValue;
		String delims = " ";
		String[] tokens = str.split(delims);
		retValue = Integer.parseInt(tokens[0]);
		
		if (str.contains("hour")){
			retValue=retValue*60;
		}
		else if (str.contains("day")){
			retValue=retValue*60*24;
		}
		return retValue;
	}
	
}
