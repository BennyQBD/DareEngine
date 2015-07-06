/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.components;

import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.rendering.Color;
import engine.rendering.IRenderContext;
import engine.rendering.SpriteSheet;
import engine.space.AABB;
import engine.util.IDAssigner;

/**
 * Draws a sprite where this entity is.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class SpriteComponent extends EntityComponent {
	private class Animation {
		private SpriteSheet[] sheets;
		private int[] indices;
		private double[] frameTimes;
		private int[] nextFrames;
		private int currentFrame;
		private double currentFrameTime;

		public Animation(SpriteSheet[] sheets, int[] indices,
				double[] frameTimes, int[] nextFrames) {
			this.sheets = sheets;
			this.indices = indices;
			this.frameTimes = frameTimes;
			this.nextFrames = nextFrames;
			this.currentFrame = 0;
			this.currentFrameTime = 0.0;
		}

		public void update(double delta) {
			currentFrameTime += delta;
			while (frameTimes[currentFrame] != 0
					&& currentFrameTime > frameTimes[currentFrame]) {
				currentFrameTime -= frameTimes[currentFrame];
				int nextFrame = nextFrames[currentFrame];
				currentFrame = nextFrame;
			}
		}

		public SpriteSheet getSheet() {
			return sheets[currentFrame];
		}

		public int getSpriteIndex() {
			return indices[currentFrame];
		}

		public void setFrame(int frame) {
			currentFrame = frame;
			currentFrameTime = 0.0;
		}
	}

	public static final int ID = IDAssigner.getId();
	private Animation animation;
	private double halfWidth;
	private double halfHeight;
	private double spriteOffsetFlippedX;
	private double spriteOffsetFlippedY;
	private double transparency;
	private boolean flipX;
	private boolean flipY;
	private Color color;

	/**
	 * Creates a new SpriteComponent
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 * @param width
	 *            How wide of an area the sprite consumes, normalized such that
	 *            the screen width is 2.
	 * @param height
	 *            How tall of an area the sprite consumes, normalized such that
	 *            the screen height is 2.
	 * @param sheet
	 *            The sheet containing this sprite.
	 * @param spriteIndex
	 *            The index of this sprite in the sheet.
	 * @param color
	 *            The color applied to the sprite.
	 */
	public SpriteComponent(Entity entity, double width, double height,
			SpriteSheet sheet, int spriteIndex, Color color) {
		this(entity, width, height, new SpriteSheet[] { sheet },
				new int[] { spriteIndex }, 0.0, color);
	}

	/**
	 * Creates a new SpriteComponent
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 * @param width
	 *            How wide of an area the sprite consumes, normalized such that
	 *            the screen width is 2.
	 * @param height
	 *            How tall of an area the sprite consumes, normalized such that
	 *            the screen height is 2.
	 * @param sheet
	 *            The sheet containing the sprites for the animation.
	 * @param frameTime
	 *            How long each sprite in the sheet is displayed.
	 * @param color
	 *            The color applied to each sprite.
	 */
	public SpriteComponent(Entity entity, double width, double height,
			SpriteSheet sheet, double frameTime, Color color) {
		super(entity, ID);
		SpriteSheet[] sheets = new SpriteSheet[sheet.getNumSprites()];
		int[] indices = new int[sheets.length];
		for (int i = 0; i < sheets.length; i++) {
			indices[i] = i;
			sheets[i] = sheet;
		}
		init(entity, width, height, sheets, indices, frameTime, color);
	}

	/**
	 * Creates a new SpriteComponent
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 * @param width
	 *            How wide of an area the sprite consumes, normalized such that
	 *            the screen width is 2.
	 * @param height
	 *            How tall of an area the sprite consumes, normalized such that
	 *            the screen height is 2.
	 * @param sheet
	 *            The sheet containing the sprites for the animation.
	 * @param indices
	 *            The index of each sprite in the animation, in order.
	 * @param frameTime
	 *            How long each sprite in the sheet is displayed.
	 * @param color
	 *            The color applied to each sprite.
	 */
	public SpriteComponent(Entity entity, double width, double height,
			SpriteSheet sheet, int[] indices, double frameTime, Color color) {
		super(entity, ID);
		SpriteSheet[] sheets = new SpriteSheet[indices.length];
		for (int i = 0; i < sheets.length; i++) {
			sheets[i] = sheet;
		}
		init(entity, width, height, sheets, indices, frameTime, color);
	}

	/**
	 * Creates a new SpriteComponent
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 * @param width
	 *            How wide of an area the sprite consumes, normalized such that
	 *            the screen width is 2.
	 * @param height
	 *            How tall of an area the sprite consumes, normalized such that
	 *            the screen height is 2.
	 * @param sheets
	 *            The sheets containing the sprites for the animation.
	 * @param indices
	 *            The index of each sprite in the animation, in order.
	 * @param frameTime
	 *            How long each sprite in the sheet is displayed.
	 * @param color
	 *            The color applied to each sprite.
	 */
	public SpriteComponent(Entity entity, double width, double height,
			SpriteSheet[] sheets, int[] indices, double frameTime, Color color) {
		super(entity, ID);
		init(entity, width, height, sheets, indices, frameTime, color);
	}

	/**
	 * Creates a new SpriteComponent
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 * @param width
	 *            How wide of an area the sprite consumes, normalized such that
	 *            the screen width is 2.
	 * @param height
	 *            How tall of an area the sprite consumes, normalized such that
	 *            the screen height is 2.
	 * @param sheets
	 *            The sheets containing the sprites for the animation.
	 * @param indices
	 *            The index of each sprite in the animation, in order.
	 * @param frameTimes
	 *            How long each frame of animation is displayed for
	 * @param nextFrames
	 *            Which animation frame is displayed after which.
	 * @param color
	 *            The color applied to each sprite.
	 */
	public SpriteComponent(Entity entity, double width, double height,
			SpriteSheet[] sheets, int[] indices, double[] frameTimes,
			int[] nextFrames, Color color) {
		super(entity, ID);
		init(entity, width, height, sheets, indices, frameTimes, nextFrames,
				color);
	}

	private void init(Entity entity, double width, double height,
			SpriteSheet[] sheets, int[] indices, double frameTime, Color color) {
		double frameTimes[] = new double[sheets.length];
		int nextFrames[] = new int[sheets.length];

		for (int i = 0; i < sheets.length; i++) {
			frameTimes[i] = frameTime;
			nextFrames[i] = i + 1;
		}
		nextFrames[sheets.length - 1] = 0;
		init(entity, width, height, sheets, indices, frameTimes, nextFrames,
				color);
	}

	private void init(Entity entity, double width, double height,
			SpriteSheet[] sheets, int[] indices, double[] frameTimes,
			int[] nextFrames, Color color) {
		this.animation = new Animation(sheets, indices, frameTimes, nextFrames);
		this.halfWidth = width / 2.0;
		this.halfHeight = height / 2.0;

		AABB spriteAABB = animation.getSheet().getAABB(
				animation.getSpriteIndex(), width, height);
		ColliderComponent c = (ColliderComponent) getEntity().getComponent(
				ColliderComponent.ID);
		if (c != null) {
			c.fitAABB(spriteAABB);
		} else {
			getEntity().fitAABB(spriteAABB);
		}
		spriteOffsetFlippedX = -spriteAABB.getMaxX() - spriteAABB.getMinX();
		spriteOffsetFlippedY = -spriteAABB.getMaxY() - spriteAABB.getMinY();

		this.transparency = 1.0;
		this.flipX = false;
		this.flipY = false;
		this.color = color;
	}

	@Override
	public void update(double delta) {
		animation.update(delta);
	}

	@Override
	public void render(IRenderContext target, double viewportX, double viewportY) {
		SpriteSheet sheet = animation.getSheet();
		int spriteIndex = animation.getSpriteIndex();
		if (sheet != null) {
			double spriteOffX = 0;
			double spriteOffY = 0;
			if (flipX) {
				spriteOffX = spriteOffsetFlippedX;
			}
			if (flipY) {
				spriteOffY = spriteOffsetFlippedY;
			}
			double centerX = getEntity().getX() - viewportX - spriteOffX;
			double centerY = getEntity().getY() - viewportY - spriteOffY;
			target.drawSprite(sheet, spriteIndex, centerX - halfWidth, centerY
					- halfHeight, centerX + halfWidth, centerY + halfHeight,
					transparency, flipX, flipY, color);
		}
	}

	/**
	 * Sets whether the sprites is flipped on X
	 * 
	 * @param flipX
	 *            Whether the sprites are flipped on X
	 */
	public void setFlipX(boolean flipX) {
		this.flipX = flipX;
	}

	/**
	 * Sets whether the sprites is flipped on Y
	 * 
	 * @param flipY
	 *            Whether the sprites are flipped on Y
	 */
	public void setFlipY(boolean flipY) {
		this.flipY = flipY;
	}

	/**
	 * Gets how transparent this sprite is.
	 * 
	 * @return The transparency of this sprite.
	 */
	public double getTransparency() {
		return transparency;
	}

	/**
	 * Sets the transparency of this sprite.
	 * 
	 * @param transparency
	 *            The new transparency of this sprite.
	 */
	public void setTransparency(double transparency) {
		this.transparency = transparency;
	}

	/**
	 * Sets which frame of animation this is currently on.
	 * 
	 * @param frame
	 *            The new current frame of animation.
	 */
	public void setFrame(int frame) {
		animation.setFrame(frame);
	}
}