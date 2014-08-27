import java.util.*;

public class QuadTree
{
	private QuadTree m_nodes[];
	private AABB     m_aabb;
	private Entity   m_entity;

//	public void GetEntity()
//	{
//		return m_entity;
//	}

	public void Print()
	{
		Print(0, "NW");
	}

	private void Print(int depth, String location)
	{
		String prefix = "";
		for(int i = 0; i < depth; i++)
		{
			prefix += "-";
		}
		System.out.println(prefix + location + " " + m_entity);
		if(m_nodes[0] != null) { m_nodes[0].Print(depth + 1, "NW"); }
		if(m_nodes[1] != null) { m_nodes[1].Print(depth + 1, "NE"); }
		if(m_nodes[2] != null) { m_nodes[2].Print(depth + 1, "SW"); }
		if(m_nodes[3] != null) { m_nodes[3].Print(depth + 1, "SE"); }
	}

	public QuadTree(Entity entity, AABB aabb)
	{
		m_nodes  = new QuadTree[4];
		m_entity = entity;
		m_aabb   = aabb;
	}

	public void Add(Entity entity)
	{
		if(entity.GetAABB().IntersectAABB(m_aabb))
		{
			AddToChild(entity);
		}
		else
		{
			System.err.println("Error: AABB not in quad tree!");
			System.exit(1);
		}
	}

	public Set<Entity> QueryRange(AABB aabb)
	{
		Set<Entity> result = new HashSet<Entity>();

		QueryRangeInternal(aabb, result);	

		return result;
	}

	private Set<Entity> QueryRangeInternal(AABB aabb, 
		Set<Entity> result)
	{
		if(!aabb.IntersectAABB(m_aabb))
		{
			return result;
		}

		if(m_entity.GetAABB().IntersectAABB(aabb))
		{
			result.add(m_entity);
		}

		for(int i = 0; i < 4; i++)
		{
			if(m_nodes[i] != null)
			{
				m_nodes[i].QueryRangeInternal(aabb, result);
			}
		}

		return result;
	}

	private void TryToAddToChildNode(Entity entity, 
			float minX, float minY, float maxX, float maxY,
			int nodeIndex)
	{
		if(entity.GetAABB().IntersectRect(minX, minY, maxX, maxY))
		{
			if(m_nodes[nodeIndex] == null)
			{
				m_nodes[nodeIndex] = new QuadTree(entity, 
					new AABB(minX, minY, maxX, maxY));
			}
			else
			{
				m_nodes[nodeIndex].AddToChild(entity);
			}
		}
	}

	private void AddToChild(Entity entity)
	{
		float minX = m_aabb.GetMinX();
		float minY = m_aabb.GetMinY();
		float maxX = m_aabb.GetMaxX();
		float maxY = m_aabb.GetMaxY();

		float halfXLength = (maxX - minX)/2.0f;
		float halfYLength = (maxY - minY)/2.0f;

		minY += halfYLength;
		maxX -= halfXLength;

		TryToAddToChildNode(entity, minX, minY, maxX, maxY, 0);

		minX += halfXLength;
		maxX += halfXLength;

		TryToAddToChildNode(entity, minX, minY, maxX, maxY, 1);

		minY -= halfYLength;
		maxY -= halfYLength;

		TryToAddToChildNode(entity, minX, minY, maxX, maxY, 3);

		minX -= halfXLength;
		maxX -= halfXLength;

		TryToAddToChildNode(entity, minX, minY, maxX, maxY, 2);
	}
}
