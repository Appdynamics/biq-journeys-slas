package com.appdynamics.analytics;

import java.io.FileReader;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ConfigManager implements IConfig {

	String analyticsUrl;
	String globalKey;
	String accessKey;
	String queryActive;
	String queryAverage;
	String schemaName;
	String appId;
	String restUser;
	String restPassword;
	String controllerUrl;
	String customEventName;
	HashMap<Long,Long> slas;
	long delay;
	String schema;
	
	public void init(String file){
		try {
			JSONParser jsonParser = new JSONParser();
			FileReader reader = new FileReader(file);
			JSONObject obj = (JSONObject) jsonParser.parse(reader);
			analyticsUrl = (String)obj.get("analyticsUrl");
			globalKey = (String)obj.get("globalKey");
			accessKey = (String)obj.get("accessKey");
			queryActive = (String) obj.get("queryActive");
			queryAverage = (String)obj.get("queryAverage");
			schemaName = (String)obj.get("schemaName");
			controllerUrl = (String)obj.get("controllerUrl");
			restUser = (String)obj.get("restUser");
			restPassword = (String)obj.get("restPassword");
			appId = (String)obj.get("appId");
			customEventName = (String)obj.get("customEventName");
			schema = (String)obj.get("schema");
			
			JSONArray slaArray = (JSONArray)obj.get("sla"); 
			slas = new HashMap<Long,Long>();
			int size = slaArray.size();
			for(int i=0; i<size; i++) {
				JSONObject slaObj = (JSONObject) slaArray.get(i);
				slas.put(new Long(slaObj.get("range-min").toString()), new Long(slaObj.get("percent").toString()));
				
			}
			delay = (Long)obj.get("delay");
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void sysInfo() {
		System.out.println("query :"+queryActive);
		System.out.println("queryAverage :"+queryAverage);
	}
	
	public String getAnalyticsUrl(){
		return analyticsUrl;
	}

	public String getAccount() {
		return globalKey;
	}

	public String getAccessKey() {
		return accessKey;
	}
	
	public String getQueryActive(){
		return queryActive;
	}

	public String getQueryAverage(){
		return queryAverage;
	}
	
	public String getSchemaName() {
		return schemaName;
	}

	public String getApplicationId() {
		return appId;
	}
	
	public String getRestUser() {
		return restUser;
	}

	public String getRestPassword() {
		return restPassword;
	}

	public String getConrollerUrl() {
		return controllerUrl;
	}

	public String getCustomEventName() {
		return customEventName;
	}

	public HashMap<Long, Long> getSLAs() {
		return slas;
	}

	public long getDelay() {
		return delay;
	}

	public boolean createSchema() {
		return schema != null && schema.toLowerCase().equals("create");
	}

	public boolean deleteSchema() {
		return schema != null && schema.toLowerCase().equals("delete");
	}

	
}
