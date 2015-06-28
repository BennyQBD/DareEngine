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

	public void fitAABB(AABB newAABB) {
		structure.remove(this);
		if(aabb.getWidth() == 0.0 && aabb.getHeight() == 0.0) {
			aabb = newAABB;
		} else {
			aabb = aabb.combine(newAABB);
		}
		structure.add(this);
	}

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

	public void visitInRange(int id, AABB range, IEntityVisitor visitor) {
		Set<Entity> entities = structure.queryRange(new HashSet<Entity>(),
				range);
		Iterator<Entity> it = entities.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			if(entity.isRemoved) {
				continue;
			}
			EntityComponent component = id == -1 ? null : entity
					.getComponent(id);
			if (component != null || id == -1) {
				visitor.visit(entity, component);
			}
		}
	}

	public void add(EntityComponent component) {
		components.add(component);
	}

	public void remove(EntityComponent component) {
		componentsToRemove.add(component);
	}

	public void remove(int id) {
		Iterator<EntityComponent> it = components.iterator();
		while (it.hasNext()) {
			EntityComponent current = it.next();
			if (current.getId() == id) {
				componentsToRemove.add(current);
			}
		}
	}
	
	public AABB translateAABB(AABB aabb) {
		return aabb.move(x, y);
	}

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

	public void remove() {
		if(isRemoved) {
			return;
		}
		AudioComponent ac = (AudioComponent)getComponent(AudioComponent.ID);
		if(ac != null) {
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

	public void forceRemove() {
		isRemoved = true;
		structure.remove(this);
	}

	public boolean getRemoved() {
		return isRemoved;
	}

	public void update(double delta) {
		components.removeAll(componentsToRemove);
		componentsToRemove.clear();
		
		Iterator<EntityComponent> it = components.iterator();
		while (it.hasNext()) {
			it.next().update(delta);
		}
	}

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
	
	public double getX() {
		return x;
	}
	
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
