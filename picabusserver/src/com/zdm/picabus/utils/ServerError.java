package com.zdm.picabus.utils;

/**
 * 
 * Different server/db errors description
 *
 */
public enum ServerError {
	UNSUPPOTED_TASK_ERROR_MSG("Supplied Task-name is not supported by Picabus server"),
	UNSUPPOTED_PAYLOAD_TYPE_ERROR_MSG("Supplied payload data type is not supported by Picabus server"),
	UNSUPPOTED_JSON_PARAMS_ERROR_MSG("Payload data does not match the API for this request"),
	DB_CONNECTION_ISSUES_ERROR_MSG("An error has occured while trying to connect the DB"),
	MISSING_TASK_NAME_HEADER_ERROR_MSG("Task-name header is missing. Please refer to Picabus Server API");
	
	private String errorDescription;
	
	private ServerError (String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	@Override
	public String toString(){
		return errorDescription;
	}
}
