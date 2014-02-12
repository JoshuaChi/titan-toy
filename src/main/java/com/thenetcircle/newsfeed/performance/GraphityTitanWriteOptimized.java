package com.thenetcircle.newsfeed.performance;

import com.thenetcircle.newsfeed.Property;
import com.thenetcircle.newsfeed.community.WriteOptimizedGraphity;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;

/**
 * write optimized Graphity implementation for Titan
 * 
 */
public class GraphityTitanWriteOptimized extends WriteOptimizedGraphity {

	/**
	 * create a write optimized Graphity instance for Titan
	 * 
	 * @param titanGraph
	 *            Titan graph to operate on
	 */
	public GraphityTitanWriteOptimized(TitanGraph titanGraph) {
		super(titanGraph);

		// TODO: create optimized graph model scheme

		// create user model
		titanGraph.createKeyIndex(Property.User.ID, Vertex.class);
		titanGraph.makeKey(Property.User.NAME).dataType(String.class).make();
	}

}
