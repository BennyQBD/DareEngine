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
			
			float startX = current.getX();
			float startY = current.getY();

			current.update(input, delta);

			if(startX != current.getX() ||
			   startY != current.getY())
			{
				RemoveEntity(current);
				current.updateAABB();
				HandleCollisions(current);
				AddEntity(current);
			}
		}
	}

	private void HandleCollisions(Entity current)
	{
		PhysicsComponent physicsComponent =(PhysicsComponent)
			current.getComponent(PhysicsComponent.NAME);

		if(physicsComponent != null)
		{
			Set<Entity> collidingEntities = new HashSet<Entity>();
			m_scene.QueryRange(current.getAABB(), collidingEntities);
			
			Iterator<Entity> it2 = collidingEntities.iterator();
			while(it2.hasNext())
			{
				Entity other = (Entity)it2.next();
				PhysicsComponent otherComponent =(PhysicsComponent)
					other.getComponent(PhysicsComponent.NAME);

				if(otherComponent != null)
				{
					float distX = current.getAABB().
						GetDistanceX(other.getAABB());
					float distY = current.getAABB().
						GetDistanceY(other.getAABB());

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
			current.render(target);
		}
	}
}
