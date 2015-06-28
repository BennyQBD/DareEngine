package engine.core.entity;

import engine.rendering.IRenderContext;

public abstract class EntityComponent {
	private Entity entity;
	private int id;
	
	public EntityComponent(Entity entity, int id) {
		this.id = id;
		this.entity = entity;
		entity.add(this);
	}

	public int getId() {
		return id;
	}

	public Entity getEntity() {
		return entity;
	}

	public void update(double delta) {
	}

	public void render(IRenderContext target, double viewportX, double viewportY) {
	}
}
