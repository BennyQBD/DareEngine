/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.components;

import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.core.entity.IEntityVisitor;
import engine.space.AABB;
import engine.util.DoublePair;
import engine.util.IDAssigner;

/**
 * Component that detects collision between two entities. Note that this
 * component requires that both entities have a ColliderComponent. Should one
 * entity not have a ColliderComponent, then no collisions will be detected,
 * because there is no collider to detect collisions against.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class CollisionComponent extends EntityComponent {
	public static final int ID = IDAssigner.getId();

	/**
	 * Creates a new CollisionComponent
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 */
	public CollisionComponent(Entity entity) {
		super(entity, ID);
	}

	/**
	 * Resolves collisions with any other CollisionComponents encountered.
	 *  
	 * @param amtX The amount attempting to be moved on X.
	 * @param amtY The amount attempting to be moved on Y.
	 * @return New amtX and amtY that will not cause collisions after movement.
	 */
	public DoublePair resolveCollisions(double amtX, double amtY) {
		final DoublePair amts = new DoublePair(amtX, amtY);
		ColliderComponent c = (ColliderComponent) getEntity().getComponent(
				ColliderComponent.ID);
		if (c == null) {
			return amts;
		}
		final AABB collider = c.getAABB();
		final AABB collisionRange = collider.stretch(amtX, amtY);
		getEntity().visitInRange(CollisionComponent.ID, collisionRange,
				new IEntityVisitor() {
					@Override
					public void visit(Entity entity, EntityComponent component) {
						if (entity == getEntity()) {
							return;
						}
						ColliderComponent c2 = (ColliderComponent) entity
								.getComponent(ColliderComponent.ID);
						if (c2 == null) {
							return;
						}
						AABB collider2 = c2.getAABB();

						if (collider2.intersects(collisionRange)) {
							amts.setVal1(collider.resolveCollisionX(collider2,
									amts.getVal1()));
							amts.setVal2(collider.resolveCollisionY(collider2,
									amts.getVal2()));
						}
					}
				});
		return amts;
	}
}
