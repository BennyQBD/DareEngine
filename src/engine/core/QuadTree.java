/*
 * Copyright (c) 2014, Benny Bobaganoosh
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package engine.core;

import java.util.*;
import engine.physics.*;

/**
 * Represents a 2D space that can be recursively divided into 4 equal subspaces.
 * 
 * TODO: Make this generic so it can store more than just Entities.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class QuadTree {
	private QuadTree nodes[];
	private Entity entities[];
	private int numEntities;
	private AABB aabb;

	/**
	 * Initializes a QuadTree from an AABB.
	 * 
	 * @param aabb
	 *            Represents the 2D space inside the QuadTree
	 * @param numChildrenPerNode
	 *            The number of objects that can be added to each leaf of the
	 *            QuadTree.
	 */
	public QuadTree(AABB aabb, int numChildrenPerNode) {
		this.nodes = new QuadTree[4];
		this.entities = new Entity[numChildrenPerNode];
		this.numEntities = 0;
		this.aabb = aabb;
	}

	/**
	 * Copy constructor. Initializes this as a copy of another QuadTree, copying
	 * by reference.
	 * 
	 * @param other
	 *            The QuadTree to copy from
	 */
	private QuadTree(QuadTree other) {
		this.nodes = other.nodes;
		this.entities = other.entities;
		this.numEntities = other.numEntities;
		this.aabb = other.aabb;
	}

	/**
	 * Adds a new object to the QuadTree
	 * 
	 * @param entity
	 *            The object to add.
	 */
	public void add(Entity entity) {
		if (entity.getAABB().intersectAABB(aabb)) {
			if (numEntities < entities.length) {
				entities[numEntities] = entity;
				numEntities++;
			} else {
				addToChild(entity);
			}
		} else {
			QuadTree thisAsNode = new QuadTree(this);

			float dirX = entity.getX() - aabb.getCenterX();
			float dirY = entity.getY() - aabb.getCenterY();

			float minX = aabb.getMinX();
			float minY = aabb.getMinY();
			float maxX = aabb.getMaxX();
			float maxY = aabb.getMaxY();

			float expanseX = maxX - minX;
			float expanseY = maxY - minY;

			nodes = new QuadTree[4];
			numEntities = 0;
			entities = new Entity[entities.length];

			if (dirX <= 0 && dirY <= 0) {
				nodes[1] = thisAsNode;
				aabb = new AABB(minX - expanseX,
						minY - expanseY, maxX, maxY);
			} else if (dirX <= 0 && dirY > 0) {
				nodes[3] = thisAsNode;
				aabb = new AABB(minX - expanseX, minY, maxX,
						maxY + expanseY);

			} else if (dirX > 0 && dirY > 0) {
				nodes[2] = thisAsNode;
				aabb = new AABB(minX, minY, maxX + expanseX,
						maxY + expanseY);

			} else if (dirX > 0 && dirY <= 0) {
				nodes[0] = thisAsNode;
				aabb = new AABB(minX, minY - expanseY, maxX
						+ expanseX, maxY);
			} else {
				throw new AssertionError(
						"Error: QuadTree direction is invalid (?): "
								+ dirX + " (dirX) " + dirY
								+ " (dirY)");
			}

			add(entity);
		}
	}

	/**
	 * Removes an object from the QuadTree.
	 * <p>
	 * This method will automatically prune any empty leaves from the tree.
	 * 
	 * @param entity
	 *            The object to remove
	 * @return Whether or not the tree is empty after removal
	 */
	public boolean remove(Entity entity) {
		if (!entity.getAABB().intersectAABB(aabb)) {
			return false;
		}

		for (int i = 0; i < numEntities; i++) {
			if (entities[i] == entity) {
				removeEntityFromList(i);
			}
		}

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null && nodes[i].remove(entity)) {
				nodes[i] = null;
			}
		}

		return isThisNodeEmpty();
	}

	/**
	 * Determines if this node contains anything.
	 * 
	 * @return Whether or not this node contains any leaves or entities.
	 */
	private boolean isThisNodeEmpty() {
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				return false;
			}
		}

		return numEntities == 0;
	}

	/**
	 * Removes an entity from the list, and shifts everything over so there are
	 * no gaps in the entities array.
	 * 
	 * @param index
	 */
	private void removeEntityFromList(int index) {
		for (int i = index + 1; i < numEntities; i++) {
			entities[i - 1] = entities[i];
		}
		entities[numEntities - 1] = null;
		numEntities--;
	}

	/**
	 * Returns a set of every object in the tree
	 * 
	 * @param result
	 *            The Set of objects to store all the tree's objects in.
	 * @return The result parameter, but returned for programmer convenience.
	 */
	public Set<Entity> getAll(Set<Entity> result) {
		return queryRange(aabb, result);
	}

	/**
	 * Returns every object in the tree that intersects a certain space.
	 * 
	 * @param aabb
	 *            The space of interest.
	 * @param result
	 *            The Set of objects to store all the objects that intersect
	 *            aabb.
	 * @return The result parameter, but returned for programmer convenience.
	 */
	public Set<Entity> queryRange(AABB aabb, Set<Entity> result) {
		if (!aabb.intersectAABB(aabb)) {
			return result;
		}

		for (int i = 0; i < numEntities; i++) {
			if (entities[i].getAABB().intersectAABB(aabb)) {
				result.add(entities[i]);
			}
		}

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				nodes[i].queryRange(aabb, result);
			}
		}

		return result;
	}

	/**
	 * Adds an object to the child node if and only if the object intersects the
	 * child node.
	 * 
	 * @param entity The object that may be added
	 * @param minX 
	 * @param minY
	 * @param maxX
	 * @param maxY
	 * @param nodeIndex
	 */
	private void tryToAddToChildNode(Entity entity, float minX,
			float minY, float maxX, float maxY, int nodeIndex) {
		if (entity.getAABB().intersectRect(minX, minY, maxX,
				maxY)) {
			if (nodes[nodeIndex] == null) {
				nodes[nodeIndex] = new QuadTree(new AABB(minX,
						minY, maxX, maxY), entities.length);
			}

			nodes[nodeIndex].add(entity);
		}
	}

	/**
	 * Adds an object to any relevant child nodes.
	 * 
	 * @param entity The object to add to child nodes
	 */
	private void addToChild(Entity entity) {
		float minX = aabb.getMinX();
		float minY = aabb.getMinY();
		float maxX = aabb.getMaxX();
		float maxY = aabb.getMaxY();

		float halfXLength = (maxX - minX) / 2.0f;
		float halfYLength = (maxY - minY) / 2.0f;

		minY += halfYLength;
		maxX -= halfXLength;

		tryToAddToChildNode(entity, minX, minY, maxX, maxY, 0);

		minX += halfXLength;
		maxX += halfXLength;

		tryToAddToChildNode(entity, minX, minY, maxX, maxY, 1);

		minY -= halfYLength;
		maxY -= halfYLength;

		tryToAddToChildNode(entity, minX, minY, maxX, maxY, 3);

		minX -= halfXLength;
		maxX -= halfXLength;

		tryToAddToChildNode(entity, minX, minY, maxX, maxY, 2);
	}

	// TODO: Use this code to create an appropriate "toString" method for the
	// QuadTree

	// public void print() {
	// print(0, "NW");
	// }
	//
	// private void print(int depth, String location) {
	// String prefix = "";
	// for (int i = 0; i < depth; i++) {
	// prefix += "-";
	// }
	// for (int i = 0; i < numEntities; i++) {
	// System.out.println(prefix + location + " " + i
	// + ": " + entities[i]);
	// }
	// if (nodes[0] != null) {
	// nodes[0].print(depth + 1, "NW");
	// }
	// if (nodes[1] != null) {
	// nodes[1].print(depth + 1, "NE");
	// }
	// if (nodes[2] != null) {
	// nodes[2].print(depth + 1, "SW");
	// }
	// if (nodes[3] != null) {
	// nodes[3].print(depth + 1, "SE");
	// }
	// }
}
