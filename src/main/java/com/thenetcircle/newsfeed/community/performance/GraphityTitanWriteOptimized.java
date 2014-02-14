package com.thenetcircle.newsfeed.community.performance;

import com.thenetcircle.newsfeed.EdgeType;
import com.thenetcircle.newsfeed.Property;
import com.thenetcircle.newsfeed.impl.NewsfeedOperationImpl;
import com.thinkaurelius.titan.core.Order;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanKey;
import com.thinkaurelius.titan.core.TitanLabel;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.thinkaurelius.titan.core.TitanVertex;

/**
 * write optimized Graphity implementation for Titan
 * 
 */
public class GraphityTitanWriteOptimized extends NewsfeedOperationImpl {

	private TitanGraph graph = null;

	/**
	 * create a write optimized Graphity instance for Titan
	 * 
	 * @param titanGraph
	 *            Titan graph to operate on
	 */
	public GraphityTitanWriteOptimized(TitanGraph titanGraph) {
		super(titanGraph);
		this.graph = titanGraph;

		// TODO: create optimized graph model scheme
		//
		// Iterator<Vertex> vs = this.graph.getVertices().iterator();
		// while(vs.hasNext()){
		// Vertex vertex = vs.next();
		// this.graph.removeVertex(vertex);
		// }
		//
		// // create user model
		// Set<String> keys = titanGraph.getIndexedKeys(Vertex.class);
		// if (!keys.contains(Property.User.ID)) {
		/**
		 * titanGraph.makeKey(Property.User.ID).dataType(String.class)
		 * .indexed(Vertex.class).indexed(Edge.class).unique()
		 * .indexed("search", Vertex.class);
		 **/
		// titanGraph.createKeyIndex(Property.User.ID, Vertex.class);
		// }
		//
		// if (!keys.contains(Property.User.NAME)) {
		/**
		 * titanGraph.makeKey(Property.User.NAME).dataType(String.class)
		 * .indexed(Vertex.class).indexed(Edge.class).unique()
		 * .indexed("search", Vertex.class) .indexed("search",
		 * Edge.class).make();
		 **/
		// }

		TitanKey time = this.graph.makeKey(Property.Time.TIMESTAMP)
				.dataType(Integer.class).make();
		this.graph.makeLabel(EdgeType.CREATE_BLOG).sortKey(time)
				.sortOrder(Order.DESC).make();
		this.graph.makeLabel(EdgeType.BLOG_COMMENT).sortKey(time)
				.sortOrder(Order.DESC).make();
		this.graph.makeLabel(EdgeType.CREATE_EVENT).sortKey(time)
				.sortOrder(Order.DESC).make();
		this.graph.makeLabel(EdgeType.UPLOAD_PHOTO).sortKey(time)
				.sortOrder(Order.DESC).make();
		this.graph.makeLabel(EdgeType.UPLOAD_AVATAR).sortKey(time)
				.sortOrder(Order.DESC).make();
		this.graph.commit();

	}

	public boolean createUser(String id, String name, int i, int j, int k,
			long lastLogin, String country, float lat, float lon) {
		TitanTransaction tx = this.graph.newTransaction();
		TitanVertex user = tx.addVertex();

		user.setProperty(Property.User.ID, id);
		user.setProperty(Property.User.NAME, name);
		user.setProperty(Property.User.GENDER, i);
		user.setProperty(Property.User.AGE, j);
		user.setProperty(Property.User.MEMBERSHIP, k);
		user.setProperty(Property.User.LASTLOGIN, lastLogin);
		user.setProperty(Property.User.LAT, String.format("%.7f", lat));
		user.setProperty(Property.User.LON, String.format("%.7f", lon));
		tx.commit();
		return true;
	}

	@Override
	public int beFriend(String userId, String targetId) {
		TitanTransaction tx = this.graph.newTransaction();
		TitanVertex vUser = this.getUserById(tx, userId);
		TitanVertex vTarget = this.getUserById(tx, targetId);

		if (vUser == null) {
			// TODO: follower missing
			return -1;
		}
		if (vTarget == null) {
			// TODO: target missing
			return -1;
		}

		if (this.beFriend(vUser, vTarget)) {
			// TODO: already friend with each other
			return -1;
		}

		vUser.addEdge(EdgeType.BEFRIEND, vTarget);
		tx.commit();
		return 0;
	}

	@Override
	public int uploadAvatar(String avatarId, long timestamp, String ownerId) {
		TitanTransaction tx = this.graph.newTransaction();
		TitanVertex vAvatar = tx.addVertex();
		TitanVertex vOwner = this.getUserById(tx, ownerId);

		if (vOwner == null) {
			// TODO: owner missing
			return -1;
		}
		vAvatar.setProperty(Property.Avatar.ID, avatarId);
		vAvatar.setProperty(Property.Avatar.TIMESTAMP, timestamp);
		TitanLabel label = (TitanLabel) this.graph
				.getType(EdgeType.UPLOAD_AVATAR);

		vOwner.addEdge(label, vAvatar);
		tx.commit();
		return 0;
	}

	@Override
	public int uploadPhoto(String photoId, long timestamp, String ownerId) {
		TitanTransaction tx = this.graph.newTransaction();
		TitanVertex vPhoto = tx.addVertex();
		TitanVertex vOwner = this.getUserById(tx, ownerId);

		if (vOwner == null) {
			// TODO: owner missing
			return -1;
		}
		vPhoto.setProperty(Property.Photo.ID, photoId);
		vPhoto.setProperty(Property.Photo.TIMESTAMP, timestamp);

		TitanLabel label = (TitanLabel) this.graph
				.getType(EdgeType.UPLOAD_PHOTO);

		vOwner.addEdge(label, vPhoto);
		tx.commit();
		return 0;
	}

	@Override
	public int createEvent(String eventId, String subject, String content,
			long timestamp, String authorId) {
		TitanTransaction tx = this.graph.newTransaction();
		TitanVertex vEvent = tx.addVertex();
		TitanVertex vAuthor = this.getUserById(tx, authorId);

		if (vAuthor == null) {
			// TODO: author missing
			return -1;
		}
		vEvent.setProperty(Property.Event.ID, eventId);
		vEvent.setProperty(Property.Event.SUBJECT, subject);
		vEvent.setProperty(Property.Event.CONTENT, content);
		vEvent.setProperty(Property.Event.TIMESTAMP, timestamp);

		TitanLabel label = (TitanLabel) this.graph
				.getType(EdgeType.CREATE_EVENT);

		vAuthor.addEdge(label, vEvent);
		tx.commit();
		return 0;
	}

	@Override
	public int createBlog(String blogId, String subject, String content,
			long timestamp, String authorId) {
		TitanTransaction tx = this.graph.newTransaction();
		TitanVertex vBlog = tx.addVertex();
		TitanVertex vAuthor = this.getUserById(tx, authorId);

		if (vAuthor == null) {
			// TODO: author missing
			return -1;
		}
		vBlog.setProperty(Property.Blog.ID, blogId);
		vBlog.setProperty(Property.Blog.SUBJECT, subject);
		vBlog.setProperty(Property.Blog.CONTENT, content);
		vBlog.setProperty(Property.Blog.TIMESTAMP, timestamp);

		TitanLabel label = (TitanLabel) this.graph
				.getType(EdgeType.CREATE_BLOG);

		vAuthor.addEdge(label, vBlog);
		tx.commit();
		return 0;
	}

	@Override
	public int commentBlog(String blogId, String commentId, String comment,
			long timestamp, String userId) {
		TitanTransaction tx = this.graph.newTransaction();
		TitanVertex vComment = tx.addVertex();
		TitanVertex vBlog = this.getBlogById(tx, blogId);

		if (vBlog == null) {
			// TODO: original blog missing
			return -1;
		}
		vComment.setProperty(Property.BlogComment.ID, commentId);
		vComment.setProperty(Property.BlogComment.COMMENT, comment);
		vComment.setProperty(Property.BlogComment.TIMESTAMP, timestamp);

		TitanLabel label = (TitanLabel) this.graph
				.getType(EdgeType.BLOG_COMMENT);

		vBlog.addEdge(label, vComment);
		tx.commit();
		return 0;
	}

	@Override
	public int upgradeMembership(String userId, String membershipType) {
		TitanTransaction tx = this.graph.newTransaction();
		TitanVertex vMembership = null;
		try {
			vMembership = this.getMembershipByType(tx, membershipType);
		} catch (Exception exception) {
			vMembership = tx.addVertex();
			vMembership.setProperty(Property.Membership.TYPE, membershipType);
		}

		TitanVertex vUser = this.getUserById(tx, userId);

		if (vUser == null) {
			// TODO: original blog missing
			return -1;
		}

		vUser.addEdge(EdgeType.UPGRADE_MEMBERSHIP, vMembership);
		tx.commit();
		return 0;
	}

	/**
	 * get a single user by its identifier
	 * 
	 * @param id
	 *            identifier of the user wanted
	 * @return TitanVertex representing the user wanted<br>
	 *         null - if no user with such identifier
	 */
	protected TitanVertex getUserById(TitanTransaction tx, String id) {
		return tx
				.getVertices((TitanKey) this.graph.getType(Property.User.ID),
						id).iterator().next();
	}

	protected TitanVertex getBlogById(TitanTransaction tx, String blogId) {
		return tx
				.getVertices((TitanKey) this.graph.getType(Property.Blog.ID),
						blogId).iterator().next();
	}

	protected TitanVertex getMembershipByType(TitanTransaction tx,
			String membershipType) {
		return tx
				.getVertices(
						(TitanKey) this.graph.getType(Property.Membership.TYPE),
						membershipType).iterator().next();
	}
}
