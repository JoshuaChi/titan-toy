package com.tnc.joshua.test;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tnc.joshua.newsfeed.EdgeType;
import com.tnc.joshua.newsfeed.Property;
import com.tnc.joshua.newsfeed.impl.NewsfeedOperationImpl;
import com.tnc.joshua.performance.GraphityTitanWriteOptimized;

public class Startup {
	public static void main(String[] args) {
		/**
		 * init graph
		 */
		Configuration conf = new BaseConfiguration();
		conf.setProperty("storage.backend", "cassandra");
		conf.setProperty("storage.hostname", "127.0.0.1");
		conf.setProperty("storage.cassandra-config-dir",
				"config/cassandra.yaml");
		conf.setProperty("storage.index.search.backend", "elasticsearch");
		conf.setProperty("storage.index.search.directory", "/tmp/searchindex");
		conf.setProperty("storage.index.search.client-only", "false");
		conf.setProperty("storage.index.search.local-mode", "true");

		TitanGraph g = TitanFactory.open(conf);
		NewsfeedOperationImpl graphity = new GraphityTitanWriteOptimized(g);

		/**
		 * prepare users
		 */
		if (!(graphity.createUser(1, "Client1", 1, 30, 0, 1392100133, "DE",
				48.3972222f, 10.4388889f)
				&& graphity.createUser(2, "Client2", 1, 34, 0, 1392101033,
						"DE", 51.2811111f, 9.6186111f)
				&& graphity.createUser(3, "Client3", 1, 31, 0, 1392101133,
						"DE", 48.6666667f, 9.2166667f)
				&& graphity.createUser(4, "Escort1", 2, 30, 0, 1392101133,
						"DE", 50.4666667f, 7.7333333f)
				&& graphity.createUser(5, "Escort2", 2, 78, 0, 1392001133,
						"DE", 52.5166667f, 10.6333333f)
				&& graphity.createUser(6, "Escort3", 2, 30, 0, 1392001133,
						"DE", 52.2666667f, 10.2f)
				&& graphity.createUser(7, "Company1", 3, 50, 0, 1391101133,
						"DE", 47.5907676194931f, 8.12953948974609f)
				&& graphity.createUser(8, "Company2", 3, 40, 0, 1391101133,
						"DE", 52.25f, 9.9666667f)
				&& graphity.createUser(9, "Company3", 3, 30, 0, 1392100133,
						"DE", 49.9738889f, 9.1491667f)
				&& graphity.createUser(10, "ClientPaidMembership", 1, 18, 1,
						1382101133, "DE", 48.3972222f, 10.4388889f)
				&& graphity.createUser(11, "EscortPaidMembership", 2, 32, 1,
						1392101133, "DE", 48.3972222f, 10.4388889f) && graphity
					.createUser(12, "CompanyPaidMembership", 3, 19, 1,
							1392101133, "DE", 48.3972222f, 10.4388889f))) {
			System.err.println("failed to create test users!");
		}

		/**
		 * 1 be a friend of 3,4,5;
		 * 
		 * 2 be a friend of 3,4,5;
		 * 
		 * 3 create an event;
		 * 
		 * 4 post a blog;
		 * 
		 * 5 write a comment for 4's blog
		 * 
		 * 6 upgrade her membership;
		 * 
		 * 1 upgrade his membership;
		 */
		if (graphity.beFriend(1, 3) != 0 || graphity.beFriend(1, 4) != 0
				|| graphity.beFriend(1, 5) != 0) {
			System.err.println("1 failed to be friend with user 3, 4 and 5!");
		}
		if (graphity.beFriend(2, 3) != 0 || graphity.beFriend(2, 4) != 0
				|| graphity.beFriend(2, 5) != 0) {
			System.err.println("2 failed to be friend with user 3, 4 and 5!");
		}
		if (graphity
				.createEvent(
						1,
						"event subject",
						"The interactive shell also features tab completion for functions, "
								+ "constants, class names, variables, static method calls and class "
								+ "constants.Example #2 Tab completionPressing the tab key twice when "
								+ "there are multiple possible completions will result in a list of these "
								+ "completions: ", 1392103766, 3) != 0) {
			System.err.println("failed to createEvent");
		}

		if (graphity
				.createBlog(
						1,
						"Blog subject",
						"I'm developing a Java package that makes basic "
								+ "HTTP requests (GET, POST, PUT, and DELETE). Right now, I'm having it just print "
								+ "the output of the request. I would like to store it in a field, but I'm not sure"
								+ " if String supports large amounts of text. Is there a data type for large amounts"
								+ " of text, or is there a reasonable alternative to it? Right now, because I'm just"
								+ " printing it, I can't do anything with the data that is returned (like parse it, "
								+ "if it's JSON).", 1392103766, 4) != 0) {
			System.err.println("failed to createBlog");
		}

		if (graphity.commentBlog(1, 1, "If you are performing a single set of "
				+ "operations on the data, you can stream it through a "
				+ "pipeline and not even store the entire data in memory "
				+ "at any time. It can also boost performance as work can "
				+ "begin upon the first character rather than after the last "
				+ "is received. Check out CharSequence.", 1392103776, 5) != 0) {
			System.err.println("failed to commentBlog");
		}

		if (graphity.upgradeMembership(6, 1) != 0) {
			System.err.println("failed to upgradeMembership");
		}

		if (graphity.upgradeMembership(7, 2) != 0) {
			System.err.println("failed to upgradeMembership");
		}

		if (graphity.upgradeMembership(1, 3) != 0) {
			System.err.println("failed to upgradeMembership");
		}

		Iterable<Vertex> users = g.getVertices(Property.User.ID, 1L);
		System.out.println(users.iterator().next()
				.getVertices(Direction.IN, EdgeType.FOLLOW).iterator().next()
				.getProperty(Property.User.NAME)
				+ " stalks.");
	}
}