package com.thenetcircle.newsfeed.impl;

import com.thenetcircle.newsfeed.EdgeType;
import com.thenetcircle.newsfeed.NewsfeedOperations;
import com.thenetcircle.newsfeed.Property;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

/**
 * basic social graph with basic functions
 * 
 */
public abstract class NewsfeedOperationImpl implements NewsfeedOperations {

	/**
	 * graph to operate on
	 */
	protected Graph graph;

	/**
	 * create a basic social graph instance
	 * 
	 * @param graph
	 *            graph to operate on
	 */
	public NewsfeedOperationImpl(Graph graph) {
		this.graph = graph;
	}

	protected boolean beFriend(Vertex user, Vertex target) {
		for (Vertex followed : user.getVertices(Direction.OUT,
				EdgeType.BEFRIEND)) {
			if (followed.equals(target)) {
				return true;
			}
		}
		return false;
	}
}