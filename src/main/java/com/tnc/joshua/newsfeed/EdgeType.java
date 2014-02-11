package com.tnc.joshua.newsfeed;

/**
 * edge types used in social graphs
 * 
 * 
 */
public class EdgeType {

	/**
	 * user is a friend of another
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
	 * user comments blog
	 */
	public static final String BLOG_COMMENT = "blog_comment";
	
	/**
	 * user follows another
	 */
	public static final String FOLLOW = "follows";

	/**
	 * user posted a status update
	 */
	public static final String POST = "posted";

}
