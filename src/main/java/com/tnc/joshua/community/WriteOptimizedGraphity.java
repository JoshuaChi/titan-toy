package com.tnc.joshua.community;

import java.util.NoSuchElementException;

import org.json.JSONObject;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tnc.joshua.newsfeed.EdgeType;
import com.tnc.joshua.newsfeed.Property;
import com.tnc.joshua.newsfeed.impl.NewsfeedOperationImpl;

/**
 * write optimized Graphity implementation
 * 
 */
public class WriteOptimizedGraphity extends NewsfeedOperationImpl {

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


	@Override
	public int beFriend(long userId, long targetId) {
		Vertex vUser = this.getUserById(userId);
		Vertex vTarget = this.getUserById(targetId);

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
		return 0;
	}


	@Override
	public int uploadAvatar(long avatarId, long timestamp, long ownerId) {
		Vertex vAvatar = this.graph.addVertex(null);
		Vertex vOwner = this.getUserById(ownerId);

		if (vOwner == null) {
			// TODO: owner missing
			return -1;
		}
		vAvatar.setProperty(Property.Avatar.ID, avatarId);
		vAvatar.setProperty(Property.Avatar.TIMESTAMP, timestamp);
		
		vOwner.addEdge(EdgeType.UPLOAD_AVATAR, vAvatar);
		
		return 0;
	}


	@Override
	public int uploadPhoto(long photoId, long timestamp, long ownerId) {
		Vertex vPhoto = this.graph.addVertex(null);
		Vertex vOwner = this.getUserById(ownerId);

		if (vOwner == null) {
			// TODO: owner missing
			return -1;
		}
		vPhoto.setProperty(Property.Photo.ID, photoId);
		vPhoto.setProperty(Property.Photo.TIMESTAMP, timestamp);
		
		vOwner.addEdge(EdgeType.UPLOAD_PHOTO, vPhoto);
		
		return 0;
	}


	@Override
	public int createEvent(long eventId, String subject, String content,
			long timestamp, long authorId) {
		Vertex vEvent = this.graph.addVertex(null);
		Vertex vAuthor = this.getUserById(authorId);

		if (vAuthor == null) {
			// TODO: author missing
			return -1;
		}
		vEvent.setProperty(Property.Event.ID, eventId);
		vEvent.setProperty(Property.Event.SUBJECT, subject);
		vEvent.setProperty(Property.Event.CONTENT, content);
		vEvent.setProperty(Property.Event.TIMESTAMP, timestamp);
		
		vAuthor.addEdge(EdgeType.CREATE_EVENT, vEvent);
		return 0;
	}


	@Override
	public int createBlog(long blogId, String subject, String content,
			long timestamp, long authorId) {
		Vertex vBlog = this.graph.addVertex(null);
		Vertex vAuthor = this.getUserById(authorId);

		if (vAuthor == null) {
			// TODO: author missing
			return -1;
		}
		vBlog.setProperty(Property.Blog.ID, blogId);
		vBlog.setProperty(Property.Blog.SUBJECT, subject);
		vBlog.setProperty(Property.Blog.CONTENT, content);
		vBlog.setProperty(Property.Blog.TIMESTAMP, timestamp);
		
		vAuthor.addEdge(EdgeType.CREATE_BLOG, vBlog);
		return 0;
	}


	@Override
	public int commentBlog(long blogId, long commentId, String comment,
			long timestamp, long userId) {
		Vertex vComment = this.graph.addVertex(null);
		Vertex vBlog = this.getBlogById(blogId);

		if (vBlog == null) {
			// TODO: original blog missing
			return -1;
		}
		vComment.setProperty(Property.BlogComment.ID, commentId);
		vComment.setProperty(Property.BlogComment.COMMENT, comment);
		vComment.setProperty(Property.BlogComment.TIMESTAMP, timestamp);
		
		vBlog.addEdge(EdgeType.BLOG_COMMENT, vComment);
		return 0;
	}


	@Override
	public int upgradeMembership(long userId, int membershipType) {

		Vertex vMembership = null;
		try {
			vMembership = this.getMembershipByType(membershipType);
		}catch(NoSuchElementException exception){
			vMembership = this.graph.addVertex(null);
			vMembership.setProperty(Property.membership.TYPE, membershipType);
		}
		
		
		Vertex vUser = this.getUserById(userId);

		if (vUser == null) {
			// TODO: original blog missing
			return -1;
		}
		
		vUser.addEdge(EdgeType.UPGRADE_MEMBERSHIP, vMembership);
		return 0;
	}


}
