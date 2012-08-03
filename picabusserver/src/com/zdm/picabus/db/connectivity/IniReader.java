package com.zdm.picabus.db.connectivity;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class IniReader {

	private String hostName;
	private String portNumber;
	private String schemaName;
	private String username;
	private String password;
	private int maxNumConnections;
	private int initNumConnections;
	
	public IniReader(String iniFile){
	
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(iniFile);
			DataInputStream dis = new DataInputStream(fis);
			
			BufferedReader file = new BufferedReader(new InputStreamReader(dis));
			
			String line;
			int separator;
			String variable;
			String value;
			IniConfigs config;
			
			this.hostName = null;
			this.initNumConnections = -1;
			this.maxNumConnections = -1;
			this.password = null;
			this.portNumber = null;
			this.schemaName = null;
			this.username = null;
			
			
			while ((line= file.readLine()) != null){
				separator = line.indexOf('=');
				variable = line.substring(0, separator).trim();
				config = IniConfigs.getConfigFromString(variable);
				value = line.substring(separator + 1).trim();
				
				switch(config){
					case HOSTNAME:
					{
						this.hostName = value;
						break;
					}
					case PORT_NUMBER:
					{
						this.portNumber = value;
						break;
					}					
					case SCHEMA_NAME:
					{
						this.schemaName = value;
						break;
					}
					case USERNAME:
					{
						this.username = value;
						break;
					}
					case PASSWORD:
					{
						this.password = value;
						break;
					}
					case MAX_NUM_OF_CONNECTIONS:
					{
						this.maxNumConnections = Integer.valueOf(value);
						break;
					}
					case INIT_NUM_OF_CONNECTIONS:
					{
						this.initNumConnections = Integer.valueOf(value);
						break;
					}
					case UNSUPPORTED_CONFIG:
					{
						continue;
					}
				}
			}
			
			dis.close();
			fis.close();
			file.close();
			
			if (this.hostName == null){
				throw new Exception("Ini file host name error"); 
			}
			if(this.initNumConnections == -1){
				throw new Exception("Ini file initial number of connection error");
			}
			if(this.maxNumConnections == -1){
				throw new Exception("Ini file maxinum number of connection error");
			}
			if(this.password == null){
				throw new Exception("Ini file passwod error");
			}
			if (this.portNumber == null){
				throw new Exception("Ini file port number error");
			}
			if(this.schemaName == null){
				throw new Exception("Ini file scheme name error");
			}
			if(this.username == null){
				throw new Exception("Ini file username error");
			}
			
		} catch (Exception e) {
		
			System.out.println(e.toString());
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
	
	
	
}
