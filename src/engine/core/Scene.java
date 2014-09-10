package engine.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import engine.components.PhysicsComponent;
import engine.physics.AABB;
import engine.rendering.RenderContext;

public class Scene
{
	private QuadTree m_scene;

	public Scene()
	{
		m_scene = new QuadTree(new AABB(-1, -1, 1, 1), 8);
	}

	public void AddEntity(Entity entity)
	{
		m_scene.Add(entity);
	}

	public void RemoveEntity(Entity entity)
	{
		m_scene.Remove(entity);
	}

	public void Update(Input input, float delta)
	{
		Set<Entity> entities = 
			m_scene.QueryRange(new AABB(-4, -4, 4, 4), 
					new HashSet<Entity>());

		Iterator<Entity> it = entities.iterator();
		while(it.hasNext())
		{
			Entity current = (Entity)it.next();		
			
			float startX = current.GetX();
			float startY = current.GetY();

			current.Update(input, delta);

			if(startX != current.GetX() ||
			   startY != current.GetY())
			{
				RemoveEntity(current);
				current.UpdateAABB();
				HandleCollisions(current);
				AddEntity(current);
			}
		}
	}

	private void HandleCollisions(Entity current)
	{
		PhysicsComponent physicsComponent =(PhysicsComponent)
			current.GetComponent(PhysicsComponent.NAME);

		if(physicsComponent != null)
		{
			Set<Entity> collidingEntities = new HashSet<Entity>();
			m_scene.QueryRange(current.GetAABB(), collidingEntities);
			
			Iterator<Entity> it2 = collidingEntities.iterator();
			while(it2.hasNext())
			{
				Entity other = (Entity)it2.next();
				PhysicsComponent otherComponent =(PhysicsComponent)
					other.GetComponent(PhysicsComponent.NAME);

				if(otherComponent != null)
				{
					float distX = current.GetAABB().
						GetDistanceX(other.GetAABB());
					float distY = current.GetAABB().
						GetDistanceY(other.GetAABB());

					physicsComponent.OnCollision(otherComponent,
							distX, distY);
				}
			}
		}
	}

	public void Render(RenderContext target)
	{
		target.Clear((byte)0x00);

		Set<Entity> renderableEntities = 
			m_scene.QueryRange(target.GetRenderArea(), new TreeSet<Entity>());

		Iterator<Entity> it = renderableEntities.iterator();
		while(it.hasNext())
		{
			Entity current = (Entity)it.next();
			current.Render(target);
		}
	}
}
