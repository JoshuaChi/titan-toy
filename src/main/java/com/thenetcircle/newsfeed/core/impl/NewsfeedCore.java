/**
 * 
 */
package com.thenetcircle.newsfeed.core.impl;

import java.util.List;

import com.thenetcircle.newsfeed.core.Criteria;
import com.thenetcircle.newsfeed.core.INewsfeedCore;
import com.thenetcircle.newsfeed.exceptions.NewsfeedException;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author jchi
 *
 */
public class NewsfeedCore implements INewsfeedCore {

	/* (non-Javadoc)
	 * @see com.thenetcircle.newsfeed.core.INewsfeedCore#filter(com.thenetcircle.newsfeed.core.Criteria)
	 */
	@Override
	public List<Vertex> filter(Criteria c) throws NewsfeedException {
		// TODO Auto-generated method stub
		return null;
	}

}
