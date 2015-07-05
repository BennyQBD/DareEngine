/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.core.entity;

/**
 * Defines a function to be called when visiting entities.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public interface IEntityVisitor {
	/**
	 * A function that will be called every time an entity is visited.
	 * 
	 * @param entity
	 *            The entity being visited.
	 * @param component
	 *            The component of the entity being visited, if relevant, or
	 *            null otherwise.
	 */
	public void visit(Entity entity, EntityComponent component);
}
