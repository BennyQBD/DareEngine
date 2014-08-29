package core;

import java.awt.event.KeyEvent;
import java.util.*;
import physics.*;
import rendering.*;

public class Game
{
	private QuadTree m_scene;

	public Game()
	{
		m_scene = new QuadTree(
				new AABB(-100, -100, 100, 100)
				//new AABB(-0.1f, -0.1f, 0.1f, 0.1f)
				, 1);

		Entity test1 = new Entity(-0.1f, -0.1f, 0.1f, 0.1f);
		Entity test2 = new Entity(-0.3f, -0.3f, -0.2f, -0.2f);
		Entity test3 = new Entity(-1.1f, -1.1f, -0.9f, -0.9f);

		m_scene.Add(test1);
		m_scene.Add(test2);
		m_scene.Add(test3);
		//m_scene.Remove(test1);
		//m_scene.Add(test1);

		m_scene.Print();
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
		Set<Entity> entities = m_scene.GetAll();

		Iterator it = entities.iterator();
		while(it.hasNext())
		{
			Entity current = (Entity)it.next();
			
			float startX = current.GetX();
			float startY = current.GetY();

			current.Update(input, delta);

			if(startX != current.GetX() ||
			   startY != current.GetY())
			{
				m_scene.Remove(current);
				current.UpdateAABB();
				m_scene.Add(current);
			}
		}
	}

	public void Render(RenderContext target)
	{
		target.Clear((byte)0x00);

		Set<Entity> renderableEntities = 
			m_scene.QueryRange(new AABB(-1, -1, 1, 1));

		Iterator it = renderableEntities.iterator();
		while(it.hasNext())
		{
			Entity current = (Entity)it.next();
			current.Render(target);
		}
	}
}
