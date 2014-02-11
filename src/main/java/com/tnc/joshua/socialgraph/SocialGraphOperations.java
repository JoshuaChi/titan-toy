package com.tnc.joshua.socialgraph;

import org.json.JSONObject;

/**
 * social graph operations
 * 
 */
public interface SocialGraphOperations {

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
	boolean createUser(long id, String name);

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

}
