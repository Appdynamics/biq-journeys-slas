package com.appdynamics.analytics.sla;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
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

	public HashMap<String,Baseline> getBaselines() throws Exception{
		HashMap<String,Baseline> averages = new HashMap<String,Baseline>();
		Range range = DateHelper.getLastTwoWeeksSinceYesterday();
		
		JSONArray jsonData = restManager.query(configManager, configManager.getQueryBaseline(), range, 15000);
		for (int i=0; i < jsonData.size(); i++) {
			JSONArray rec =  (JSONArray) jsonData.get(i);
			if(rec == null || rec.get(0) == null || rec.get(1) == null || rec.get(2) == null) {
				continue;
			}
			Double base = new Double(rec.get(1).toString());
			Double std = new Double(rec.get(2).toString());
			
			averages.put(rec.get(0).toString(),new Baseline(rec.get(0).toString(),base,std));
		}
		return averages;
	}
	
	public List<ActiveRecord> getActiveRecords() throws Exception{
		List<ActiveRecord> slas = new ArrayList<ActiveRecord>();
		JSONArray jsonData = restManager.query(configManager, configManager.getQueryActive(), DateHelper.getTimeRangeForNow(), 15000);
		for (int i=0; i < jsonData.size(); i++) {
			JSONArray rec =  (JSONArray) jsonData.get(i);
			String id = rec.get(0).toString();
			String instance = rec.get(1).toString();
			String startTime = rec.get(2).toString();
			ActiveRecord activeRec = new ActiveRecord(id,instance,startTime);
			slas.add(activeRec);
		}
		return slas;
	}

	public List<ActiveRecord> verifySLAs(HashMap<String, Baseline> averages,
			List<ActiveRecord> activeRecords) throws ParseException {
		
		List<ActiveRecord> failedSLAs = new ArrayList<ActiveRecord>();
		if(averages == null) {
			LOGGER.log(Level.SEVERE,"\nNo Baselines Found ...\n");
			return failedSLAs;
		}
		Range range = DateHelper.getLastTwoWeeksSinceYesterday();
		
		String message = range.getStart()+" : "+range.getEnd();
		for(int i=0; i< activeRecords.size(); i++) {
			ActiveRecord rec = (ActiveRecord) activeRecords.get(i);
			if(averages.containsKey(rec.getId())) {
				Baseline baseline = averages.get(rec.getId());
				if(slaFailed(baseline,rec)) {
					rec.setRangeMessage(message);
					failedSLAs.add(rec);
				}
			}
		}
		
		return failedSLAs;
	}
	

	public boolean slaFailed(Baseline baseline, ActiveRecord rec) throws ParseException {
		long timeDifference = rec.getTimeDiff(configManager.getDateFormat());
		double stdevMultiple = getStandardDev(baseline.getBase());
		double sla = baseline.getBase() + (baseline.getStdDev()* stdevMultiple);
		
		if(timeDifference > sla) {
			rec.setBase(baseline.getBase());
			rec.setStdDeviation(baseline.getStdDev());
			rec.setSLA(sla);
			return true;
		}
		return false;
	}

	public long getStandardDev(Double base) {
		HashMap<Long,Long> slas = configManager.getSLAs();
		Long stdDev = 0l;
		for ( Long key : slas.keySet() ) {
		    long convertToMillis = key.longValue() * 60 * 1000;
		    stdDev = slas.get(key);
		    if(base < convertToMillis) {
		    	break;
		    }
		}
		return stdDev;
	}
}
