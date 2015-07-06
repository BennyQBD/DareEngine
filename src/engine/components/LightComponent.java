/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.components;

import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.rendering.Color;
import engine.rendering.IRenderContext;
import engine.rendering.LightMap;
import engine.space.AABB;
import engine.util.IDAssigner;

/**
 * Casts a dynamic light on the entity and nearby entities.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class LightComponent extends EntityComponent {
	public static final int ID = IDAssigner.getId();
	private LightMap light;
	private Color color;
	private double offsetX;
	private double offsetY;
	private double halfWidth;
	private double halfHeight;

	/**
	 * Creates a new LightComponent
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 * @param light
	 *            The light to be cast.
	 * @param width
	 *            How wide of an area the light is cast over, normalized into the range (-1, 1)
	 * @param height
	 *            How tall of an area the light is cast over, normalized into the range (-1, 1)
	 * @param offsetX
	 *            How much the light is offset from the center of the entity on
	 *            X.
	 * @param offsetY
	 *            How much the light is offset from the center of the entity on
	 *            Y.
	 */
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

	/**
	 * Sets the intensity of the light
	 * 
	 * @param amt
	 *            How intense the light is, where 0.0 is none and 1.0 is full.
	 */
	public void setIntensity(double amt) {
		this.color = new Color(amt, amt, amt);
	}
}
