package engine.components;

import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.space.AABB;
import engine.util.IDAssigner;

public class ColliderComponent extends EntityComponent {
	public static final int ID = IDAssigner.getId();
	private AABB aabb;
	
	public ColliderComponent(Entity entity) {
		super(entity, ID);
		this.aabb = null;
	}
	
	public void fitAABB(AABB other) {
		if(aabb == null) {
			aabb = other;
		} else {
			aabb = aabb.combine(other);
		}
		getEntity().fitAABB(aabb);
	}
	
	public AABB getAABB() {
		return getEntity().translateAABB(aabb);
	}

}
