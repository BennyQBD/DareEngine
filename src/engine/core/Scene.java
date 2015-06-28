package engine.core;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import engine.core.entity.Entity;
import engine.rendering.IRenderContext;
import engine.space.AABB;
import engine.space.ISpatialStructure;

public abstract class Scene {
	private ISpatialStructure<Entity> structure;

	public Scene(ISpatialStructure<Entity> structure) {
		this.structure = structure;
	}

	protected void updateRange(double delta, AABB range) {
		Set<Entity> entities = structure.queryRange(new HashSet<Entity>(),
				range);

		Iterator<Entity> it = entities.iterator();
		while (it.hasNext()) {
			Entity current = (Entity) it.next();
			current.update(delta);
		}
	}

	protected ISpatialStructure<Entity> getStructure() {
		return structure;
	}

	public abstract boolean update(double delta);

	protected void renderRange(IRenderContext target, double viewportX,
			double viewportY) {
		Set<Entity> renderableEntities = structure.queryRange(
				new TreeSet<Entity>(new Comparator<Entity>() {
					public int compare(Entity e0, Entity e1) {
						if (e0.getAABB().getMinZ() > e1.getAABB().getMinZ()) {
							return 1;
						}
						if (e0.getAABB().getMinZ() < e1.getAABB().getMinZ()) {
							return -1;
						}

						return e0.compareTo(e1);
					}
				}),
				new AABB(viewportX - 1, viewportY - 1, viewportX + 1, viewportY + 1));

		Iterator<Entity> it = renderableEntities.iterator();
		while (it.hasNext()) {
			it.next().render(target, viewportX, viewportY);
		}
	}

	public abstract void render(IRenderContext target);
}
