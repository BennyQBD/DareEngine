/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.space;

import java.util.Set;

/**
 * A data structure that stores objects with a notion of space.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 *
 * @param <T> Some spatial object being stored in the structure.
 */
public interface ISpatialStructure<T extends ISpatialObject> {
	/**
	 * Adds a new object to the spatial structure
	 * 
	 * @param obj
	 *            The object to add.
	 */
	public void add(T obj);

	/**
	 * Removes an object from the spatial structure
	 * 
	 * @param obj
	 *            The object to remove.
	 */
	public void remove(T obj);

	/**
	 * Removes all objects from the spatial structure.
	 */
	public void clear();

	/**
	 * Returns a set of all objects in the spatial structure
	 * 
	 * @param result The set of objects to add the objects to.
	 * @return The set specified by {@code result}
	 */
	public Set<T> getAll(Set<T> result);

	/**
	 * Returns a set of all objects in a specific range of the spatial structure
	 * 
	 * @param result The set of objects to add the objects to.
	 * @param range The range of space being queried.
	 * @return The set specified by {@code result}
	 */
	public Set<T> queryRange(Set<T> result, AABB range);
}
