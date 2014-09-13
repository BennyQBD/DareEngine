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
package engine.components;

import engine.core.*;
import engine.rendering.*;

/**
 * Represents a sprite that can be rendered on screen and animated.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class SpriteComponent extends EntityComponent {
	/** The name of this component when attached to an entity */
	public static final String NAME = "SpriteComponent";

	private Bitmap[] frames;
	private float[] frameTimes;
	private int[] nextFrames;
	private int currentFrame;
	private int transparencyType;
	private float renderLayer;
	private float currentFrameTime;

	/**
	 * Creates and initializes a sprite with no animation.
	 * 
	 * @param sprite
	 *            The image of the sprite
	 * @param transparencyType
	 *            The transparency algorithm used by the sprite. Should be set
	 *            to a RenderContext.TRANSPARENCY_ value
	 * @param layer
	 *            The render layer of this sprite.
	 *            
	 * @see engine.rendering.RenderContext
	 */
	public SpriteComponent(Bitmap sprite, int transparencyType,
			float layer) {
		this(new Bitmap[] { sprite }, 0.0f, transparencyType,
				layer);
	}

	/**
	 * Creates and initializes a sprite with a basic looping animation.
	 * 
	 * @param frames
	 *            An array of all the animation frames
	 * @param frameTime
	 *            The amount of time, in seconds, between each frame of
	 *            animation
	 * @param transparencyType
	 *            The transparency algorithm used by the sprite. Should be set
	 *            to a RenderContext.TRANSPARENCY_ value
	 * @param layer
	 *            The render layer of this sprite.
	 */
	public SpriteComponent(Bitmap[] frames, float frameTime,
			int transparencyType, float layer) {
		super(NAME);
		float frameTimes[] = new float[frames.length];
		int nextFrames[] = new int[frames.length];

		for (int i = 0; i < frames.length; i++) {
			frameTimes[i] = frameTime;
			nextFrames[i] = i + 1;
		}
		nextFrames[frames.length - 1] = 0;
		init(frames, frameTimes, nextFrames, transparencyType,
				layer);
	}

	/**
	 * Initializes a sprite to a usable state.
	 * 
	 * @param frames
	 *            An array of all the animation frames
	 * @param frameTimes
	 *            An array of the duration of each animation frame
	 * @param nextFrames
	 *            An array containing the indices of the next animation frame at
	 *            each frame
	 * @param transparencyType
	 *            The transparency algorithm used by the sprite. Should be set
	 *            to a RenderContext.TRANSPARENCY_ value
	 * @param layer
	 *            The render layer of this sprite.
	 */
	private final void init(Bitmap[] frames, float[] frameTimes,
			int[] nextFrames, int transparencyType, float layer) {
		this.frames = frames;
		this.frameTimes = frameTimes;
		this.nextFrames = nextFrames;
		this.transparencyType = transparencyType;
		this.renderLayer = layer;
		this.currentFrame = 0;
		this.currentFrameTime = 0.0f;
	}

	/**
	 * Creates and initializes a sprite with a custom animation.
	 * 
	 * @param frames
	 *            An array of all the animation frames
	 * @param frameTimes
	 *            An array of the duration of each animation frame
	 * @param nextFrames
	 *            An array containing the indices of the next animation frame at
	 *            each frame
	 * @param transparencyType
	 *            The transparency algorithm used by the sprite. Should be set
	 *            to a RenderContext.TRANSPARENCY_ value
	 * @param layer
	 *            The render layer of this sprite.
	 */
	public SpriteComponent(Bitmap[] frames, float[] frameTimes,
			int[] nextFrames, int transparencyType, float layer) {
		super(NAME);
		init(frames, frameTimes, nextFrames, transparencyType,
				layer);
	}

	/**
	 * Updates the entity with proper render layers.<p>
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void onAdd() {
		getEntity().setRenderLayer(renderLayer);
	}

	/**
	 * Updates the animation, progressing, stopping, or restarting
	 * if necessary.<p>
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void update(Input input, float delta) {
		currentFrameTime += delta;
		while (frameTimes[currentFrame] != 0
				&& currentFrameTime > frameTimes[currentFrame]) {
			currentFrameTime -= frameTimes[currentFrame];
			int nextFrame = nextFrames[currentFrame];
			currentFrame = nextFrame;
		}
	}

	/**
	 * Renders the current sprite to a render target.<p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void render(RenderContext target) {
		target.drawImage(frames[currentFrame], getEntity()
				.getAABB().getMinX(), getEntity().getAABB()
				.getMinY(), getEntity().getAABB().getMaxX(),
				getEntity().getAABB().getMaxY(),
				transparencyType);
	}
}
