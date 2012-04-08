package com.zdm.picabus.db.framework;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;



public class IniConfigs {

	private String hostName;
	private String portNumber;
	private String schemaName;
	private String username;
	private String password;
	private int maxNumConnections;
	private int initNumConnections;
	
	public IniConfigs(String iniFile){
	
		
		FileInputStream fis;
		try {
			fis = new FileInputStream(iniFile);
			DataInputStream dis = new DataInputStream(fis);
			
			BufferedReader file = new BufferedReader(new InputStreamReader(dis));
			
			String line;
			int separator;
			String variable;
			String value;
			
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
				value = line.substring(separator + 1).trim();
				
				switch(variable.toLowerCase()){
					case "hostname":{
						this.hostName = value;
						break;
					}
					case "portnumber":{
						this.portNumber = value;
						break;
					}					
					case "schemaname":{
						this.schemaName = value;
						break;
					}
					case "username":{
						this.username = value;
						break;
					}
					case "password":{
						this.password = value;
						break;
					}
					case "maxnumconnections":{
						this.maxNumConnections = Integer.valueOf(value);
						break;
					}
					case "initnumconnections":{
						this.initNumConnections = Integer.valueOf(value);
						break;
					}
					default:{
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
			//TODO: print error message to client layer
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
