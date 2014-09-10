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

public class SpriteComponent extends EntityComponent {
	public static final String NAME = "SpriteComponent";

	private Bitmap[] frames;
	private float[] frameTimes;
	private int[] nextFrames;
	private int currentFrame;
	private int transparencyType;
	private float renderLayer;
	private float currentFrameTime;

	public SpriteComponent(Bitmap sprite, int transparencyType,
			float layer) {
		this(new Bitmap[] { sprite }, 0.0f, transparencyType,
				layer);
	}

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

	public SpriteComponent(Bitmap[] frames, float[] frameTimes,
			int[] nextFrames, int transparencyType, float layer) {
		super(NAME);
		init(frames, frameTimes, nextFrames, transparencyType,
				layer);
	}

	@Override
	public void onAdd() {
		getEntity().setRenderLayer(renderLayer);
	}

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

	@Override
	public void render(RenderContext target) {
		target.drawImage(frames[currentFrame], getEntity()
				.getAABB().getMinX(), getEntity().getAABB()
				.getMinY(), getEntity().getAABB().getMaxX(),
				getEntity().getAABB().getMaxY(),
				transparencyType);
	}
}
