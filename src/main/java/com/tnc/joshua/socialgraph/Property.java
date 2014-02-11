package com.tnc.joshua.socialgraph;

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

}
