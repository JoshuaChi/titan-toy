package com.tnc.joshua.newsfeed.impl;

import java.math.BigDecimal;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tnc.joshua.newsfeed.EdgeType;
import com.tnc.joshua.newsfeed.NewsfeedOperations;
import com.tnc.joshua.newsfeed.Property;

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


	protected Vertex getBlogById(long blogId) {
		return this.graph.getVertices(Property.Blog.ID, blogId).iterator().next();
	}


	protected Vertex getMembershipByType(long membershipType) {
		return this.graph.getVertices(Property.membership.TYPE, membershipType).iterator().next();
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

	protected boolean beFriend(Vertex user, Vertex target) {
		for (Vertex followed : user.getVertices(Direction.OUT,
				EdgeType.BEFRIEND)) {
			if (followed.equals(target)) {
				return true;
			}
		}
		return false;
	}

	public boolean createUser(long id, String name, int i, int j,
			int k, long lastLogin, String country, float lat, float lon) {
		Vertex user = this.graph.addVertex(null);

		user.setProperty(Property.User.ID, id);
		user.setProperty(Property.User.NAME, name);
		user.setProperty(Property.User.GENDER, i);
		user.setProperty(Property.User.AGE, j);
		user.setProperty(Property.User.MEMBERSHIP, k);
		user.setProperty(Property.User.LASTLOGIN, lastLogin);
		user.setProperty(Property.User.LAT, String.format("%.7f", lat));
		user.setProperty(Property.User.LON, String.format("%.7f", lon));
		return true;
	}

}
