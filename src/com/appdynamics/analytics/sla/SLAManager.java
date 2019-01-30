package com.appdynamics.analytics.sla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONArray;

import com.appdynamics.analytics.ConfigManager;
import com.appdynamics.analytics.rest.RestManager;
import com.appdynamics.analytics.util.DateHelper;
import com.appdynamics.analytics.util.Range;

public class SLAManager {

	public static final String RANGE_END = "range-end";
	public static final String RANGE_START = "range-start";
	private final static Logger LOGGER = Logger.getLogger(SLAManager.class.getName());
	
	ConfigManager configManager;
	RestManager restManager;
	
	public void setConfigManager(ConfigManager configManager) {
		this.configManager = configManager;
	}

	public void setRestManager(RestManager restManager) {
		this.restManager = restManager;
	}

	public HashMap<String,Long> getAverages() throws Exception{
		HashMap<String,Long> averages = new HashMap<String,Long>();
		Range range = DateHelper.getLastTwoWeeksSinceYesterday();
		
		averages.put(RANGE_START, range.getStart());
		averages.put(RANGE_END, range.getEnd());
		
		JSONArray jsonData = restManager.query(configManager, configManager.getQueryAverage(), range, 15000);
		for (int i=0; i < jsonData.size(); i++) {
			JSONArray rec =  (JSONArray) jsonData.get(i);
			averages.put(rec.get(0).toString(),new Long(rec.get(i).toString()));
		}
		return averages;
	}
	
	public HashMap<String,ActiveRecord> getActiveRecords() throws Exception{
		HashMap<String,ActiveRecord> slas = new HashMap<String,ActiveRecord>();
		JSONArray jsonData = restManager.query(configManager, configManager.getQueryActive(), DateHelper.getTimeRangeForNow(), 15000);
		for (int i=0; i < jsonData.size(); i++) {
			JSONArray rec =  (JSONArray) jsonData.get(i);
			String id = rec.get(0).toString();
			String instance = rec.get(1).toString();
			String startTime = rec.get(2).toString();
			String milestoneTime = rec.get(3).toString();
			ActiveRecord activeRec = new ActiveRecord(id,instance,startTime,milestoneTime);
			slas.put(id,activeRec);
		}
		return slas;
	}

	public List<ActiveRecord> verifySLAs(HashMap<String, Long> averages,
			HashMap<String, ActiveRecord> activeRecords) {
		
		String message = getRangeMessage(averages);
		List<ActiveRecord> failedSLAs = new ArrayList<ActiveRecord>();
		Object[] records = activeRecords.values().toArray();
		for(int i=0; i< records.length; i++) {
			ActiveRecord rec = (ActiveRecord) records[0];
			if(averages.containsKey(rec.getId())) {
				long average = averages.get(rec.getId());
				if(!slaCheck(average,rec.getTimeDiff())) {
					rec.setAverage(average);
					rec.setRangeMessage(message);
					failedSLAs.add(rec);
				}
			}
		}
		
		return failedSLAs;
	}
	
	private String getRangeMessage(HashMap<String, Long> averages) {
		return averages.get(RANGE_START)+" - "+averages.get(RANGE_END);
	}

	public boolean slaCheck(long average, long timeDifference) {
		float percentageSLA = getPercent(average);
		float diff = average * (percentageSLA/100f);
		float sla = average + diff;
		if(timeDifference > sla) {
			return true;
		}
		return false;
	}

	private long getPercent(long average) {
		HashMap<Long,Long> slas = configManager.getSLAs();
		Long lastPercent = 0l;
		for ( Long key : slas.keySet() ) {
		    long convertToMillis = key.longValue() * 60 * 1000;
		    lastPercent = slas.get(key);
		    if(average < convertToMillis) {
		    	break;
		    }
		}
		return lastPercent;
	}
}
