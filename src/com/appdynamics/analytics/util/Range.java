package com.appdynamics.analytics.util;

public class Range {
	long start;
	long end;
	
	public Range(long start, long end) {
		this.start = start;
		this.end = end;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public void sysInfo() {
		System.out.println("Start : "+start+" End : "+end);
	}
	
	
}
