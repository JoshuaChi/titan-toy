package com.tnc.joshua.graphity;

import org.json.JSONObject;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tnc.joshua.socialgraph.EdgeType;
import com.tnc.joshua.socialgraph.Property;
import com.tnc.joshua.socialgraph.SocialGraph;

/**
 * write optimized Graphity implementation
 * 
 */
public class WriteOptimizedGraphity extends SocialGraph {

	/**
	 * create a write optimized Graphity instance
	 * 
	 * @param graph
	 *            graph to operate on
	 */
	public WriteOptimizedGraphity(Graph graph) {
		super(graph);
	}


	public int follow(long follower, long target) {
		Vertex vFollower = this.getUserById(follower);
		Vertex vTarget = this.getUserById(target);

		if (vFollower == null) {
			// TODO: follower missing
			return -1;
		}
		if (vTarget == null) {
			// TODO: target missing
			return -1;
		}
		if (this.follows(vFollower, vTarget)) {
			// TODO: already following
			return -1;
		}

		vFollower.addEdge(EdgeType.FOLLOW, vTarget);
		return 0;
	}


	public int post(long statusUpdateId, long timestamp, long poster,
			JSONObject content) {
		Vertex vPoster = this.getUserById(poster);

		if (vPoster == null) {
			// TODO: poster missing
			return -1;
		}

		Vertex vStatusUpdate = this.graph.addVertex(null);
		vStatusUpdate.setProperty(Property.StatusUpdate.ID, statusUpdateId);
		vStatusUpdate.setProperty(Property.StatusUpdate.TIMESTAMP, timestamp);
		vStatusUpdate.setProperty(Property.StatusUpdate.CONTENT,
				content.toString());

		vPoster.addEdge(EdgeType.POST, vStatusUpdate);
		return 0;
	}

}
