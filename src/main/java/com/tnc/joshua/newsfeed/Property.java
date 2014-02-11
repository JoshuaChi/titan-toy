package com.tnc.joshua.newsfeed;

/**
 * properties used in social graphs
 * 
 * 
 */
public class Property {

	/**
	 * properties used for user vertices
	 * 
	 * 
	 */
	public static class User {

		/**
		 * unique user identifier
		 */
		public static final String ID = "user_id";

		/**
		 * user name
		 */
		public static final String NAME = "user_name";

		/**
		 * user gender
		 */
		public static final String GENDER = "user_gender";

		/**
		 * user age
		 */
		public static final String AGE = "user_age";

		/**
		 * user membership
		 */
		public static final String MEMBERSHIP = "user_membership";

		/**
		 * user lastlogin
		 */
		public static final String LASTLOGIN = "user_lastlogin";
		
		public static final String COUNTRY = "country";
		
		public static final String LAT="lat";
		
		public static final String LON="lon";

	}

	public static class membership {
		public static final String TYPE = "membership_type";
	}
	
	/**
	 * properties for status updates
	 * 
	 * 
	 */
	public static class StatusUpdate {

		/**
		 * unique status update identifier
		 */
		public static final String ID = "status_update_id";

		/**
		 * creation time-stamp
		 */
		public static final String TIMESTAMP = "timestamp";

		/**
		 * JSON formatted item content
		 */
		public static final String CONTENT = "content";

	}


	public static class Avatar {
		public static final String ID = "avatar_id";
		public static final String TIMESTAMP = "timestamp";
	}
	
	public static class Photo {
		public static final String ID = "photo_id";
		public static final String TIMESTAMP = "timestamp";
	}
	
	public static class Event {
		public static final String ID = "event_id";
		public static final String SUBJECT = "event_subject";
		public static final String CONTENT = "event_content";
		public static final String TIMESTAMP = "timestamp";
	}
	
	public static class Blog {
		public static final String ID = "blog_id";
		public static final String SUBJECT = "blog_subject";
		public static final String CONTENT = "blog_content";
		public static final String TIMESTAMP = "timestamp";
	}
	
	public static class BlogComment {
		public static final String ID = "comment_id";
		public static final String BLOG_ID = "blog_id";
		public static final String COMMENT = "comment";
		public static final String TIMESTAMP = "timestamp";
	}

}
