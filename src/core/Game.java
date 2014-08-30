package core;

import java.awt.event.KeyEvent;
import java.util.*;
import physics.*;
import rendering.*;

public class Game
{
	private QuadTree m_scene;
	private List<Entity> m_entities;

	public Game()
	{
		m_scene = new QuadTree(
				new AABB(-1, -1, 1, 1)
				//new AABB(-0.1f, -0.1f, 0.1f, 0.1f)
				, 16);
		m_entities = new ArrayList<Entity>();

		Bitmap test = new Bitmap(20, 20);//new Bitmap("./res/bricks.jpg");//new Bitmap(50,50);

		for(int j = 0; j < test.GetHeight(); j++)
		{
			for(int i = 0; i < test.GetWidth(); i++)
			{
				test.DrawPixel(i, j, 
						(byte)(Math.random() * 255), 
						(byte)(Math.random() * 255),
						(byte)(Math.random() * 255), 
						(byte)(Math.random() * 255));
			}
		}
//		Bitmap test = new Bitmap("./res/bricks.jpg");

		for(int i = 0; i < 500; i++)
		{
			float val = (float)i * 0.001f;
			AddEntity(new Entity(
						-0.1f + val, -0.1f + val,
						0.1f + val, 0.1f + val)
					.AddComponent(new TestComponent(test)));
		}

		//test.ClearScreen((byte)0x00, (byte)0x75, (byte)0x11, (byte)0x82);

//		Entity test1 = new Entity(-0.1f, -0.1f, 0.1f, 0.1f);
//		Entity test2 = new Entity(-0.3f, -0.3f, -0.2f, -0.2f);
		Entity test3 = new Entity(-1.1f, -1.1f, -0.9f, -0.9f);

//		test1.AddComponent(new TestComponent(test));
//		test2.AddComponent(new TestComponent(test));
		test3.AddComponent(new TestComponent2(test));

		AddEntity(test3);
	}

	public void AddEntity(Entity entity)
	{
		//m_scene.Add(entity);
		m_entities.add(entity);
	}

	public void RemoveEntity(Entity entity)
	{
		//m_scene.Remove(entity);
		m_entities.remove(entity);
	}

	public void Update(Input input, float delta)
	{
//		Set<Entity> entities = m_scene.GetAll();
//
//		Iterator it = entities.iterator();
//		while(it.hasNext())
//		{
//			Entity current = (Entity)it.next();		
		for(int i = 0; i < m_entities.size(); i++)
		{
			Entity current = m_entities.get(i);	
			
			float startX = current.GetX();
			float startY = current.GetY();

			current.Update(input, delta);

			if(startX != current.GetX() ||
			   startY != current.GetY())
			{
//				RemoveEntity(current);
				current.UpdateAABB();
//				AddEntity(current);
			}
		}
	}

	public void Render(RenderContext target)
	{
		target.Clear((byte)0x00);
		for(int i = 0; i < m_entities.size(); i++)
		{
			m_entities.get(i).Render(target);
		}

//		Set<Entity> renderableEntities = 
//			m_scene.QueryRange(new AABB(-1, -1, 1, 1));
//
//		int numIterations = 0;
//		Iterator it = renderableEntities.iterator();
//		while(it.hasNext())
//		{
//			Entity current = (Entity)it.next();
//			current.Render(target);
//			numIterations++;
//		}

		//System.out.println(numIterations);
	}
}
