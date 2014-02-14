package com.thenetcircle.newsfeed;

/**
 * edge types used in social graphs
 * 
 * 
 */
public class EdgeType {
	public static final String ACTIVITY = "activity";

	/**
	 * user is a friend of another
	 * 
	 */
	public static final String BEFRIEND = "be_friend";
	
	/**
	 * user create event/blog
	 */
	public static final String CREATE_EVENT = "create_event";

	/**
	 * user create blog
	 */
	public static final String CREATE_BLOG = "create_blog";
	
	/**
	 * user upload avatar
	 */
	public static final String UPLOAD_AVATAR = "upload_avatar";
	
	/**
	 * user upload photo
	 */
	public static final String UPLOAD_PHOTO = "upload_photo";
	
	/**
	 * user upgrade membership
	 */
	public static final String UPGRADE_MEMBERSHIP = "upgrade_membership";
	
	/**
	 * blog comment
	 */
	public static final String BLOG_COMMENT = "blog_comment";
	
	/**
	 * user comments blog
	 */
	public static final String USER_BLOG_COMMENT = "user_blog_comment";

}
