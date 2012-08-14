package com.zdm.picabus.localdatastorage;

public class UserPreferences {
	
	private int timeInterval;
	private boolean enableNotifications;
	private int notificationDelta;
	
	/**
	 * @param timeInterval
	 * @param enableNotifications
	 * @param notificationDelta
	 */
	public UserPreferences(int timeInterval, boolean enableNotifications,
			int notificationDelta) {
		super();
		this.timeInterval = timeInterval;
		this.enableNotifications = enableNotifications;
		this.notificationDelta = notificationDelta;
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (enableNotifications ? 1231 : 1237);
		result = prime * result + notificationDelta;
		result = prime * result + timeInterval;
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserPreferences other = (UserPreferences) obj;
		if (enableNotifications != other.enableNotifications)
			return false;
		if (notificationDelta != other.notificationDelta)
			return false;
		if (timeInterval != other.timeInterval)
			return false;
		return true;
	}



	/**
	 * @return the timeInterval
	 */
	public int getTimeInterval() {
		return timeInterval;
	}

	/**
	 * @param timeInterval the timeInterval to set
	 */
	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}

	/**
	 * @return the enableNotifications
	 */
	public boolean isEnableNotifications() {
		return enableNotifications;
	}

	/**
	 * @param enableNotifications the enableNotifications to set
	 */
	public void setEnableNotifications(boolean enableNotifications) {
		this.enableNotifications = enableNotifications;
	}

	/**
	 * @return the notificationDelta
	 */
	public int getNotificationDelta() {
		return notificationDelta;
	}

	/**
	 * @param notificationDelta the notificationDelta to set
	 */
	public void setNotificationDelta(int notificationDelta) {
		this.notificationDelta = notificationDelta;
	}
	
	
	
}
