/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.space;

/**
 * Represents an object that has some notion of space, and can be stored in a
 * {@link ISpatialStructure}.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public interface ISpatialObject {
	public AABB getAABB();
}
