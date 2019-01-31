package com.appdynamics.analytics.sla;

import java.text.ParseException;

import com.appdynamics.analytics.util.DateHelper;

public class ActiveRecord {
	String id;
	String instance;
	String startTime;
	String rangeMessage;
	private double base;
	private double std;
	private double sla;
	
	public ActiveRecord(String _id, String _instance, String _startTime) {
		this.id = _id;
		this.startTime = _startTime;
		this.instance = _instance;
	}

	public String getId() {
		return id;
	}
	
	public long getTimeDiff(String dateFormat) throws ParseException {
		return DateHelper.diffTimeFromNow(dateFormat,startTime);
	}

	public void setBase(double base) {
		this.base = base;
	}
	
	public double getBase() {
		return base;
	}

	public void setStdDeviation(double std) {
		this.std = std;
	}
	
	public double getStdDeviation() {
		return std;
	}
	
	public void setSLA(double sla) {
		this.sla = sla;
	}
	
	public double getSLA() {
		return sla;
	}
	
	public void setRangeMessage(String rangeMessage) {
		this.rangeMessage = rangeMessage;	
	}
	
	
	public String getFailedMessage(String dateFormat) throws ParseException {
		return "{message:'Time Taken "+getTimeDiff(dateFormat)+" (ms) Baseline="+this.base+"(ms) STD="+this.std+" SLA Threshold="+sla+" For Period : "+this.rangeMessage+"'}";
	}

	public String getInstance() {
		return this.instance;
	}
	
	@Override
	public String toString() {
		return "id:"+id+" uid:"+instance+" time:"+startTime;
	}
}
