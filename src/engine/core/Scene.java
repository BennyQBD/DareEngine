/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
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

/**
 * Represents an entire game scene, including any objects or players.
 * <p>
 * This can almost be thought of as a game level. The difference is this can
 * also represent other interactive game aspects, such as the title screen and
 * main menu.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public abstract class Scene {
	private ISpatialStructure<Entity> structure;

	/**
	 * Creates a new Scene.
	 * 
	 * @param structure
	 *            The spatial structure storing everything in this scene.
	 */
	public Scene(ISpatialStructure<Entity> structure) {
		this.structure = structure;
	}

	/**
	 * Updates everything intersecting a specific range of space.
	 * 
	 * @param delta
	 *            How much time has passed since the last update.
	 * @param range
	 *            The range of space to update.
	 */
	protected void updateRange(double delta, AABB range) {
		Set<Entity> entities = structure.queryRange(new HashSet<Entity>(),
				range);

		Iterator<Entity> it = entities.iterator();
		while (it.hasNext()) {
			Entity current = (Entity) it.next();
			current.update(delta);
		}
	}

	/**
	 * Gets the spatial structure used by this scene.
	 * 
	 * @return The spatial structure used by this scene.
	 */
	protected ISpatialStructure<Entity> getStructure() {
		return structure;
	}

	/**
	 * Updates this scene.
	 * 
	 * @param delta
	 *            How much time has passed since the last update.
	 * @return Whether or not the scene has "ended." This can be anything that
	 *         prompts the engine shutting down.
	 */
	public abstract boolean update(double delta);

	/**
	 * Renders everything that is visible to the screen.
	 * 
	 * @param target
	 *            The context being used for rendering.
	 * @param viewportX
	 *            The location of the viewport on X.
	 * @param viewportY
	 *            The location of the viewport on Y.
	 */
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
				}), new AABB(viewportX - 1, viewportY - 1, viewportX + 1,
						viewportY + 1));

		Iterator<Entity> it = renderableEntities.iterator();
		while (it.hasNext()) {
			it.next().render(target, viewportX, viewportY);
		}
	}

	/**
	 * Renders the scene
	 * 
	 * @param target
	 *            The context used for rendering.
	 */
	public abstract void render(IRenderContext target);
}
