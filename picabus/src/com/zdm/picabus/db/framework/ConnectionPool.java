package com.zdm.picabus.db.framework;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;

public class ConnectionPool {
 

	private static  String PASSWORD;
	private static  String USERNAME;
	private static  String SCHEMA;
	private static  String PORT;
	private static  String HOSTNAME;
	private static  int MAXCONNECTIONS;
	private static  int INITNUMOFCONNECTION;


	
	private static Boolean isInitialized = false;
	
	private static int connectionCount = 0;
	private static int maxNumOfConnections = 0;
	private static HashSet<Connection> ready = new HashSet<Connection>();
	private static HashSet<Connection> used = new HashSet<Connection>();

	
	public static boolean startConnectionPool (int initNumOfConnections, int maxNumOfConnections, String hostAddress, String port,
			String schemaName, String username, String password) {
		ConnectionPool.maxNumOfConnections = maxNumOfConnections;
		connectionCount = 0;
		for (int i = 0; i < initNumOfConnections; i++) { 
			Connection c = createConnection(hostAddress, port,
					schemaName, username, password);
			if (c == null) {
				//TODO: show error message to the client
				return false;
			}
			ready.add(c);
			connectionCount++;
		}
		return true;
	}

	/**
	 * Provides new connection. 
	 * @param   none
	 * @return  On Success: New Connection. 
	 * 			On Failure: null.
	 *  
	 */
	public static Connection getConnection() {
		
		if (!isInitialized) {
			IniConfigs ini = new IniConfigs("config.ini"); // the ini should be located in the root directory of the project
			PASSWORD =  ini.getPassword();
			USERNAME = ini.getUsername();
			SCHEMA = ini.getSchemaName();
			PORT = ini.getPortNumber();
			HOSTNAME = ini.getHostName();
			MAXCONNECTIONS=ini.getMaxNumConnections();
			INITNUMOFCONNECTION = ini.getInitNumConnections();

		Boolean res = startConnectionPool(INITNUMOFCONNECTION, MAXCONNECTIONS, HOSTNAME, PORT, SCHEMA,
				     USERNAME, PASSWORD);
			if (res == false) {
				return null;
			}
			isInitialized = true;
		}
		Connection c = null;
		synchronized (ready) {
			while (ready.size() == 0) {
				if (connectionCount < maxNumOfConnections) {
					c = createConnection(HOSTNAME, PORT, SCHEMA,
							USERNAME, PASSWORD);
					if (c == null) { // Connection creation failed
						return null;
					}
					connectionCount++;
					used.add(c);
					return c;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			c = (Connection) ready.toArray()[0];
			ready.remove(c);
			used.add(c);
		
		}
		return c;
	};

	
	/**
	 * Free a used connection. 
	 * @param   Connection
	 * @return  void
	 */
	public static void freeConnection(Connection c) {
		if (used.contains(c) == true) {
			connectionCount--;
			ready.add(c);
			used.remove(c);
		}
	};

	
	/**
	 * Creates new connection. 
	 * @param   none
	 * @return  On Success: New Connection. 
	 * 			On Failure: null.
	 *  
	 */
	private static Connection createConnection(String hostAddress, String port,
			String schemaName, String username, String password) {
		Connection connection = null;
		// loading the driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to load the MySQL JDBC driver - " + e.toString());
			return null;
		}
		System.out.println("Driver loaded successfully");
		// creating the connection
		System.out.print("Trying to connect.. ");
		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://" + hostAddress + ":" + port + "/" + schemaName, username, password);
		} catch (SQLException e) {
			System.out.println("Unable to connect - " + e.toString());
			return null;
		}
		System.out.println("Connected!");
		
		return connection;
	}
}
