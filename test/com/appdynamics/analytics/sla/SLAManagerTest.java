package com.appdynamics.analytics.sla;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.appdynamics.analytics.ConfigManager;

class SLAManagerTest {

	SLAManager slaManager = new SLAManager();
	
	@Test
	void test_underSla() {
		HashMap<String, Long> averages = new HashMap<String, Long>();
		averages.put("1", 3000l);
		
		HashMap<String,ActiveRecord> activeRecords = new HashMap<String,ActiveRecord>();
		activeRecords.put("1", new ActiveRecord("1","101","2019-01-14T15:50:32.000Z","2019-01-14T15:50:33.000Z"));
		
		List<ActiveRecord> failedSLARecords = slaManager.verifySLAs(averages, activeRecords);
		assertEquals(0,failedSLARecords.size());
	}
	
	@Test
	void test_overSla() {
		HashMap<String, Long> averages = new HashMap<String, Long>();
		averages.put(SLAManager.RANGE_START, 123l);
		averages.put(SLAManager.RANGE_END, 1234l);
		averages.put("1", 3000l);
		
		HashMap<String,ActiveRecord> activeRecords = new HashMap<String,ActiveRecord>();
		activeRecords.put("1", new ActiveRecord("1","101","2019-01-14T15:50:32.000Z","2019-01-14T15:50:37.000Z"));
		
		List<ActiveRecord> failedSLARecords = slaManager.verifySLAs(averages, activeRecords);
		assertEquals(1,failedSLARecords.size());
		
		String message = "Time Taken 5000 (ms) Average 3000 (ms) For Period : 123 - 1234"; 
		String generatedMessage = ((ActiveRecord)failedSLARecords.get(0)).getFailedMessage();
		assertEquals(message,generatedMessage);
				
	}
	
	@Test
	void test_IdDoesNotExist() {
		HashMap<String, Long> averages = new HashMap<String, Long>();
		HashMap<String,ActiveRecord> activeRecords = new HashMap<String,ActiveRecord>();
		activeRecords.put("9", new ActiveRecord("1","102","2019-01-14T15:50:32.000Z","2019-01-14T15:50:33.000Z"));
		
		List<ActiveRecord> failedSLARecords = slaManager.verifySLAs(averages, activeRecords);
		assertEquals(0,failedSLARecords.size());
	}
	
	@Test
	void test_NoData() {
		HashMap<String, Long> averages = new HashMap<String, Long>();
		HashMap<String,ActiveRecord> activeRecords = new HashMap<String,ActiveRecord>();
		List<ActiveRecord> failedSLARecords = slaManager.verifySLAs(averages, activeRecords);
		assertEquals(0,failedSLARecords.size());
	}
	
	@Test
	void test_slaCheck() {
		ConfigManager mgr = new ConfigManager();
		mgr.init("config.json");
		slaManager.setConfigManager(mgr);
		assertFalse(slaManager.slaCheck(10, 10));
		assertTrue(slaManager.slaCheck(10, 14));
		assertTrue(slaManager.slaCheck(60*60*1000, 70*60*1000));
		
	}

}
