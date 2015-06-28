package engine.components;

import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.util.IDAssigner;

public abstract class RemoveComponent extends EntityComponent {
	public static final int ID = IDAssigner.getId();

	private boolean activated;
	
	public RemoveComponent(Entity entity) {
		super(entity, ID);
		this.activated = false;
	}
	
	public abstract void onActivate();
	public abstract void removeUpdate(double delta);

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
