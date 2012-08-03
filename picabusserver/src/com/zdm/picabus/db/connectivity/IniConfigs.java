package com.zdm.picabus.db.connectivity;



public enum IniConfigs {
	HOSTNAME("hostname"),
	PORT_NUMBER("portnumber"),
	SCHEMA_NAME("schemaname"),
	USERNAME("username"),
	PASSWORD("password"),
	MAX_NUM_OF_CONNECTIONS("maxnumconnections"),
	INIT_NUM_OF_CONNECTIONS("initnumconnections"),
	UNSUPPORTED_CONFIG("unsoppertedconfig");
	
	private String configLabel;
	
	private IniConfigs (String configLabel) {
		this.configLabel = configLabel;
	}
	
	public String getConfigLabel() {
		return configLabel;
	}
	
	public static IniConfigs getConfigFromString(String configName) {
		for (IniConfigs config : IniConfigs.values()) {
			if (configName.equalsIgnoreCase(config.toString())){
				return config;
			}
		}
		return IniConfigs.UNSUPPORTED_CONFIG;
		
	}
	
	@Override
	public String toString(){
		return configLabel;
	}
}
