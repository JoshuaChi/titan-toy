package com.thenetcircle.newsfeed.core;

import java.util.List;

import com.thenetcircle.newsfeed.exceptions.NewsfeedException;
import com.tinkerpop.blueprints.Vertex;

public interface INewsfeedCore {

	List<Vertex> filter(Criteria c) throws NewsfeedException;
}
