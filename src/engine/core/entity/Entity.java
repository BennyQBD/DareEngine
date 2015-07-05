/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.core.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import engine.components.AudioComponent;
import engine.components.CollisionComponent;
import engine.components.RemoveComponent;
import engine.rendering.IRenderContext;
import engine.space.AABB;
import engine.space.ISpatialObject;
import engine.space.ISpatialStructure;
import engine.util.DoublePair;

/**
 * An object in the game.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Entity implements ISpatialObject, Comparable<Entity> {
	private static int currentId = 0;
	private ISpatialStructure<Entity> structure;
	private List<EntityComponent> components;
	private List<EntityComponent> componentsToRemove;
	private AABB aabb;
	private int id;
	private boolean isRemoved;
	private double x;
	private double y;

	private static int getNextId() {
		return currentId++;
	}

	/**
	 * Creates a new Entity with minimum necessary construction.
	 * 
	 * @param structure
	 *            The spatial structure this entity will be contained in.
	 * @param posX
	 *            The location of the entity on the x axis.
	 * @param posY
	 *            The location of the entity on the y axis.
	 * @param posZ
	 *            The location of the entity on the z axis.
	 */
	public Entity(ISpatialStructure<Entity> structure, double posX,
			double posY, double posZ) {
		this.structure = structure;
		this.x = posX;
		this.y = posY;
		this.aabb = new AABB(0, 0, posZ, 0, 0);
		this.isRemoved = false;
		this.id = getNextId();
		this.components = new ArrayList<>();
		this.componentsToRemove = new ArrayList<>();
		structure.add(this);
	}

	/**
	 * Ensures the bounds of this entity are at least big enough to contain
	 * {@code newAABB}.
	 * 
	 * @param newAABB
	 *            The AABB this entity must be able to contain.
	 */
	public void fitAABB(AABB newAABB) {
		structure.remove(this);
		if (aabb.getWidth() == 0.0 && aabb.getHeight() == 0.0) {
			aabb = newAABB;
		} else {
			aabb = aabb.combine(newAABB);
		}
		structure.add(this);
	}

	/**
	 * Finds and returns a component attached to this entity by id. If more than
	 * one is found, the first component in the list is returned. If none are
	 * found, returns null.
	 * 
	 * @param id
	 *            The id of the component. This is typically found with
	 *            ComponentClass.ID.
	 * @return The first component found with the given id, or null if none are
	 *         found.
	 */
	public EntityComponent getComponent(int id) {
		Iterator<EntityComponent> it = components.iterator();
		while (it.hasNext()) {
			EntityComponent current = it.next();
			if (current.getId() == id) {
				return current;
			}
		}
		return null;
	}

	/**
	 * Visits every entity with a particular component within a certain range of
	 * space.
	 * 
	 * @param id
	 *            The id of the component. This is typically found with
	 *            ComponentClass.ID. If no particular component is desired,
	 *            specify -1.
	 * @param range
	 *            The range of space to be visited.
	 * @param visitor
	 *            The visitor that will be executed for every entity visited.
	 */
	public void visitInRange(int id, AABB range, IEntityVisitor visitor) {
		Set<Entity> entities = structure.queryRange(new HashSet<Entity>(),
				range);
		Iterator<Entity> it = entities.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			if (entity.isRemoved) {
				continue;
			}
			EntityComponent component = id == -1 ? null : entity
					.getComponent(id);
			if (component != null || id == -1) {
				visitor.visit(entity, component);
			}
		}
	}

	/**
	 * Adds a new component to the entity.
	 * 
	 * @param component
	 *            The component to add.
	 */
	public void add(EntityComponent component) {
		components.add(component);
	}

	/**
	 * Removes a component to the entity.
	 * 
	 * @param component
	 *            The component to remove.
	 */
	public void remove(EntityComponent component) {
		componentsToRemove.add(component);
	}

	/**
	 * Removes a component from this entity by id. If more than one is found,
	 * the first component in the list is removed. If none are found, nothing is
	 * removed.
	 * 
	 * @param id
	 *            The id of the component. This is typically found with
	 *            ComponentClass.ID.
	 */
	public void remove(int id) {
		Iterator<EntityComponent> it = components.iterator();
		while (it.hasNext()) {
			EntityComponent current = it.next();
			if (current.getId() == id) {
				componentsToRemove.add(current);
			}
		}
	}

	/**
	 * Moves an AABB into the position of this entity.
	 * 
	 * @param aabb
	 *            The aabb to be translated.
	 * @return An AABB translated into the position of this entity.
	 */
	public AABB translateAABB(AABB aabb) {
		return aabb.move(x, y);
	}

	/**
	 * Moves this entity by a certain amount. If this entity is a colliding
	 * entity and it hits another colliding entity when it moves, then this will
	 * only move the entity as far as it can without intersecting a colliding
	 * entity. This function only works on one axis at a time; one of the
	 * parameters must be 0.
	 * 
	 * @param amtXIn
	 *            The amount to move on X.
	 * @param amtYIn
	 *            The amount to move on Y.
	 * @return The amount actually moved on X or Y, depending on which was
	 *         specified.
	 * @throws IllegalArgumentException
	 *             If movement is specified on both axis; this function only
	 *             works on one axis at a time.
	 */
	public float move(float amtXIn, float amtYIn) {
		if (amtXIn != 0.0f && amtYIn != 0.0f) {
			throw new IllegalArgumentException(
					"Can only move in 1 dimension per call");
		}
		structure.remove(this);
		double amtX = (double) amtXIn;
		double amtY = (double) amtYIn;

		CollisionComponent c = (CollisionComponent) getComponent(CollisionComponent.ID);
		if (c != null) {
			DoublePair amts = c.resolveCollisions(amtX, amtY);
			amtX = amts.getVal1();
			amtY = amts.getVal2();
		}

		x += amtX;
		y += amtY;
		structure.add(this);
		if (amtX != 0) {
			return (float) amtX;
		} else {
			return (float) amtY;
		}
	}

	/**
	 * Removes this entity from the spatial structure, and triggers any remove
	 * actions specified for this entity.
	 */
	public void remove() {
		if (isRemoved) {
			return;
		}
		AudioComponent ac = (AudioComponent) getComponent(AudioComponent.ID);
		if (ac != null) {
			ac.play("remove");
		}

		isRemoved = true;
		RemoveComponent r = (RemoveComponent) getComponent(RemoveComponent.ID);
		if (r != null) {
			r.activate();
		} else {
			forceRemove();
		}
	}

	/**
	 * Forcibly removes this entity from the spatial structure without
	 * triggering remove actions. Use with caution; this function may fail or
	 * cause errors if used inappropriately.
	 */
	public void forceRemove() {
		isRemoved = true;
		structure.remove(this);
	}

	/**
	 * Gets whether or not this entity has been removed from the spatial
	 * structure.
	 * 
	 * @return Whether or not this entity has been removed from the spatial
	 *         structure.
	 */
	public boolean getRemoved() {
		return isRemoved;
	}

	/**
	 * Updates all the components attached to this entity.
	 * 
	 * @param delta
	 *            How much time has passed since the last update.
	 */
	public void update(double delta) {
		components.removeAll(componentsToRemove);
		componentsToRemove.clear();

		Iterator<EntityComponent> it = components.iterator();
		while (it.hasNext()) {
			it.next().update(delta);
		}
	}

	/**
	 * Renders all the components attached to this entity.
	 * 
	 * @param target
	 *            The context being used for rendering
	 * @param viewportX
	 *            The location of the viewport on X.
	 * @param viewportY
	 *            The location of the viewport on Y.
	 */
	public void render(IRenderContext target, double viewportX, double viewportY) {
		Iterator<EntityComponent> it = components.iterator();
		while (it.hasNext()) {
			it.next().render(target, viewportX, viewportY);
		}
	}

	@Override
	public AABB getAABB() {
		return translateAABB(aabb);
	}

	/**
	 * Gets the location of this entity on x.
	 * 
	 * @return The location of this entity on x.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the location of this entity on y.
	 * 
	 * @return The location of this entity on y.
	 */
	public double getY() {
		return y;
	}

	@Override
	public int compareTo(Entity o) {
		if (id > o.id) {
			return 1;
		}
		if (id < o.id) {
			return -1;
		}
		return 0;
	}
}
