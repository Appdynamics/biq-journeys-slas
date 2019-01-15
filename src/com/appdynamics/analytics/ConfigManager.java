package com.appdynamics.analytics;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ConfigManager implements IConfig {

	String analyticsUrl;
	String globalKey;
	String accessKey;
	String queryActive;
	String queryAverage;
	
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
}
