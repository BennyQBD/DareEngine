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
package engine;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.io.IOException;

import engine.components.SpriteComponent;
import engine.core.CoreEngine;
import engine.core.Entity;
import engine.core.Scene;
import engine.rendering.Bitmap;
import engine.rendering.RenderContext;

/**
 * The sole purpose of this class is to hold the main method.
 * <p>
 * Any other use should be placed in a separate class
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Main extends Applet {
	private static final long serialVersionUID = 1L;

	private static CoreEngine engine = new CoreEngine(800, 600,
			60.0f, createScene());

	private static Scene createScene() {
		Scene scene = new Scene();

		Bitmap test = null;
		try {
			test = new Bitmap("bricks.jpg");
		} catch (IOException e) {
			e.printStackTrace();
			throw new AssertionError("Error: Unable to load resource.");
		}

		scene.add(new Entity(-1f, -1f, 1f, 1f)
				.add(new SpriteComponent(test,
						RenderContext.TRANSPARENCY_NONE, 0)));

		return scene;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		setLayout(new BorderLayout());
		add(engine, BorderLayout.CENTER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() {
		engine.start();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {
		engine.stop();
	}

	/**
	 * The main entry point of the program.
	 * 
	 * @param args
	 *            The command line arguments passed to the program.
	 */
	public static void main(String[] args) {
		engine.createWindow("Dare Engine", true);
		engine.start();
	}
}
