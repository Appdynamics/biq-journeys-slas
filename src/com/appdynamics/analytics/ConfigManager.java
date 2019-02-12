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
	String queryBaseline;
	String schemaName;
	String appId;
	String restUser;
	String restPassword;
	String controllerUrl;
	String customEventName;
	HashMap<Long,Long> slas;
	long delay;
	String mode;
	String dateFormat;
	long baselineDays;
	long activePeriodMinutes;
	
	public void init(String file){
		try {
			JSONParser jsonParser = new JSONParser();
			FileReader reader = new FileReader(file);
			JSONObject obj = (JSONObject) jsonParser.parse(reader);
			analyticsUrl = (String)obj.get("analyticsUrl");
			globalKey = (String)obj.get("globalKey");
			accessKey = (String)obj.get("accessKey");
			queryActive = (String) obj.get("queryActive");
			queryBaseline = (String)obj.get("queryBaseline");
			schemaName = (String)obj.get("schemaName");
			controllerUrl = (String)obj.get("controllerUrl");
			restUser = (String)obj.get("restUser");
			restPassword = (String)obj.get("restPassword");
			appId = (String)obj.get("appId");
			customEventName = (String)obj.get("customEventName");
			
			JSONArray slaArray = (JSONArray)obj.get("sla"); 
			slas = new HashMap<Long,Long>();
			int size = slaArray.size();
			for(int i=0; i<size; i++) {
				JSONObject slaObj = (JSONObject) slaArray.get(i);
				slas.put(new Long(slaObj.get("range-min").toString()), new Long(slaObj.get("stdev").toString()));
				
			}
			delay = (Long)obj.get("delay");
			mode = (String)obj.get("mode");
			dateFormat = (String)obj.get("dateFormat");
			baselineDays = (Long)obj.get("baselineDays");
			activePeriodMinutes = (Long)obj.get("activePeriodMinutes");
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
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

	public String getQueryBaseline(){
		return queryBaseline;
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
	
	public void setSLAs(HashMap<Long,Long> _slas){
		slas = _slas;
	}
	
	public long getDelay() {
		return delay;
	}

	public boolean createSchema() {
		return mode != null && mode.toLowerCase().equals("create-schema");
	}

	public boolean deleteSchema() {
		return mode != null && mode.toLowerCase().equals("delete-schema");
	}
	
	public boolean isVerifyMode() {
		return mode != null && mode.toLowerCase().equals("verify");
	}

	public boolean isTestMode() {
		return mode != null && mode.toLowerCase().equals("test");
	}
	
	public boolean isRunMode() {
		return mode != null && mode.toLowerCase().equals("run");
	}
	
	public String getDateFormat() {
		return dateFormat;
	}
	
	public long getBaselineDays() {
		return baselineDays;
	}

	public long getActivePeriodMinutes() {
		return activePeriodMinutes;
	}
	
}
