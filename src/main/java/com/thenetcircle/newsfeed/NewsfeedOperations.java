package com.thenetcircle.newsfeed;

import org.json.JSONObject;

/**
 * social graph operations
 * 
 */
public interface NewsfeedOperations {

	/**
	 * create a user
	 * 
	 * @param id
	 *            unique identifier for the new user
	 * @param name
	 *            user name
	 * @return true - user created successfully<br>
	 *         false - user identifier in use
	 */
	boolean createUser(long id, String name, int gender, int age,
			int membership, long lastLogin, String country, float lat, float lon);

	/**
	 * register a fellowship from one user to another
	 * 
	 * @param followingId
	 *            identifier of the user following
	 * @param followedId
	 *            identifier of the user followed
	 * @return 0 - user is now following<br>
	 *         error code - if the registration failed
	 */
	int follow(long followingId, long followedId);

	/**
	 * create a new status update for an user
	 * 
	 * @param statusUpdateId
	 *            identifier for the new status update
	 * @param timestamp
	 *            creation time-stamp
	 * @param postingId
	 *            identifier of the user posting
	 * @param content
	 *            JSON formatted item content
	 * @return 0 - status update has been created successfully<br>
	 *         error code - if the creation failed
	 */
	int post(long statusUpdateId, long timestamp, long postingId,
			JSONObject content);

	/**
	 * user be a friend of another user
	 * 
	 * @param userId
	 *            identifier of the user
	 * @param targetId
	 *            identifier of the target user
	 * @return 0 - user is now following<br>
	 *         error code - if the registration failed
	 */
	int beFriend(long userId, long targetId);
	
	int uploadAvatar(long avatarId, long timestamp, long ownerId);
	
	int uploadPhoto(long photoId, long timestamp, long ownerId);

	int createEvent(long eventId, String subject, String content, long timestamp, long authorId);

	int createBlog(long blogId, String subject, String content, long timestamp, long authorId);

	int commentBlog(long blogId, long commentId, String comment, long timestamp, long userId);
	
	int upgradeMembership(long userId, int membershipType);
}
