package com.appdynamics.analytics.sla;

public class Baseline {

	String id;
	Double base;
	Double stdDev;
	
	public Baseline(String _id, Double _base, Double _stdDev) {
		id = _id;
		base = _base;
		stdDev = _stdDev;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getBase() {
		return base;
	}

	public void setBase(Double base) {
		this.base = base;
	}

	public Double getStdDev() {
		return stdDev;
	}

	public void setStdDev(Double stdDev) {
		this.stdDev = stdDev;
	}
	
	@Override
	public String toString() {
		return "id:"+id+" base:"+base+" stdev:"+stdDev;
	}
	
}
