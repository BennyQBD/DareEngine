/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.components;

import engine.core.entity.Entity;

/**
 * A RemoveComponent that fades out until the entity disappears.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class FadeRemove extends RemoveComponent {
	private double timer;
	private double duration;
	private int animationFrame;
	private double solidityDuration;

	/**
	 * Creates a new FadeRemove
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 * @param duration
	 *            How long the fade out takes.
	 * @param animationFrame
	 *            If the entity has a sprite component, which animation frame
	 *            should be jumped to before the fadeout sequence. If no jump is
	 *            desired, specify -1.
	 * @param solidityDuration
	 *            How long the entity continues colliding with other objects
	 *            before the collision component is remove.
	 */
	public FadeRemove(Entity entity, double duration, int animationFrame,
			double solidityDuration) {
		super(entity);
		this.duration = duration;
		this.animationFrame = animationFrame;
		this.solidityDuration = solidityDuration;
		this.timer = 0.0;
	}

	@Override
	public void onActivate() {
		SpriteComponent sc = (SpriteComponent) getEntity().getComponent(
				SpriteComponent.ID);
		if (sc != null && animationFrame != -1) {
			sc.setFrame(animationFrame);
		}
	}

	@Override
	public void removeUpdate(double delta) {
		timer += delta;
		if (timer >= solidityDuration) {
			getEntity().remove(CollisionComponent.ID);
		}
		if (timer >= duration) {
			getEntity().forceRemove();
		}

		double amt = (duration - timer) / duration;
		SpriteComponent sc = (SpriteComponent) getEntity().getComponent(
				SpriteComponent.ID);
		if (sc != null) {
			sc.setTransparency(amt);
		}

		LightComponent l = (LightComponent) getEntity().getComponent(
				LightComponent.ID);
		if (l != null) {
			l.setIntensity(amt);
		}
	}
}
