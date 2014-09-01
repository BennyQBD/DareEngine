package engine.core;

import java.awt.event.KeyEvent;
import java.util.*;
import engine.physics.*;
import engine.rendering.*;
import engine.components.*;

public class Scene
{
	private QuadTree m_scene;

	public Scene()
	{
		m_scene = new QuadTree(
				new AABB(-1, -1, 1, 1)
				//new AABB(-0.1f, -0.1f, 0.1f, 0.1f)
				, 8);

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

		int numFrames = 12;
		Bitmap animationTest[] = new Bitmap[numFrames];
//		float animationTimes[] = new float[numFrames];
//		int animationNext[] = new int[numFrames];

		for(int k = 0; k < numFrames; k++)
		{
			Bitmap currentTest = new Bitmap(20, 20);
			for(int j = 0; j < currentTest.GetHeight(); j++)
			{
				for(int i = 0; i < currentTest.GetWidth(); i++)
				{
					currentTest.DrawPixel(i, j, 
							(byte)(Math.random() * 255), 
							(byte)(Math.random() * 255),
							(byte)(Math.random() * 255), 
							(byte)(Math.random() * 255));
				}
			}
			animationTest[k] = currentTest;
//			animationTimes[k] = 0.5f;
//			animationNext[k] = k + 1;
		}
//		animationNext[numFrames - 1] = 0;

//		Bitmap test = new Bitmap("./res/bricks.jpg");

		float range = 50.0f;
		for(int i = 0; i < 20000; i++)
		{
			float xLoc = ((float)Math.random()) * range * 2.0f - range;
			float yLoc = ((float)Math.random()) * range * 2.0f - range;
			//System.out.println(xLoc + ", " + yLoc);
			//float val = (float)i * 0.001f;
			AddEntity(new Entity(
//						xLoc, yLoc,
//						xLoc + 0.1f, yLoc + 0.1f)
						-0.1f + xLoc, -0.1f + yLoc,
						0.1f + xLoc, 0.1f + yLoc)
					.AddComponent(new SpriteComponent(test,
							   	RenderContext.TRANSPARENCY_FULL, (float)i))
					.AddComponent(new PhysicsComponent(0.0f, 0.0f)));
		}

		//test.ClearScreen((byte)0x00, (byte)0x75, (byte)0x11, (byte)0x82);

//		Entity test1 = new Entity(-0.1f, -0.1f, 0.1f, 0.1f);
//		Entity test2 = new Entity(-0.3f, -0.3f, -0.2f, -0.2f);
		Entity test3 = new Entity(-1.1f, -1.1f, -0.9f, -0.9f);

//		test1.AddComponent(new TestComponent(test));
//		test2.AddComponent(new TestComponent(test));
		test3.AddComponent(new SpriteComponent(animationTest, 0.1f,
				RenderContext.TRANSPARENCY_FULL, -1.0f));
		test3.AddComponent(new PhysicsComponent(0.2f, 0.2f));


		AddEntity(test3);
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
			m_scene.QueryRange(new AABB(-2, -2, 2, 2), new HashSet<Entity>());
			//m_scene.GetAll();

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
			
			Iterator it2 = collidingEntities.iterator();
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
					otherComponent.OnCollision(physicsComponent,
							distX, distY);
				}
			}
		}
	}

	public void Render(RenderContext target)
	{
		target.Clear((byte)0x00);
		//target.SetCameraPosition(camX, camY);

		Set<Entity> renderableEntities = 
			m_scene.QueryRange(target.GetRenderArea(), new TreeSet<Entity>());

		Iterator it = renderableEntities.iterator();
		while(it.hasNext())
		{
			Entity current = (Entity)it.next();
			current.Render(target);
		}

		target.DrawString("Hello, World!", -0.5f, -0.5f, 32.0f/256.0f,
				(byte)0xF0, (byte)0xF0, (byte)0x00);
	}
}
