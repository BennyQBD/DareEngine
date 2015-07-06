/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.components;

import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.space.AABB;
import engine.util.IDAssigner;

/**
 * Gives an object a collider for spatial interaction. Note that a collider
 * doesn't necessarily need to be used for collision; a collider component can
 * be used for any spatial interaction. For example, a checkpoint can use a
 * ColliderComponent to detect when the player has reached it.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class ColliderComponent extends EntityComponent {
	public static final int ID = IDAssigner.getId();
	private AABB aabb;

	/**
	 * Creates a new ColliderComponent
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 */
	public ColliderComponent(Entity entity) {
		super(entity, ID);
		this.aabb = null;
	}

	/**
	 * Ensures the bounds of the collider are at least big enough to contain
	 * {@code other}.
	 * 
	 * @param other
	 *            The AABB this collider must be able to contain.
	 */
	public void fitAABB(AABB other) {
		if (aabb == null) {
			aabb = other;
		} else {
			aabb = aabb.combine(other);
		}
		getEntity().fitAABB(aabb);
	}

	/**
	 * Gets the AABB representing the collision range.
	 * @return An AABB representing the collision range.
	 */
	public AABB getAABB() {
		return getEntity().translateAABB(aabb);
	}

}
