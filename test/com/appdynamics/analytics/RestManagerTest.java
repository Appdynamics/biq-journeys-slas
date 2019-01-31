package com.appdynamics.analytics;
import java.util.ArrayList;
import java.util.List;

import com.appdynamics.analytics.rest.RestManager;
import com.appdynamics.analytics.sla.ActiveRecord;


public class RestManagerTest {

	public static void main(String[] args) {
		ConfigManager mgr = new ConfigManager();
		mgr.init("config.json");
		
		RestManager restMgr = new RestManager();
		
		try {
			
//			Range range = DateHelper.getTimeRangeForNow();
//			range.sysInfo();
//			Object results = restMgr.query(mgr, "SELECT * FROM transactions", range, 10);
//			System.out.println(results);
			
			ActiveRecord rec1 = new ActiveRecord("P1","I1","2019-01-14T16:50:32.000Z");
			ActiveRecord rec2 = new ActiveRecord("P2","I2","2019-01-14T16:50:32.000Z");
			List<ActiveRecord> records = new ArrayList<ActiveRecord>();
			records.add(rec1);
			records.add(rec2);
			restMgr.postFailedSLAs(mgr,records);	
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
