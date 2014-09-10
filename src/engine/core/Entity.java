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
import engine.rendering.*;

/**
 * An object in the game.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 * @version 1.0
 * @since 2014-09-10
 */
public class Entity implements Comparable<Entity> {
	private float x;
	private float y;
	private float renderLayer;
	private AABB aabb;
	private List<EntityComponent> components;

	/**
	 * Creates a new Entity with minimum necessary construction.
	 * 
	 * @param minX
	 *            Smallest X value of the entity
	 * @param minY
	 *            Smallest Y value of the entity
	 * @param maxX
	 *            Biggest X value of the entity
	 * @param maxY
	 *            Biggest Y value of the entity
	 */
	public Entity(float minX, float minY, float maxX, float maxY) {
		this.components = new ArrayList<EntityComponent>();
		this.aabb = new AABB(minX, minY, maxX, maxY);
		this.x = aabb.GetCenterX();
		this.y = aabb.GetCenterY();
		this.renderLayer = 0.0f;
	}

	/**
	 * Finds and returns a component attached to this entity by name. If more
	 * than one is found, the first component in the list is returned. If none
	 * are found, returns null.
	 *
	 * @param name
	 *            The name of the component to look for.
	 * @return The attached component with the name, or null if none exists.
	 */
	public EntityComponent getComponent(String name) {
		for (int i = 0; i < components.size(); i++) {
			EntityComponent current = components.get(i);
			if (current.GetName().equals(name)) {
				return current;
			}
		}

		return null;
	}

	/**
	 * Adds a new component to the entity.
	 * 
	 * @param component
	 *            The component to add.
	 * @return this, to allow easily adding multiple components.
	 */
	public Entity addComponent(EntityComponent component) {
		component.SetEntity(this);
		component.OnAdd();
		components.add(component);
		return this;
	}

	/**
	 * Creates a new AABB for the object based on it's position.
	 * <p>
	 * This should not be called if this entity is in an acceleration structure,
	 * like a {@link engine.core.QuadTree}, or it will break the acceleration
	 * structure.
	 * <p>
	 * Instead, only call this method if the entity has moved and it is certain
	 * this entity is no longer in any acceleration structure.
	 */
	public void updateAABB() {
		float deltaX = x - aabb.GetCenterX();
		float deltaY = y - aabb.GetCenterY();

		float minX = aabb.GetMinX() + deltaX;
		float minY = aabb.GetMinY() + deltaY;
		float maxX = aabb.GetMaxX() + deltaX;
		float maxY = aabb.GetMaxY() + deltaY;

		aabb = new AABB(minX, minY, maxX, maxY);
	}

	/**
	 * Updates all the components attached to this entity.
	 * 
	 * @param input
	 *            User input to be processed
	 * @param delta
	 *            How much time has passed since the last update.
	 */
	public void update(Input input, float delta) {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).Update(input, delta);
		}
	}

	/**
	 * Renders all the components attached to this entity.
	 * 
	 * @param target
	 *            Where the components are rendered to.
	 */
	public void render(RenderContext target) {
		for (int i = 0; i < components.size(); i++) {
			components.get(i).Render(target);
		}
	}

	/**
	 * Calculates whether this Entity's AABB is intersecting another's AABB.
	 * 
	 * @param other
	 *            The entity to test intersection against.
	 * @return Whether or not this Entity is intersecting the other Entity.
	 */
	public boolean intersectAABB(Entity other) {
		return aabb.IntersectAABB(other.getAABB());
	}

	/**
	 * Compares this entity to another, and returns which comes first based on
	 * render layer. Higher render layers should be drawn first.
	 */
	@Override
	public int compareTo(Entity r) {
		final int BEFORE = -1;
		final int AFTER = 1;
		final int EQUAL = 0;
		if (this == r) {
			return EQUAL;
		}
		if (renderLayer < r.renderLayer) {
			return AFTER;
		}
		return BEFORE;
	}

	/**
	 * Gets the X Position of this entity
	 * 
	 * @return The X Position of this entity
	 */
	public float getX() {
		return x;
	}

	/**
	 * Gets the Y Position of this entity
	 * 
	 * @return The Y Position of this entity
	 */
	public float getY() {
		return y;
	}

	/**
	 * Gets the bounding box of this entity.
	 * 
	 * @return The AABB bounding this entity
	 */
	public AABB getAABB() {
		return aabb;
	}

	/**
	 * Sets this entity's X position to a new value.
	 * 
	 * @param x
	 *            The new X position of the Entity
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Sets this entity's Y position to a new value.
	 * 
	 * @param y
	 *            The new Y position of the Entity
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Sets this entity's render layer to a new value.
	 * 
	 * @param layer
	 *            The new render layer of the Entity. Higher values are drawn
	 *            first.
	 */
	public void setRenderLayer(float layer) {
		renderLayer = layer;
	}
}
