package com.thenetcircle.newsfeed.core;

import java.util.Map;

public class Criteria {

	private int offset;
	private int limit;
	private GeoCriteria center;
	
	private Map<String, Object> properties;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public GeoCriteria getCenter() {
		return center;
	}

	public void setCenter(GeoCriteria center) {
		this.center = center;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	
}
