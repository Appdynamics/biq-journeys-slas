package com.appdynamics.analytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.appdynamics.analytics.rest.RestManager;
import com.appdynamics.analytics.sla.ActiveRecord;
import com.appdynamics.analytics.sla.Baseline;
import com.appdynamics.analytics.sla.SLAManager;

public class SLAProcessor extends TimerTask {
	
	private final static Logger LOGGER = Logger.getLogger(SLAProcessor.class.getName());
	private ConfigManager configMgr;
	private RestManager restMgr;
	
	

	public static void main(String[] args) throws Exception {
		
		LOGGER.log(Level.INFO,"Version : 1.0.11");
		
		LOGGER.log(Level.INFO,"Init Configuration Manager ...");
		
		ConfigManager configMgr = new ConfigManager();
		configMgr.init("config.json");
		
		RestManager restMgr = new RestManager();
		
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
			return;
		}
				
		
		LOGGER.log(Level.INFO,"Init Timer ... Process will start in 10 seconds and then sleep for :"+configMgr.getDelay()+" milliseconds ");
		Timer timer = new Timer();
		timer.schedule(processor, 10000, configMgr.getDelay());
		
	}
	
	private void setRestManager(RestManager mgr) {
		restMgr = mgr;
	}

	private void setConfigManager(ConfigManager mgr) {
		configMgr = mgr;
	}
	
	@Override
	public void run() {
		if(configMgr.isVerifyMode()) {
			verify();
		}
		
		if(configMgr.isTestMode()) {
			test();
		}
		
		if(configMgr.isRunMode()) {			
			execute();
		}
	}
	
	public void execute() {
		
		LOGGER.log(Level.INFO,"running SLAs ...");
		SLAManager slaManager = new SLAManager();
		slaManager.setConfigManager(configMgr);
		slaManager.setRestManager(restMgr);
		
		try {
			LOGGER.log(Level.INFO,"getBaselines ...");
			HashMap<String,Baseline> averages = slaManager.getBaselines();
			LOGGER.log(Level.INFO,"getBaselines : Found :"+averages.size());
			LOGGER.log(Level.INFO,"getActiveRecords ...");
			List<ActiveRecord> activeRecords = slaManager.getActiveRecords();
			LOGGER.log(Level.INFO,"getActiveRecords : Found :"+activeRecords.size());
			LOGGER.log(Level.INFO,"verifySLAs ...");
			List<ActiveRecord> failedSLAs = slaManager.verifySLAs(averages,activeRecords);
			LOGGER.log(Level.INFO,"failedSLAs : Found :"+failedSLAs.size());
			restMgr.postFailedSLAs(configMgr,failedSLAs);			
			LOGGER.log(Level.INFO,"failedSLAs : Posted :"+failedSLAs.size());
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE,"Error running scheduled task",e);
		}
		
	}
	
	public void verify() {
		LOGGER.log(Level.INFO,"verifying SLAs ...");
		SLAManager slaManager = new SLAManager();
		slaManager.setConfigManager(configMgr);
		slaManager.setRestManager(restMgr);
		
		HashMap<String,Baseline> averages = null;
		List<ActiveRecord> activeRecords = null;
		
		try {
			LOGGER.log(Level.INFO,"\nBaselines ...\n");
			averages = slaManager.getBaselines();
			LOGGER.log(Level.INFO,averages.toString());
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE,"Error baselines",e);
		}
		
		try {
			LOGGER.log(Level.INFO,"\nActive Records ...\n");
			activeRecords = slaManager.getActiveRecords();
			LOGGER.log(Level.INFO,activeRecords.toString());
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE,"Error active records",e);
		}
		
		try {
			LOGGER.log(Level.INFO,"\nFailed SLAs ...\n");
			List<ActiveRecord> failedSLAs = slaManager.verifySLAs(averages,activeRecords);
			LOGGER.log(Level.INFO,failedSLAs.toString());
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE,"Error failed slas",e);
		}
	}
	
	public void test() {
		LOGGER.log(Level.INFO,"testing apis ...");
		SLAManager slaManager = new SLAManager();
		slaManager.setConfigManager(configMgr);
		slaManager.setRestManager(restMgr);
		
		try {
			ActiveRecord rec1 = new ActiveRecord("P1","I1","2019-01-31T10:35:13.786+0000");
			ActiveRecord rec2 = new ActiveRecord("P2","I2","2019-01-31T10:35:13.786+0000");
			List<ActiveRecord> records = new ArrayList<ActiveRecord>();
			records.add(rec1);
			records.add(rec2);
			restMgr.postFailedSLAs(configMgr,records);
			//restMgr.postCustomEvent(configMgr, 5);
		
		}catch(Exception e) {
			LOGGER.log(Level.SEVERE,"Error testing APIs",e);
		}
	}
}
