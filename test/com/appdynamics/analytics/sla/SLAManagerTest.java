package com.appdynamics.analytics.sla;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.appdynamics.analytics.ConfigManager;

class SLAManagerTest {

	SLAManager slaManager = new SLAManager();
	public Long time = 0l;
	
	@Test
	void test_underSla() throws ParseException {
		
		ConfigManager configManager = new ConfigManager();
		configManager.init("config.json");
		slaManager.setConfigManager(configManager);
		
		HashMap<String, Baseline> averages = new HashMap<String, Baseline>();
		averages.put("1", new Baseline("1",3000d,10d));
		
		List<ActiveRecord> activeRecords = new ArrayList<ActiveRecord>();
		activeRecords.add(new ActiveRecord("1","101","2019-01-14T15:50:32.000+0000"));
		
		List<ActiveRecord> failedSLARecords = slaManager.verifySLAs(averages, activeRecords);
		assertEquals(1,failedSLARecords.size());
	}
	
	@Test
	void test_overSla() throws ParseException {
		
		ConfigManager configManager = new ConfigManager();
		configManager.init("config.json");
		slaManager.setConfigManager(configManager);
		
		HashMap<String, Baseline> averages = new HashMap<String, Baseline>();
		averages.put("1", new Baseline("1",3000d,10d));
		
		List<ActiveRecord> activeRecords = new ArrayList<ActiveRecord>();
		activeRecords.add(new ActiveRecord("1","101","2019-01-14T15:50:32.000+0000"));
		
		List<ActiveRecord> failedSLARecords = slaManager.verifySLAs(averages, activeRecords);
		assertEquals(1,failedSLARecords.size());
		
		//String message = "Time Taken 1472695825 (ms) Average 3000.0 (ms) For Period : 123.0 - 1234.0"; 
		String generatedMessage = ((ActiveRecord)failedSLARecords.get(0)).getFailedMessage(configManager.getDateFormat());
		assertNotNull(generatedMessage);
				
	}
	
	@Test
	void test_overSlaMultipleRecords() throws ParseException {
		
		ConfigManager configManager = new ConfigManager();
		configManager.init("config.json");
		slaManager.setConfigManager(configManager);
		
		HashMap<String, Baseline> averages = new HashMap<String, Baseline>();
		averages.put("1", new Baseline("1",3000d,10d));
		
		List<ActiveRecord> activeRecords = new ArrayList<ActiveRecord>();
		activeRecords.add(new ActiveRecord("1","101","2019-01-14T15:50:32.000+0000"));
		activeRecords.add(new ActiveRecord("1","102","2019-01-14T15:50:32.000+0000"));
		activeRecords.add(new ActiveRecord("1","103","2019-01-14T15:50:32.000+0000"));
		
		List<ActiveRecord> failedSLARecords = slaManager.verifySLAs(averages, activeRecords);
		assertEquals(3,failedSLARecords.size());
		
		//String message = "Time Taken 1472695825 (ms) Average 3000.0 (ms) For Period : 123.0 - 1234.0"; 
		String generatedMessage = ((ActiveRecord)failedSLARecords.get(0)).getFailedMessage(configManager.getDateFormat());
		assertNotNull(generatedMessage);
				
	}
	
	@Test
	void test_IdDoesNotExist() throws ParseException {
		HashMap<String, Baseline> averages = new HashMap<String, Baseline>();
		List<ActiveRecord> activeRecords = new ArrayList<ActiveRecord>();
		activeRecords.add(new ActiveRecord("1","102","2019-01-14T15:50:32.000+0000"));
		
		List<ActiveRecord> failedSLARecords = slaManager.verifySLAs(averages, activeRecords);
		assertEquals(0,failedSLARecords.size());
	}
	
	@Test
	void test_NoData() throws ParseException {
		HashMap<String, Baseline> averages = new HashMap<String, Baseline>();
		List<ActiveRecord> activeRecords = new ArrayList<ActiveRecord>();
		List<ActiveRecord> failedSLARecords = slaManager.verifySLAs(averages, activeRecords);
		assertEquals(0,failedSLARecords.size());
	}
	
	class ActiveRecordTest extends ActiveRecord {

		public ActiveRecordTest(String _id, String _instance, String _startTime) {
			super(_id, _instance, _startTime);
		}
		
		public long getTimeDiff(String dateFormat) throws ParseException {
			return time;
		};
		
	}
	
	@Test
	void test_slaCheck() throws ParseException {
		ConfigManager mgr = new ConfigManager();
		mgr.init("config.json");
		slaManager.setConfigManager(mgr);
		
		Baseline baseline = new Baseline("1",10d,3d);
		ActiveRecordTest activeRecord = new ActiveRecordTest("1","101","");
		time = 10l;
		assertFalse(slaManager.slaFailed(baseline, activeRecord));
		
		time = 30l;
		assertTrue(slaManager.slaFailed(baseline, activeRecord));
		assertTrue(10d == activeRecord.getBase());
		assertTrue(3d == activeRecord.getStdDeviation());
		assertTrue(19d == activeRecord.getSLA());
		assertNotNull(activeRecord.getFailedMessage(mgr.getDateFormat()));
		
	}

}
