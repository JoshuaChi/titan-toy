package com.thenetcircle.newsfeed.impl;

import com.thenetcircle.newsfeed.EdgeType;
import com.thenetcircle.newsfeed.NewsfeedOperations;
import com.thenetcircle.newsfeed.Property;
import com.thinkaurelius.titan.core.Order;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanKey;
import com.thinkaurelius.titan.core.TitanLabel;
import com.thinkaurelius.titan.core.TitanTransaction;
import com.thinkaurelius.titan.core.TitanType;
import com.thinkaurelius.titan.core.TitanVertex;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * write optimized Graphity implementation for Titan
 * 
 */
public class NewsfeedOperationImpl implements NewsfeedOperations {

	private TitanGraph graph = null;

	/**
	 * create a write optimized Graphity instance for Titan
	 * 
	 * @param titanGraph
	 *            Titan graph to operate on
	 */
	public NewsfeedOperationImpl(TitanGraph titanGraph) {
		this.graph = titanGraph;

		TitanType indexExists = this.graph.getType(Property.User.ID);


		//indexes can only be created once.
		if (indexExists == null) {
			// user index
			this.graph.makeKey(Property.User.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();
			this.graph.makeKey(Property.User.NAME).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class)
					.indexed("search", Edge.class).make();

			// blog index
			this.graph.makeKey(Property.Blog.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();
			
			this.graph.makeKey(Property.BlogComment.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();

			this.graph.makeKey(Property.Avatar.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();

			this.graph.makeKey(Property.Photo.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();
			this.graph.makeKey(Property.Event.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();

			this.graph.makeKey(Property.Membership.TYPE).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();

			// edge sort index
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

	}

	public boolean createUser(String id, String name, int i, int j, int k,
			long lastLogin, String country, float lat, float lon) {
		
		if(this.isUserCreated(id)){
			return false;
		}
		
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

		if (this.isFriend(vUser, vTarget)) {
			// TODO: already friend with each other
			return -1;
		}

		vUser.addEdge(EdgeType.BEFRIEND, vTarget);
		tx.commit();
		return 0;
	}

	@Override
	public int uploadAvatar(String avatarId, long timestamp, String ownerId) {
		if(this.isUploadedAvatar(avatarId)){
			return -1;
		}
		
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
		if(this.isUploadedPhoto(photoId)) {
			return -1;
		}
		
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
		if(this.isCreatedEvent(eventId)) {
			return -1;
		}
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
		if(this.isCreatedBlog(blogId)) {
			return -1;
		}
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
		if(this.isCreatedComment(commentId)) {
			return -1;
		}
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
		if(this.hasMembership(userId, membershipType)) {
			return -1;
		}
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
	


	protected boolean isUserCreated(String id) {
		TitanTransaction t = this.graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.User.ID, id);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean isUploadedAvatar(String avatarId) {
		TitanTransaction t = this.graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.Avatar.ID, avatarId);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean isUploadedPhoto(String photoId) {
		TitanTransaction t = this.graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.Photo.ID, photoId);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean isCreatedEvent(String eventId) {
		TitanTransaction t = this.graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.Event.ID, eventId);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean isCreatedBlog(String blogId) {
		TitanTransaction t = this.graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.Blog.ID, blogId);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean isCreatedComment(String commentId) {
		TitanTransaction t = this.graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.BlogComment.ID, commentId);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean hasMembership(String userId, String membershipType) {
		TitanTransaction t = this.graph.buildTransaction().start();
		TitanVertex isCreated = t.getVertex(Property.Membership.TYPE, membershipType);
		t.commit();
		if (isCreated != null) {
			return true;
		}
		return false;
	}

	protected boolean isFriend(Vertex user, Vertex target) {
		for (Vertex followed : user.getVertices(Direction.OUT,
				EdgeType.BEFRIEND)) {
			if (followed.equals(target)) {
				return true;
			}
		}
		return false;
	}
}
