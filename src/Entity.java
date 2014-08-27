public class Entity
{
	private float        m_x;
	private float        m_y;
	private float        m_radius;

	public float GetX() { return m_x; }
	public float GetY() { return m_y; }
	public float GetRadius() { return m_radius; }
	
	public void SetX(float x) { m_x = x; }
	public void SetY(float y) { m_y = y; }
	public void SetRadius(float radius) { m_radius = radius; }

	public Entity(float x, float y, float radius) 
	{
		m_x = x;
		m_y = y;
		m_radius = radius;
	}

	public void Update(Input input, float delta)
	{
		
	}

	public void Render(RenderContext target)
	{
	}

	public boolean SphereIntersect(Entity other)
	{
		float radiusDistance = m_radius + other.GetRadius();
		float centerDistance = Util.VectorLength(other.GetX() - m_x, other.GetY() - m_y);

		return (centerDistance - radiusDistance) < 0;
	}

	public boolean LineIntersect(float xStart, float yStart,
			float xEnd, float yEnd)
	{
		float dirX = xEnd-xStart;
		float dirY = yEnd-yStart;
		float dirLength = Util.VectorLength(dirX, dirY);
		dirX /= dirLength;
		dirY /= dirLength;

		float lX = m_x - xStart;
		float lY = m_y - yStart;
		float tca = lX * dirX + lY * dirY;

		if(tca < 0)
		{
			return false;
		}

		float d2 = lX * lX + lY * lY - tca * tca;
		float radius2 = m_radius * m_radius;
		
		if(d2 > radius2)
		{
			return false;
		}

		float thc = (float)Math.sqrt(radius2 - d2);
		float tMax = tca - thc;
		float tMin = tca + thc;

		if(tMin > 0 && tMin < dirLength
			&& tMax > 0 && tMax < dirLength)
		{
			return true;
		}
		
		return false;
	}
}
