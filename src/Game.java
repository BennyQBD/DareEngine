import java.awt.event.KeyEvent;
import java.util.*;

public class Game
{
	private final ArrayList<Entity>  m_entities;
	private final ArrayList<Entity>  m_toRemove;
	private final ArrayList<Entity>  m_toAdd;

	private final Stars3D            m_stars;
	private float                    m_delta;
	private final Player             m_mainPlayer;

	public Game()
	{
		m_entities           = new ArrayList<Entity>();
		m_toRemove           = new ArrayList<Entity>();
		m_toAdd              = new ArrayList<Entity>();
		m_stars = new Stars3D(4096, 64.0f, 4.0f);

		m_mainPlayer = new Player(true, -1.0f, -1.0f,
					1.0f, 1.0f);
		m_entities.add(m_mainPlayer);

		for(int i = 0; i < 10; i++)
		{
			float x = (float)Math.random();
			float y = (float)Math.random();
			m_entities.add(new Player(false, x, y,
						0.5f, 0.5f));
		}
		for(int i = 0; i < 10; i++)
		{
			float x = (float)Math.random();
			float y = (float)-Math.random();
			m_entities.add(new DestroyableObject(x, y,
						0.5f, 0.5f));
		}
		for(int i = 0; i < 10; i++)
		{
			float x = (float)-Math.random();
			float y = (float)Math.random();
			m_entities.add(new BlockingObject(x, y,
						0.5f, 0.5f));
		}
	}

	public void Update(Input input, float delta)
	{
		if(input.GetKey(KeyEvent.VK_E))
		{
			if(m_mainPlayer.GetIsActive())
			{
				m_mainPlayer.SetIsActive(false);
			}
			else
			{
				m_mainPlayer.SetIsActive(true);
			}
		}

		m_delta = delta;
		for(int i = 0; i < m_entities.size(); i++)
		{
			m_entities.get(i).Update(input, delta);
		}

		for(int i = 0; i < m_entities.size(); i++)
		{
			if(m_entities.get(i) instanceof Player
			  && m_entities.get(i) != m_mainPlayer)
			{
				Player other = (Player)m_entities.get(i);
				Player result = m_mainPlayer
					.CheckCollisionRecursive(other);

				if(result != null)
				{
					other.SetIsActive(true);
					if(other.GetIsActive())
					{
						result.AddChild(other);
						m_toRemove.add(other);
					}
				}
			}

			if(m_entities.get(i) instanceof 
					DestroyableObject)
			{
				DestroyableObject current = 
					(DestroyableObject)m_entities.get(i);

				Player laserResult = 
					m_mainPlayer.CheckLaserCollision(current);

				if(laserResult != null)
				{
					m_toRemove.add(current);
				}
			}

			if(m_entities.get(i) instanceof 
					BlockingObject)
			{
				BlockingObject current = 
					(BlockingObject)m_entities.get(i);
				
				Player laserResult = 
					m_mainPlayer.CheckLaserCollision(current);

				if(laserResult != null && laserResult
						!= m_mainPlayer)
				{
					m_mainPlayer.RemoveChild(laserResult);
					laserResult.SetIsActive(false);
					laserResult.AddChildrenToArray(m_toAdd);
					m_toAdd.add(laserResult);
				}
			}
		}

		for(int i = 0; i < m_toRemove.size(); i++)
		{
			m_entities.remove(m_toRemove.get(i));
		}
		m_toRemove.clear();

		for(int i = 0; i < m_toAdd.size(); i++)
		{
			m_entities.add(m_toAdd.get(i));
		}
		m_toAdd.clear();
	}

	public void Render(RenderContext target)
	{
		m_stars.UpdateAndRender(target, m_delta);
		//target.Clear((byte)0x00);

		for(int i = 0; i < m_entities.size(); i++)
		{
			m_entities.get(i).Render(target);
		}
		
		int clampEnd = target.GetWidth() < target.GetHeight() ? 
			target.GetWidth() :
			target.GetHeight();
		int clampStartY = (clampEnd - target.GetHeight()) / -2;
		int clampStartX = (clampEnd - target.GetWidth()) / -2;
		int clampEndX = clampEnd + clampStartX;
		int clampEndY = clampEnd + clampStartY;

		target.FillRect(0, 0, clampStartX, target.GetHeight(), 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00);
		target.FillRect(clampEndX, 0, target.GetWidth() - clampEndX, target.GetHeight(), 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00);
	}
}
