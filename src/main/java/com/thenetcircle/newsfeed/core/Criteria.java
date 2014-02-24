package com.thenetcircle.newsfeed.core;

import java.util.List;
import java.util.Map;

import com.tinkerpop.blueprints.Vertex;

public class Criteria {

	private int offset = 0;
	private int limit = 10;
	private GeoCriteria center = null;
	private Vertex startVertex = null;
	
	private List<String> edges = null;

	public List<String> getEdges() {
		return edges;
	}

	public void setEdges(List<String> edges) {
		this.edges = edges;
	}

	public Vertex getStartVertex() {
		return startVertex;
	}

	public void setStartVertex(Vertex startVertex) {
		this.startVertex = startVertex;
	}

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
