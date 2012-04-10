package com.zdm.picabus.db.framework;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * DB ini file configurations parser.
 * 
 * @author Daniel Lereya
 * 
 */
public class IniConfigs {

	private String hostName;
	private String portNumber;
	private String schemaName;
	private String username;
	private String password;
	private Integer maxNumConnections;
	private Integer initNumConnections;

	private final String iniFilePath;

	private FileInputStream fis = null;
	private DataInputStream dis = null;
	private BufferedReader file = null;

	/**
	 * Constructor
	 * 
	 * @param iniFilePath
	 *            full path to the 'config.ini' file.
	 * 
	 */
	public IniConfigs(String iniFilePath) {
		this.iniFilePath = iniFilePath;
	}

	/**
	 * Parse the 'config.ini' file.
	 * 
	 * after this method is done, all the relevant configurations is accessible
	 * through the 'IniConfigs' getters.
	 * 
	 * @throws FileNotFoundException
	 *             'config.ini' file couldn't be found.
	 * @throws IllegalDBConfigurationsException
	 *             one or more configurations in the 'config.ini' file is
	 *             invalid.
	 * @throws IOException
	 *             problem occurred while reading the file.
	 * */
	public void parseIniFile() throws FileNotFoundException,
			IllegalDBConfigurationsException, IOException {
		fis = new FileInputStream(iniFilePath);
		dis = new DataInputStream(fis);
		file = new BufferedReader(new InputStreamReader(dis));

		String line;
		int separator;
		String configName;
		String configValue;

		this.hostName = null;
		this.initNumConnections = -1;
		this.maxNumConnections = -1;
		this.password = null;
		this.portNumber = null;
		this.schemaName = null;
		this.username = null;

		ConfigurationErrors ce = new ConfigurationErrors();

		try {
			while ((line = file.readLine()) != null) {
				separator = line.indexOf('=');
				configName = line.substring(0, separator).trim();
				configValue = line.substring(separator + 1).trim();

				if (configName.equalsIgnoreCase("hostname")) {
					this.hostName = configValue;
				} else if (configName.equalsIgnoreCase("portnumber")) {
					this.portNumber = configValue;
				} else if (configName.equalsIgnoreCase("schemaname")) {
					this.schemaName = configValue;
				} else if (configName.equalsIgnoreCase("username")) {
					this.username = configValue;
				} else if (configName.equalsIgnoreCase("password")) {
					this.password = configValue;
				} else if (configName.equalsIgnoreCase("maxnumconnections")) {
					this.maxNumConnections = Integer.valueOf(configValue);
				} else if (configName.equalsIgnoreCase("initnumconnections")) {
					this.initNumConnections = Integer.valueOf(configValue);
				} else
					continue; // ignoring irrelevant configurations names

			}
		} catch (NumberFormatException e) {
			// here, we're just printing the stack trace. later, an 'IllegalDBConfigurationsException' exception will be thrown
			e.printStackTrace();
		}

		if (this.hostName == null) {
			ce.addEror("hostName", null);
		}
		if (this.initNumConnections <= 1) {
			ce.addEror("initNumConnections", this.initNumConnections.toString());
		}
		if (this.maxNumConnections <= 1) {
			ce.addEror("maxNumConnections", this.maxNumConnections.toString());
		}
		if (this.password == null) {
			ce.addEror("password", null);
		}
		if (this.portNumber == null) {
			ce.addEror("protNumber", null);
		}
		if (this.schemaName == null) {
			ce.addEror("schemaName", null);
		}
		if (this.username == null) {
			ce.addEror("username", null);
		}

		if (ce.isErrorOccured()) {
			throw new IllegalDBConfigurationsException(
					"The 'config.ini' file is invalid", ce.getErrorsString());
		}
	}


	public String getHostName() {
		return hostName;
	}

	public String getPortNumber() {
		return portNumber;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getMaxNumConnections() {
		return maxNumConnections;
	}

	public int getInitNumConnections() {
		return initNumConnections;
	}

	/**
	 * 
	 * @author Daniel Lereya
	 *
	 * Used to generate a descriptive String containing the errors that occurred during 'config.ini' parsing.
	 * 
	 */
	private class ConfigurationErrors {
		StringBuilder sb = null;
		boolean confErrOccured;

		public ConfigurationErrors() {
			this.confErrOccured = false;
		}

		public void addEror(String configName, String configValue) {
			String configStrValue = (configValue == null ? "null" : configValue);

			if (sb == null) {
				sb = new StringBuilder();
			}

			if (sb.length() == 0) {
				sb.append(configName + ": " + configStrValue);
			} else {
				sb.append(", " + configName + ": " + configStrValue);
			}

			this.confErrOccured = true;
		}

		public String getErrorsString() {
			return sb.toString();
		}

		public boolean isErrorOccured() {
			return this.confErrOccured;
		}

	}

}
