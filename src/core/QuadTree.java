package core;

import java.util.*;
import physics.*;

public class QuadTree
{
	private QuadTree m_nodes[];
	private Entity   m_entities[];
	private int      m_numEntities;
	private AABB     m_aabb;
	
	public QuadTree(AABB aabb, int numChildrenPerNode)
	{
		m_nodes       = new QuadTree[4];
		m_entities    = new Entity[numChildrenPerNode];
		m_numEntities = 0;
		m_aabb        = aabb;
	}

	public QuadTree(QuadTree[] nodes, Entity[] entities, 
			int numEntities, AABB aabb)
	{
		m_nodes       = nodes;
		m_entities    = entities;
		m_numEntities = numEntities;
		m_aabb        = aabb;
	}

	public void Add(Entity entity)
	{
		if(entity.GetAABB().IntersectAABB(m_aabb))
		{
			if(m_numEntities < m_entities.length)
			{
				m_entities[m_numEntities] = entity;
				m_numEntities++;
			}
			else
			{
				AddToChild(entity);
			}
		}
		else
		{
			QuadTree thisAsNode = 
				new QuadTree(m_nodes, m_entities, m_numEntities, m_aabb);

			float dirX = entity.GetX() - m_aabb.GetCenterX();
			float dirY = entity.GetY() - m_aabb.GetCenterY();

			float minX = m_aabb.GetMinX();
			float minY = m_aabb.GetMinY();
			float maxX = m_aabb.GetMaxX();
			float maxY = m_aabb.GetMaxY();

			float expanseX = maxX - minX;
			float expanseY = maxY - minY;

			m_nodes = new QuadTree[4];
			m_numEntities = 0;
			m_entities = new Entity[m_entities.length];

			if(dirX <= 0 && dirY <= 0)
			{
				m_nodes[1] = thisAsNode;
				m_aabb = new AABB(
						minX - expanseX, minY - expanseY, 
						maxX, maxY);
			}
			else if(dirX <= 0 && dirY > 0)
			{
				m_nodes[3] = thisAsNode;
				m_aabb = new AABB(
						minX - expanseX, minY, 
						maxX, maxY + expanseY);

			}
			else if(dirX > 0 && dirY > 0)
			{
				m_nodes[2] = thisAsNode;
				m_aabb = new AABB(
						minX, minY, 
						maxX + expanseX, maxY + expanseY);
				
			}
			else if(dirX > 0 && dirY <= 0)
			{
				m_nodes[0] = thisAsNode;
				m_aabb = new AABB(
						minX, minY - expanseY, 
						maxX + expanseX, maxY);
			}
			else
			{
				System.err.println("Error: QuadTree direction is invalid (?): "
						+ dirX + " (dirX) " + dirY + " (dirY)");
				System.exit(1);
			}

			Add(entity);
			
//			System.err.println("Error: AABB not in quad tree!");
//			System.exit(1);
		}
	}

	public boolean Remove(Entity entity)
	{
		if(!entity.GetAABB().IntersectAABB(m_aabb))
		{
			return false;
		}

		for(int i = 0; i < m_numEntities; i++)
		{
			if(m_entities[i] == entity)
			{
				RemoveEntityFromList(i);				
			}
		}

		for(int i = 0; i < m_nodes.length; i++)
		{
			if(m_nodes[i] != null && m_nodes[i].Remove(entity))
			{
				m_nodes[i] = null;
			}
		}

		return IsThisNodeEmpty();
	}

	private boolean IsThisNodeEmpty()
	{
		for(int i = 0; i < m_nodes.length; i++)
		{
			if(m_nodes[i] != null)
			{
				return false;
			}
		}
		
		return m_numEntities == 0;	
	}

	private void RemoveEntityFromList(int index)
	{
		for(int i = index + 1; i < m_numEntities; i++)
		{
			m_entities[i - 1] = m_entities[i];
		}
		m_entities[m_numEntities - 1] = null;
		m_numEntities--;
	}

	public Set<Entity> QueryRange(AABB aabb)
	{
		Set<Entity> result = new HashSet<Entity>();

		QueryRangeInternal(aabb, result);	

		return result;
	}

	public Set<Entity> GetAll()
	{
		return QueryRange(m_aabb);
	}

	private Set<Entity> QueryRangeInternal(AABB aabb, 
		Set<Entity> result)
	{
		if(!aabb.IntersectAABB(m_aabb))
		{
			return result;
		}

		for(int i = 0; i < m_numEntities; i++)
		{
			if(m_entities[i].GetAABB().IntersectAABB(aabb))
			{
				result.add(m_entities[i]);
			}
		}

		for(int i = 0; i < m_nodes.length; i++)
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
				m_nodes[nodeIndex] = new QuadTree( 
					new AABB(minX, minY, maxX, maxY),
					m_entities.length);
			}

			m_nodes[nodeIndex].Add(entity);
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
		for(int i = 0; i < m_numEntities; i++)
		{
			System.out.println(prefix + location + " " 
					+ i + ": " + m_entities[i]);
		}
		if(m_nodes[0] != null) { m_nodes[0].Print(depth + 1, "NW"); }
		if(m_nodes[1] != null) { m_nodes[1].Print(depth + 1, "NE"); }
		if(m_nodes[2] != null) { m_nodes[2].Print(depth + 1, "SW"); }
		if(m_nodes[3] != null) { m_nodes[3].Print(depth + 1, "SE"); }
	}

}
