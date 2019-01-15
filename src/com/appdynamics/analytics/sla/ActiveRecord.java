package com.appdynamics.analytics.sla;

import com.appdynamics.analytics.util.DateHelper;

public class ActiveRecord {
	String id;
	String instance;
	String startTime;
	String compareTime;
	Long average;
	String rangeMessage;
	
	public ActiveRecord(String _id, String _instance, String _startTime, String _compareTime) {
		this.id = _id;
		this.startTime = _startTime;
		this.compareTime = _compareTime;
		this.instance = _instance;
	}

	public String getId() {
		return id;
	}
	
	public long getTimeDiff() {
		return DateHelper.diffTime(startTime, compareTime);
	}

	public void setAverage(long average) {
		this.average = average;
	}
	
	public void setRangeMessage(String rangeMessage) {
		this.rangeMessage = rangeMessage;	
	}
	
	
	public String getFailedMessage() {
		return "Time Taken "+getTimeDiff()+" (ms) Average "+this.average+" (ms) For Period : "+this.rangeMessage;
	}

	public long getAverage() {
		return this.average;
	}

	public String getInstance() {
		return this.instance;
	}
}
