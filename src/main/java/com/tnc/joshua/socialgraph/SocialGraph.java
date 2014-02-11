package com.tnc.joshua.socialgraph;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

/**
 * basic social graph with basic functions
 * 
 */
public abstract class SocialGraph implements SocialGraphOperations {

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
	public SocialGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * get a single user by its identifier
	 * 
	 * @param id
	 *            identifier of the user wanted
	 * @return vertex representing the user wanted<br>
	 *         null - if no user with such identifier
	 */
	protected Vertex getUserById(long id) {
		return this.graph.getVertices(Property.User.ID, id).iterator().next();
	}

	/**
	 * check if an user follows another one
	 * 
	 * @param follower
	 *            user expected to follow
	 * @param target
	 *            user expected to be followed
	 * @return true - the user is following<br>
	 *         false - otherwise
	 */
	protected boolean follows(Vertex follower, Vertex target) {
		for (Vertex followed : follower.getVertices(Direction.OUT,
				EdgeType.FOLLOW)) {
			if (followed.equals(target)) {
				return true;
			}
		}
		return false;
	}

	public boolean createUser(long id, String name) {
		Vertex user = this.graph.addVertex(null);

		user.setProperty(Property.User.ID, id);
		user.setProperty(Property.User.NAME, name);
		return true;
	}

}
