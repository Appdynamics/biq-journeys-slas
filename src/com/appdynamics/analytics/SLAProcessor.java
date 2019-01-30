package com.appdynamics.analytics;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.appdynamics.analytics.rest.RestManager;
import com.appdynamics.analytics.sla.ActiveRecord;
import com.appdynamics.analytics.sla.SLAManager;

public class SLAProcessor extends TimerTask {
	
	private final static Logger LOGGER = Logger.getLogger(SLAProcessor.class.getName());
	private ConfigManager configMgr;
	private RestManager restMgr;
	
	

	public static void main(String[] args) throws Exception {
		
		LOGGER.log(Level.INFO,"Version : 1.0.1");
		
		LOGGER.log(Level.INFO,"Init Configuration Manager ...");
		
		ConfigManager configMgr = new ConfigManager();
		configMgr.init("config.json");
		
		LOGGER.log(Level.INFO,"Init Rest Manager ...");
		RestManager restMgr = new RestManager();
		
		LOGGER.log(Level.INFO,"Init SLA Process ...");
		SLAProcessor processor = new SLAProcessor();
		processor.setConfigManager(configMgr);
		processor.setRestManager(restMgr);
		
		if(configMgr.createSchema()) {
			try {
				LOGGER.log(Level.INFO,"Creating Schama ...");
				String result = restMgr.createSchema(configMgr);
				LOGGER.log(Level.INFO,"Creating Schama = "+result);				
			}catch(Exception e) {
				LOGGER.log(Level.SEVERE,"Error Creating Schema : "+e.getMessage(),e);
			}
		}
		
		
		LOGGER.log(Level.INFO,"Init Timer ... Process will start in 1 min and then sleep for :"+configMgr.getDelay()+" milliseconds ");
		Timer timer = new Timer();
		timer.schedule(processor, 60000, configMgr.getDelay());
		
	}
	
	private void setRestManager(RestManager mgr) {
		restMgr = mgr;
	}

	private void setConfigManager(ConfigManager mgr) {
		configMgr = mgr;
	}

	@Override
	public void run() {
		
		LOGGER.log(Level.INFO,"running SLAs ...");
		SLAManager slaManager = new SLAManager();
		slaManager.setConfigManager(configMgr);
		slaManager.setRestManager(restMgr);
		
		try {
			LOGGER.log(Level.INFO,"getAverages ...");
			HashMap<String,Long> averages = slaManager.getAverages();
			LOGGER.log(Level.INFO,"getAverages : Found :"+averages.size());
			LOGGER.log(Level.INFO,"getActiveRecords ...");
			HashMap<String,ActiveRecord> activeRecords = slaManager.getActiveRecords();
			LOGGER.log(Level.INFO,"getActiveRecords : Found :"+activeRecords.size());
			LOGGER.log(Level.INFO,"verifySLAs ...");
			List<ActiveRecord> failedSLAs = slaManager.verifySLAs(averages,activeRecords);
			LOGGER.log(Level.INFO,"vailedSLAs : Found :"+failedSLAs.size());
			restMgr.postFailedSLAs(configMgr,failedSLAs);			
			LOGGER.log(Level.INFO,"vailedSLAs : Posted :"+failedSLAs.size());
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE,"Error running scheduled task",e);
		}
		
	}
}
