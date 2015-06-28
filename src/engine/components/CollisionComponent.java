package engine.components;

import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.core.entity.IEntityVisitor;
import engine.space.AABB;
import engine.util.DoublePair;
import engine.util.IDAssigner;

public class CollisionComponent extends EntityComponent {
	public static final int ID = IDAssigner.getId();

	public CollisionComponent(Entity entity) {
		super(entity, ID);
	}

	public DoublePair resolveCollisions(double amtX, double amtY) {
		final DoublePair amts = new DoublePair(amtX, amtY);
		ColliderComponent c = (ColliderComponent) getEntity().getComponent(
				ColliderComponent.ID);
		if (c == null) {
			return amts;
		}
		final AABB collider = c.getAABB();
		final AABB collisionRange = getEntity().getAABB().stretch(amtX, amtY);
		getEntity().visitInRange(CollisionComponent.ID, collisionRange,
				new IEntityVisitor() {
					@Override
					public void visit(Entity entity, EntityComponent component) {
						if (entity == getEntity()) {
							return;
						}
						ColliderComponent c2 = (ColliderComponent) entity
								.getComponent(ColliderComponent.ID);
						AABB collider2 = c2 != null ? c2.getAABB() : entity
								.getAABB();

						if (entity.getAABB().intersects(collisionRange)) {
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
