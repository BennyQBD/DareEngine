package engine.core;

import engine.rendering.*;

public abstract class EntityComponent
{
	private Entity m_entity;

	public Entity GetEntity() { return m_entity; }
	public void SetEntity(Entity entity) { m_entity = entity; }
	
	public void OnAdd() {}
	public void Update(Input input, float delta) {}
	public void Render(RenderContext target) {}
}
