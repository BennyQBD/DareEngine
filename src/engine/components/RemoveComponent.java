/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.components;

import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.util.IDAssigner;

/**
 * Performs some function when an entity is removed.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public abstract class RemoveComponent extends EntityComponent {
	public static final int ID = IDAssigner.getId();

	private boolean activated;

	/**
	 * Creates a new RemoveComponent
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 */
	public RemoveComponent(Entity entity) {
		super(entity, ID);
		this.activated = false;
	}

	/**
	 * Called when the entity is first removed.
	 */
	public abstract void onActivate();

	/**
	 * Called every update after the entity is removed.
	 * 
	 * @param delta
	 *            How much time has passed since the last update.
	 */
	public abstract void removeUpdate(double delta);

	/**
	 * Activates this component. Calls the onActivate function, and begins
	 * calling the removeUpdate function on every update.
	 */
	public void activate() {
		activated = true;
		onActivate();
	}

	@Override
	public void update(double delta) {
		if (!activated) {
			return;
		}
		removeUpdate(delta);
	}
}
