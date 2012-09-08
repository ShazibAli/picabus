package com.zdm.picabus.utils;

import java.util.Calendar;

/**
 * 
 * Provides simple utilities for working with dates  
 *
 */
public class DateUtils {
	public static String getDayString(int dayCode) {
		if (dayCode == java.util.Calendar.SUNDAY) {
			return "sunday";
		}
		else if (dayCode == java.util.Calendar.MONDAY) {
			return "monday";
		}
		else if (dayCode == java.util.Calendar.TUESDAY) {
			return "tuesday";
		}
		else if (dayCode == java.util.Calendar.WEDNESDAY) {
			return "wednesday";
		}
		else if (dayCode == java.util.Calendar.THURSDAY) {
			return "thursday";
		}
		else if (dayCode == java.util.Calendar.FRIDAY) {
			return "friday";
		}
		else if (dayCode == java.util.Calendar.SATURDAY) {
			return "saturday";
		}
		return null;
	}
	
	public static int getCurrentDayCode() {
		Calendar c = Calendar.getInstance();
		c.setTime(c.getTime());
		return c.get(Calendar.DAY_OF_WEEK); 
	}

	public static String getTodayString() {
		return getDayString(getCurrentDayCode());
	}

}
