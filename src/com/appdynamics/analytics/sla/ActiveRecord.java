package com.appdynamics.analytics.sla;

import com.appdynamics.analytics.util.DateHelper;

public class ActiveRecord {
	String id;
	String startTime;
	String compareTime;
	
	public ActiveRecord(String _id, String _startTime, String _compareTime) {
		this.id = _id;
		this.startTime = _startTime;
		this.compareTime = _compareTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getCompareTime() {
		return compareTime;
	}

	public void setCompareTime(String compareTime) {
		this.compareTime = compareTime;
	}
	
	public long getTimeDiff() {
		return DateHelper.diffTime(startTime, compareTime);
	}
}
