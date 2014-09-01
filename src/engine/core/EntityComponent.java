package engine.core;

import engine.rendering.*;

public abstract class EntityComponent
{
	private Entity m_entity;
	private String m_name;

	public EntityComponent(String name)
	{
		m_name = name;
	}

	public String GetName() { return m_name; }

	public Entity GetEntity() { return m_entity; }
	public void SetEntity(Entity entity) { m_entity = entity; }
	
	public void OnAdd() {}
	public void Update(Input input, float delta) {}
	public void Render(RenderContext target) {}
}
