package engine.physics;

public class AABB
{
	private float m_minX;
	private float m_minY;
	private float m_maxX;
	private float m_maxY;

	public float GetMinX() { return m_minX; }
	public float GetMinY() { return m_minY; }
	public float GetMaxX() { return m_maxX; }
	public float GetMaxY() { return m_maxY; }

	public float GetCenterX()
	{
		return (m_minX + m_maxX)/2.0f;
	}

	public float GetCenterY()
	{
		return (m_minY + m_maxY)/2.0f;
	}

	public AABB(float minX, float minY, float maxX, float maxY)
	{
		m_minX = minX;
		m_minY = minY;
		m_maxX = maxX;
		m_maxY = maxY;
	}

	public boolean IntersectAABB(AABB other)
	{
		return IntersectRect(other.GetMinX(), 
				other.GetMinY(), other.GetMaxX(), other.GetMaxY());
	}

	public boolean IntersectRect(float minX, float minY, 
			float maxX, float maxY)
	{
		return m_minX < maxX && m_maxX > minX &&
		   m_minY < maxY && m_maxY > minY;
	}

	public float GetDistanceX(AABB other)
	{
		float distance1 = other.GetMinX() - GetMaxX();
		float distance2 = GetMinX() - other.GetMaxX(); 
		
		float distance = distance1 > distance2 ? distance1 : distance2;

		return distance;
	}
	
	public float GetDistanceY(AABB other)
	{
		float distance1 = other.GetMinY() - GetMaxY();
		float distance2 = GetMinY() - other.GetMaxY(); 
		
		float distance = distance1 > distance2 ? distance1 : distance2;

		return distance;
	}

}
