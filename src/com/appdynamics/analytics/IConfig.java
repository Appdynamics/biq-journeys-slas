package com.appdynamics.analytics;

public interface IConfig {

	String getAnalyticsUrl();

	String getAccount();

	String getAccessKey();

	String getSchemaName();
	
	String getApplicationId();

	String getRestUser();

	String getRestPassword();

	String getConrollerUrl();

	String getCustomEventName();

	long getDelay();

	boolean createSchema();

}
