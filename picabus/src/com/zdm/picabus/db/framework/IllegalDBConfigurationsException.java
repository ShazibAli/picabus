/**
 * 
 */
package com.zdm.picabus.db.framework;

/**
 * An exception representing an illegal configuration in the DB 'config.ini' file.
 * 
 * @author Daniel Lereya
 *
 */
public class IllegalDBConfigurationsException extends Exception {

	private static final long serialVersionUID = 1L;
	private String configsName;
	
	public IllegalDBConfigurationsException(String msg) {
		super(msg);
	}
	
	public IllegalDBConfigurationsException(String msg, String configName) {
		super(msg);
		this.configsName = configName;
	}
	
	public String getConfigName() {
		return configsName;
	}
	
	@Override
	public String toString() {
		
		return "IllegalDBConfigurationsException -  "+ configsName + ". \n" + (getMessage() != null ? getMessage() : "");
	}
	
}
