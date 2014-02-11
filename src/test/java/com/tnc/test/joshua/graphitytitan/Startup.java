package com.tnc.test.joshua.graphitytitan;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tnc.joshua.graphitytitan.GraphityTitanWriteOptimized;
import com.tnc.joshua.socialgraph.EdgeType;
import com.tnc.joshua.socialgraph.Property;
import com.tnc.joshua.socialgraph.SocialGraph;

public class Startup {
	public static void main(String[] args) {
		Configuration conf = new BaseConfiguration();
//		conf.setProperty("storage.directory", "/tmp/titan2");
		conf.setProperty("storage.backend", "cassandra");
		conf.setProperty("storage.hostname", "127.0.0.1");
		conf.setProperty("storage.cassandra-config-dir",
				"config/cassandra.yaml");
		conf.setProperty("storage.index.search.backend", "elasticsearch");
		conf.setProperty("storage.index.search.directory", "/tmp/searchindex");
		conf.setProperty("storage.index.search.client-only", "false");
		conf.setProperty("storage.index.search.local-mode", "true");

		TitanGraph g = TitanFactory.open(conf);
		SocialGraph graphity = new GraphityTitanWriteOptimized(g);

		if (!(graphity.createUser(1, "Testy") && graphity.createUser(2,
				"Stalky"))) {
			System.err.println("failed to create test users!");
		}
		if (graphity.follow(2, 1) != 0) {
			System.err.println("failed to create follow edge!");
		}

		Iterable<Vertex> users = g.getVertices(Property.User.ID, 1L);
		System.out.println(users.iterator().next()
				.getVertices(Direction.IN, EdgeType.FOLLOW).iterator().next()
				.getProperty(Property.User.NAME)
				+ " stalks.");
	}
}