package com.appdynamics.analytics;

import java.util.HashMap;

import com.appdynamics.analytics.rest.RestManager;
import com.appdynamics.analytics.sla.SLAManager;

public class SLAProcessor {

	public static void main(String[] args) throws Exception {
		ConfigManager mgr = new ConfigManager();
		mgr.init("config.json");
		RestManager restMgr = new RestManager();
		
		SLAProcessor processor = new SLAProcessor();
		processor.queryRecords(mgr,restMgr);
	}
	
	public void queryRecords(ConfigManager mgr, RestManager restManager) throws Exception{
		SLAManager slaManager = new SLAManager();
		slaManager.setConfigManager(mgr);
		slaManager.setRestManager(restManager);
		
		HashMap<String,String> averages = slaManager.getAverages();
		HashMap<String,String> activeRecords = slaManager.getActiveRecords();
		HashMap<String,String> failedSLAs = slaManager.verifySLAs(averages,activeRecords);
		restManager.postFailedSLAs(mgr,failedSLAs);
	}
}
