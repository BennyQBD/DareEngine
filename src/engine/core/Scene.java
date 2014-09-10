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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import engine.components.PhysicsComponent;
import engine.physics.AABB;
import engine.rendering.RenderContext;

public class Scene {
	private QuadTree scene;

	public Scene() {
		scene = new QuadTree(new AABB(-1, -1, 1, 1), 8);
	}

	public void addEntity(Entity entity) {
		scene.add(entity);
	}

	public void removeEntity(Entity entity) {
		scene.remove(entity);
	}

	public void update(Input input, float delta) {
		Set<Entity> entities = scene.queryRange(new AABB(-4, -4,
				4, 4), new HashSet<Entity>());

		Iterator<Entity> it = entities.iterator();
		while (it.hasNext()) {
			Entity current = (Entity) it.next();

			float startX = current.getX();
			float startY = current.getY();

			current.update(input, delta);

			if (startX != current.getX()
					|| startY != current.getY()) {
				removeEntity(current);
				current.updateAABB();
				handleCollisions(current);
				addEntity(current);
			}
		}
	}

	private void handleCollisions(Entity current) {
		PhysicsComponent physicsComponent = (PhysicsComponent) current
				.getComponent(PhysicsComponent.NAME);

		if (physicsComponent != null) {
			Set<Entity> collidingEntities = new HashSet<Entity>();
			scene.queryRange(current.getAABB(),
					collidingEntities);

			Iterator<Entity> it2 = collidingEntities.iterator();
			while (it2.hasNext()) {
				Entity other = (Entity) it2.next();
				PhysicsComponent otherComponent = (PhysicsComponent) other
						.getComponent(PhysicsComponent.NAME);

				if (otherComponent != null) {
					float distX = current.getAABB()
							.getDistanceX(other.getAABB());
					float distY = current.getAABB()
							.getDistanceY(other.getAABB());

					physicsComponent.onCollision(otherComponent,
							distX, distY);
				}
			}
		}
	}

	public void render(RenderContext target) {
		target.clear((byte) 0x00);

		Set<Entity> renderableEntities = scene.queryRange(
				target.getRenderArea(), new TreeSet<Entity>());

		Iterator<Entity> it = renderableEntities.iterator();
		while (it.hasNext()) {
			Entity current = (Entity) it.next();
			current.render(target);
		}
	}
}
