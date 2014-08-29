package core;

import rendering.*;

public abstract class EntityComponent
{
	private Entity m_entity;

	public Entity GetEntity() { return m_entity; }
	public void SetEntity(Entity entity) { m_entity = entity; }
	
	public abstract void Update(Input input, float delta);
	public abstract void Render(RenderContext target);
}
