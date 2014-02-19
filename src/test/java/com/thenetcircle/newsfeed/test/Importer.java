package com.thenetcircle.newsfeed.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import au.com.bytecode.opencsv.CSVReader;

import com.thenetcircle.newsfeed.EdgeType;
import com.thenetcircle.newsfeed.IDConstant;
import com.thenetcircle.newsfeed.Property;
import com.thenetcircle.newsfeed.Property.Activity;
import com.thenetcircle.newsfeed.impl.NewsfeedOperationImpl;
import com.thinkaurelius.titan.core.Order;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanKey;
import com.thinkaurelius.titan.core.attribute.Geoshape;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.ElementHelper;
import com.tinkerpop.blueprints.util.wrappers.batch.BatchGraph;
import com.tinkerpop.blueprints.util.wrappers.batch.VertexIDType;

/**
 * @user sql - select users.id, nickname, gender, FLOOR(18 + (RAND() * 40)) as
 *       age, membership, last_login, geo.country_iso, geo.lat, geo.lon from
 *       users_account users left join users_geo geo on users.id=geo.id limit 1
 * 
 * @event_user sql - select id, uid, name, unix_timestamp(created_at) as
 *             timestamp from event;
 * 
 * @authrized_favourites sql - select fav.sid, fav.tid from favourites fav left
 *                       join users_account ff on ff.id=fav.sid left join
 *                       users_account tt on tt.id=fav.tid where fav.authorized
 *                       = 1 and ff.id is not null and tt.id is not null;
 * 
 * @authrized_favourites sql - select fav.sid, fav.tid from favourites fav left
 *                       join users_account ff on ff.id=fav.sid left join
 *                       users_account tt on tt.id=fav.tid where fav.authorized
 *                       = 0 and ff.id is not null and tt.id is not null;
 * @author jchi
 * 
 */
public class Importer {
	public static void main(String[] args) {
		String workingDir = System.getProperty("user.dir")
				+ "/src/test/java/com/thenetcircle/newsfeed/test/";

		Boolean step1NotDone = false;
		Boolean step2NotDone = false;
		Boolean step3NotDone = true;
		Boolean step4NotDone = true;

		Configuration conf = new BaseConfiguration();
		conf.setProperty("storage.backend", "cassandra");
		conf.setProperty("storage.keyspace", "titan_prod6");
		conf.setProperty("storage.hostname", "127.0.0.1");
		conf.setProperty("storage.cassandra-config-dir",
				"config/cassandra.yaml");
		conf.setProperty("storage.index.search.backend", "elasticsearch");
		conf.setProperty("storage.index.search.directory",
				"/tmp/searchindex_prod6");
		conf.setProperty("storage.index.search.client-only", "false");
		conf.setProperty("storage.index.search.local-mode", "true");

		TitanGraph g = TitanFactory.open(conf);

		if (step1NotDone) {
			g.makeKey(Property.User.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();
			g.makeKey(Property.User.NAME).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class)
					.indexed("search", Edge.class).make();
			g.makeKey(Property.User.GEO).dataType(Geoshape.class)
					.indexed("search", Edge.class).make();

			// blog index
			g.makeKey(Property.Event.ID).dataType(String.class)
					.indexed(Vertex.class).indexed(Edge.class).unique()
					.indexed("search", Vertex.class).make();

			// edge sort index
			TitanKey time = g.makeKey(Property.Time.TIMESTAMP)
					.dataType(Integer.class).make();
			g.makeLabel(EdgeType.BEFRIEND).sortKey(time).sortOrder(Order.DESC)
					.make();
			g.makeLabel(EdgeType.USER_ACTIVITY).sortKey(time)
					.sortOrder(Order.DESC).make();
			g.makeLabel(EdgeType.BLOG_COMMENT).sortKey(time)
					.sortOrder(Order.DESC).make();
			g.commit();
		}

		NewsfeedOperationImpl graphity = new NewsfeedOperationImpl(g);

		BatchGraph bg = new BatchGraph(g, VertexIDType.STRING, 1000);

		// Verify graph vertex
		//
		// Vertex u77 = g.getVertices(Property.User.NAME,
		// "AirpsLunt").iterator().next();
		// System.out.println(u77);
		// System.exit(-1);

		// start import all users
		CSVReader reader;

		if (step2NotDone) {
			String csvFile = workingDir + "all_users_acount.csv";
			try {
				reader = new CSVReader(new FileReader(csvFile));
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					Vertex user = bg.addVertex(IDConstant.user + nextLine[0]);
					HashMap<String, Object> map = new HashMap<String, Object>();

					map.put(Property.User.ID, nextLine[0]);
					map.put(Property.User.NAME, nextLine[1]);
					map.put(Property.User.GENDER, Integer.parseInt(nextLine[2]));
					map.put(Property.User.AGE, Integer.parseInt(nextLine[3]));
					map.put(Property.User.MEMBERSHIP,
							Integer.parseInt(nextLine[4]));
					map.put(Property.User.LASTLOGIN,
							Long.parseLong(nextLine[5]));
					map.put(Property.User.COUNTRY, nextLine[6]);
					System.out.println(nextLine[7].getClass().getName() + "-"
							+ nextLine[8]);
					if (nextLine[7].compareTo("NULL") != 0
							&& nextLine[8].compareTo("NULL") != 0) {
						map.put(Property.User.LAT,
								String.format("%.7f",
										Float.parseFloat(nextLine[7])));
						map.put(Property.User.LON,
								String.format("%.7f",
										Float.parseFloat(nextLine[8])));
						map.put(Property.User.GEO, Geoshape.point(
								Float.parseFloat(nextLine[7]),
								Float.parseFloat(nextLine[8])));
					}

					ElementHelper.setProperties(user, map);
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String csvFileAuthorizedFavourites = workingDir
				+ "authorized_favourites.csv";
		try {
			reader = new CSVReader(new FileReader(csvFileAuthorizedFavourites));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				Vertex v1 = bg.getVertex(IDConstant.user + nextLine[0]);

				Vertex v2 = bg.getVertex(IDConstant.user + nextLine[1]);
				System.out.println("a fav v1:"+v1+"-v2:"+v2+"-user id:"+nextLine[0]);
				if (v1 != null && v2 != null) {
					bg.addEdge(null, v1, v2, EdgeType.BEFAVOURITE);
				}

			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String csvFileNotAuthorizedFavourites = workingDir
				+ "not_authorized_favourites.csv";
		try {
			reader = new CSVReader(new FileReader(
					csvFileNotAuthorizedFavourites));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				Vertex v1 = bg.getVertex(IDConstant.user + nextLine[0]);
				Vertex v2 = bg.getVertex(IDConstant.user + nextLine[1]);
				System.out.println("n a fav v1:"+v1+"-v2:"+v2+"-user id:"+nextLine[0]);
				if (v1 != null && v2 != null) {
					bg.addEdge(null, v1, v2, EdgeType.BEFAVOURITE);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String csvFileEvents = workingDir + "events.csv";
		try {
			reader = new CSVReader(new FileReader(csvFileEvents));
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				System.out.println(nextLine[0]);
				// create event vertex
				Vertex event = bg.addVertex(IDConstant.event + nextLine[0]);
				HashMap<String, Object> map = new HashMap<String, Object>();

				map.put(Activity.TYPE, Activity.TYPE_EVENT);
				map.put(Property.Event.ID, nextLine[0]);
				map.put(Property.Event.SUBJECT, nextLine[2]);
				map.put(Property.Time.TIMESTAMP, nextLine[3]);
				if(ElementHelper.getProperties(event) == null){
					System.out.println("event id:"+nextLine[0]);
					ElementHelper.setProperties(event, map);
				}
				

				// create user event edge
				Vertex v1 = bg.getVertex(IDConstant.user + nextLine[1]);
				if(v1 != null) {
					bg.addEdge(null, v1, event, EdgeType.USER_ACTIVITY);
				}
				
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bg.commit();
	}
}
