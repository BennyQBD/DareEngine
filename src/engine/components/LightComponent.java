package engine.components;

import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.rendering.Color;
import engine.rendering.IRenderContext;
import engine.rendering.LightMap;
import engine.space.AABB;
import engine.util.IDAssigner;

public class LightComponent extends EntityComponent {
	public static final int ID = IDAssigner.getId();
	private LightMap light;
	private Color color;
	private double offsetX;
	private double offsetY;
	private double halfWidth;
	private double halfHeight;

	public LightComponent(Entity entity, LightMap light, double width,
			double height, double offsetX, double offsetY) {
		super(entity, ID);
		this.light = light;
		this.halfWidth = width / 2.0;
		this.halfHeight = height / 2.0;
		entity.fitAABB(new AABB(-halfWidth, -halfHeight, halfWidth, halfHeight));
		this.color = Color.WHITE;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	@Override
	public void render(IRenderContext target, double viewportX, double viewportY) {
		double centerX = getEntity().getX() - viewportX + offsetX;
		double centerY = getEntity().getY() - viewportY + offsetY;
		target.drawLight(light, centerX - halfWidth, centerY - halfHeight,
				centerX + halfWidth, centerY + halfHeight, 0, 0, 1, 1, color);
	}

	public void setIntensity(double amt) {
		this.color = new Color(amt, amt, amt);
	}
}
