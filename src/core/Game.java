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
				new AABB(-1, -1, 1, 1)
				//new AABB(-0.1f, -0.1f, 0.1f, 0.1f)
				, 1);

		Bitmap test = new Bitmap(20, 20);//new Bitmap("./res/bricks.jpg");//new Bitmap(50,50);

		for(int j = 0; j < 20; j++)
		{
			for(int i = 0; i < 20; i++)
			{
				test.DrawPixel(i, j, 
						(byte)(Math.random() * 255), 
						(byte)(Math.random() * 255),
						(byte)(Math.random() * 255), 
						(byte)(Math.random() * 255));
			}
		}

		//test.ClearScreen((byte)0x00, (byte)0x75, (byte)0x11, (byte)0x82);

		Entity test1 = new Entity(-0.1f, -0.1f, 0.1f, 0.1f);
		Entity test2 = new Entity(-0.3f, -0.3f, -0.2f, -0.2f);
		Entity test3 = new Entity(-1.1f, -1.1f, -0.9f, -0.9f);

		test1.AddComponent(new TestComponent(test));
		test2.AddComponent(new TestComponent(test));
		test3.AddComponent(new TestComponent2(test));

		m_scene.Add(test1);
		m_scene.Add(test2);
		m_scene.Add(test3);
//		m_scene.Remove(test1);
//		m_scene.Add(test1);

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

		int numIterations = 0;
		Iterator it = renderableEntities.iterator();
		while(it.hasNext())
		{
			Entity current = (Entity)it.next();
			current.Render(target);
			numIterations++;
		}

		//System.out.println(numIterations);
	}
}
