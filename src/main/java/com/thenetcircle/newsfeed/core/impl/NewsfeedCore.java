/**
 * 
 */
package com.thenetcircle.newsfeed.core.impl;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

import com.thenetcircle.newsfeed.EdgeType;
import com.thenetcircle.newsfeed.Property;
import com.thenetcircle.newsfeed.Tools;
import com.thenetcircle.newsfeed.core.Criteria;
import com.thenetcircle.newsfeed.core.INewsfeedCore;
import com.thenetcircle.newsfeed.exceptions.NewsfeedException;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.java.GremlinPipeline;
import com.tinkerpop.pipes.PipeFunction;
import com.tinkerpop.pipes.util.structures.Pair;

/**
 * @author jchi
 * 
 */
public class NewsfeedCore implements INewsfeedCore {
	private TitanGraph g = null;

	public NewsfeedCore(TitanGraph g) {
		super();
		this.g = g;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thenetcircle.newsfeed.core.INewsfeedCore#filter(com.thenetcircle.
	 * newsfeed.core.Criteria)
	 */
	@Override
	public List<Vertex> filter(Criteria c) throws NewsfeedException {
		int offset = c.getOffset();
		int limit = c.getLimit();
		GremlinPipeline<Object, Vertex> gp = new GremlinPipeline<Object, Vertex>(
				g);

		if (c.getStartVertex() != null) {
			gp = gp.start(c.getStartVertex()).cast(Vertex.class);
		}

		//edge paths
		List<String> edges = c.getEdges();
		if (edges != null) {
			for (String e : edges) {
				gp = gp.out(e);
			}
		}
		
		//filter tags
		final BitSet tags = c.getTags();
		if(tags != null) {
			gp = gp.filter(new PipeFunction<Vertex, Boolean>() {
				public Boolean compute(Vertex v) {
					byte[] bitA = v.getProperty(Property.Activity.TAG);
					BitSet bitset = Tools.fromByteArray(bitA);
					if(bitset != null){
						bitset.and(tags);
						if (!bitset.isEmpty()) {
							return true;
						}	
					}
					return false;
				}
			});
		}
		
		//order by timestamp
		List<Vertex> result = gp
				.order(new PipeFunction<Pair<Vertex, Vertex>, Integer>() {
					@Override
					public Integer compute(Pair<Vertex, Vertex> argument) {
						Integer iB = new Integer(argument.getB()
								.getProperty(Property.Time.TIMESTAMP)
								.toString());
						Integer iA = new Integer(argument.getA()
								.getProperty(Property.Time.TIMESTAMP)
								.toString());
						return iB.compareTo(iA);
					}

				}).range(offset, limit).toList();

		return result;
	}
}
