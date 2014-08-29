public class Entity
{
	private float        m_x;
	private float        m_y;
	private AABB         m_aabb;
	
	public float GetX()    { return m_x; }
	public float GetY()    { return m_y; }
	public AABB GetAABB()  { return m_aabb; }
	
	public void SetX(float x)      { m_x = x; }
	public void SetY(float y)      { m_y = y; }
	public void SetAABB(AABB aabb) { m_aabb = aabb; }

	public Entity(float minX, float minY, float maxX, float maxY) 
	{
		m_aabb = new AABB(minX, minY, maxX, maxY);
		m_x = (minX + maxX)/2.0f;
		m_y = (minY + maxY)/2.0f;
	}

	public void Update(Input input, float delta)
	{
		
	}

	public void Render(RenderContext target)
	{
		//TODO: Temp code!
		target.FillRect(m_aabb.GetMinX(), m_aabb.GetMinY(),
				        m_aabb.GetMaxX(), m_aabb.GetMaxY(),
				(byte)0x00, 
				(byte)0x79, (byte)0xbf, (byte)0x10);
	}

	public boolean IntersectAABB(Entity other)
	{
		return m_aabb.IntersectAABB(other.GetAABB());
	}

//	public boolean SphereIntersect(Entity other)
//	{
//		float radiusDistance = m_radius + other.GetRadius();
//		float centerDistance = Util.VectorLength(other.GetX() - m_x, other.GetY() - m_y);
//
//		return (centerDistance - radiusDistance) < 0;
//	}
//
//	public boolean LineIntersect(float xStart, float yStart,
//			float xEnd, float yEnd)
//	{
//		float dirX = xEnd-xStart;
//		float dirY = yEnd-yStart;
//		float dirLength = Util.VectorLength(dirX, dirY);
//		dirX /= dirLength;
//		dirY /= dirLength;
//
//		float lX = m_x - xStart;
//		float lY = m_y - yStart;
//		float tca = lX * dirX + lY * dirY;
//
//		if(tca < 0)
//		{
//			return false;
//		}
//
//		float d2 = lX * lX + lY * lY - tca * tca;
//		float radius2 = m_radius * m_radius;
//		
//		if(d2 > radius2)
//		{
//			return false;
//		}
//
//		float thc = (float)Math.sqrt(radius2 - d2);
//		float tMax = tca - thc;
//		float tMin = tca + thc;
//
//		if(tMin > 0 && tMin < dirLength
//			&& tMax > 0 && tMax < dirLength)
//		{
//			return true;
//		}
//		
//		return false;
//	}
}
