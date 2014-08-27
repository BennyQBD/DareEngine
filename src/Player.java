import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends SpriteObject
{
	private static final long SECOND = 1000000000L;
	private static final long DISABLE_LENGTH = 3 * SECOND;
	private long                    m_disableTime;
	private boolean                 m_isActive;
	private final ArrayList<Player> m_children;
	private final ArrayList<Player> m_childrenToRemove;

	public void AddChildrenToArray(ArrayList<Entity> dest)
	{
		for(int i = 0; i < m_children.size(); i++)
		{
			m_children.get(i).SetIsActive(false);
			m_children.get(i).AddChildrenToArray(dest);
			dest.add(m_children.get(i));
		}
		m_children.clear();
		m_childrenToRemove.clear();
	}

	private void Print()
	{
		System.out.println(this);
		System.out.println(">>");
		for(int i = 0; i < m_children.size(); i++)
		{
			m_children.get(i).Print();
		}
		System.out.println("<<");
	}

	public void AddChild(Player other)
	{
		if(ContainsObject(other))
		{
			System.out.println("Adding existing object");
			new Exception().printStackTrace();
			Print();
			System.out.println();
			other.Print();
			System.exit(1);
		}
		m_children.add(other);
	}

	public void RemoveChild(Player other)
	{
		m_childrenToRemove.add(other);
	}

	public boolean ContainsObject(Player other)
	{
		if(this == other)
		{
			return true;
		}

		for(int i = 0; i < m_children.size(); i++)
		{
			if(m_children.get(i).ContainsObject(other))
			{
				return true;
			}
		}

		return false;
	}

	public Player CheckCollisionRecursive(Player other)
	{
		if(this != other && SphereIntersect(other))
		{
			return this;
		}

		for(int i = 0; i < m_children.size(); i++)
		{
			Player result = m_children.get(i)
				.CheckCollisionRecursive(other);
			if(result != null)
			{
				return result;
			}
		}

		return null;
	}

	public Player CheckLaserCollision(Entity other)
	{
		float startX = GetX();
		float startY = GetY();
		for(int i = 0; i < m_children.size(); i++)
		{	
			if(!m_children.get(i).GetIsActive())
			{
				continue;
			}

			float endX = m_children.get(i).GetX();
			float endY = m_children.get(i).GetY();
			
			if(other.LineIntersect(startX, startY, endX, endY))
			{
				return this;
			}

			Player result = 
				m_children.get(i).CheckLaserCollision(other);
			if(result != null)
			{
				return result;
			}
		}

		return null;
	}

	public boolean GetIsActive() { return m_isActive; }
	public void SetIsActive(boolean val) 
	{ 
		if(val == true)
		{
			if(System.nanoTime() >= 
				(m_disableTime + DISABLE_LENGTH))
			{
				m_isActive = val;
			}
		}
		else
		{
			m_isActive = val;
			m_disableTime = System.nanoTime();

		}
	}
	
	public Player(boolean isActive, float x, float y, 
			float width, float height)
	{
		super(x, y, width, height,
				new Bitmap(100, 100)
				.ClearScreen((byte)0x00, (byte)0x00, 
					(byte)0x00, (byte)0xFF));
		m_isActive = isActive;
		m_children = new ArrayList<Player>();
		m_childrenToRemove = new ArrayList<Player>();
		m_disableTime = 0;
	}

	@Override
	public void Update(Input input, float delta)
	{
		if(m_isActive)
		{
			float speed = 1.0f;
			if(input.GetKey(KeyEvent.VK_W))
			{
				SetY(GetY() - delta * speed);
			}
			if(input.GetKey(KeyEvent.VK_S))
			{
				SetY(GetY() + delta * speed);
			}
			if(input.GetKey(KeyEvent.VK_D))
			{
				SetX(GetX() + delta * speed);
			}
			if(input.GetKey(KeyEvent.VK_A))
			{
				SetX(GetX() - delta * speed);
			}

			for(int i = 0; i < m_children.size(); i++)
			{
				m_children.get(i).Update(input, delta);
			}
		}
		
		for(int i = 0; i < m_childrenToRemove.size(); i++)
		{
			m_children.remove(m_childrenToRemove.get(i));
		}
		m_childrenToRemove.clear();
	}

	@Override
	public void Render(RenderContext target)
	{
		if(m_isActive)
		{
			for(int i = 0; i < m_children.size(); i++)
			{
				if(m_children.get(i).GetIsActive())
				{
					target.DrawLine(GetX(), GetY(),
							m_children.get(i).GetX(), 
							m_children.get(i).GetY(),
						(byte)0x00, 
						(byte)0x79, (byte)0xbf, (byte)0x10);
				}
			}
		}
		
		for(int i = 0; i < m_children.size(); i++)
		{
			try
			{
				m_children.get(i).Render(target);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
		super.Render(target);
	}
}
