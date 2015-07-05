/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.core.entity;

import engine.rendering.IRenderContext;

/**
 * Base class for all components that can be attached to entities.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public abstract class EntityComponent {
	private Entity entity;
	private int id;

	/**
	 * Creates a component attached to a specific entity.
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 * @param id
	 *            The id identifying the type of component. This should be
	 *            unique to the subclass, but not unique to the object.
	 */
	public EntityComponent(Entity entity, int id) {
		this.id = id;
		this.entity = entity;
		entity.add(this);
	}

	/**
	 * Gets the id of this component.
	 * 
	 * @return The id of this component.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the entity this is attached to.
	 * 
	 * @return The entity this is attached to.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Updates this component.
	 * 
	 * @param delta
	 *            How much time has passed since the last update.
	 */
	public void update(double delta) {
	}

	/**
	 * Renders this component.
	 * 
	 * @param target
	 *            The context being used for rendering
	 * @param viewportX
	 *            The location of the viewport on X.
	 * @param viewportY
	 *            The location of the viewport on Y.
	 */
	public void render(IRenderContext target, double viewportX, double viewportY) {
	}
}
