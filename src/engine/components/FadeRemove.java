package engine.components;

import engine.core.entity.Entity;

public class FadeRemove extends RemoveComponent {
	private double timer;
	private double duration;
	private int animationFrame;
	private double solidityDuration;

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
		if(l != null) {
			l.setIntensity(amt);
		}
	}
}
