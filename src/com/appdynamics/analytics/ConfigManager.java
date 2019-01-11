package com.appdynamics.analytics;

import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ConfigManager implements IConfig {

	String query;
	String analyticsUrl;
	String globalKey;
	String accessKey;
	
	public void init(String file){
		try {
			JSONParser jsonParser = new JSONParser();
			FileReader reader = new FileReader(file);
			JSONObject obj = (JSONObject) jsonParser.parse(reader);
			query = (String) obj.get("query");
			analyticsUrl = (String)obj.get("analyticsUrl");
			globalKey = (String)obj.get("globalKey");
			accessKey = (String)obj.get("accessKey");
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void sysInfo() {
		System.out.println(query);
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

}
