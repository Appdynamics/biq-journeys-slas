package com.appdynamics.analytics;

import java.util.HashMap;

import org.json.simple.JSONArray;

import com.appdynamics.analytics.rest.RestManager;
import com.appdynamics.analytics.util.DateHelper;

public class SLAManager {

	ConfigManager configManager;
	RestManager restManager;
	
	public void setConfigManager(ConfigManager configManager) {
		this.configManager = configManager;
	}

	public void setRestManager(RestManager restManager) {
		this.restManager = restManager;
	}

	public HashMap<String,String> getAverages() throws Exception{
		HashMap<String,String> slas = new HashMap<String,String>();
		JSONArray jsonData = restManager.query(configManager, configManager.getQueryAverage(), DateHelper.getLastTwoWeeksSinceYesterday(), 15000);
		for (int i=0; i < jsonData.size(); i++) {
			JSONArray rec =  (JSONArray) jsonData.get(i);
			slas.put(rec.get(0).toString(),rec.get(i).toString());
		}
		return slas;
	}
	
	public HashMap<String,String> getActiveRecords() throws Exception{
		HashMap<String,String> slas = new HashMap<String,String>();
		JSONArray jsonData = restManager.query(configManager, configManager.getQueryActive(), DateHelper.getTimeRangeForNow(), 15000);
		for (int i=0; i < jsonData.size(); i++) {
			JSONArray rec =  (JSONArray) jsonData.get(i);
			slas.put(rec.get(0).toString(),rec.get(i).toString());
		}
		return slas;
	}

	public HashMap<String, String> verifySLAs(HashMap<String, String> averages,
			HashMap<String, String> activeRecords) {
		// TODO Auto-generated method stub
		return null;
	}
}
