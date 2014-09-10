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


import engine.components.SpriteComponent;
import engine.core.CoreEngine;
import engine.core.Display;
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
public class Main
{
	/**
	 * The main entry point of the program.
	 * 
	 * @param args The command line arguments passed to the program.
	 */
	public static void main(String[] args)
	{
		Display display = new Display(800, 600, "Dare Engine");
		Scene scene = new Scene();

		Bitmap test = new Bitmap("./res/bricks.jpg");

		scene.addEntity(new Entity(-1f, -1f, 1f, 1f)
				.addComponent(
					new SpriteComponent(test, 
						RenderContext.TRANSPARENCY_NONE, 0)));

		CoreEngine engine = new CoreEngine(display, scene);
		engine.start();
	}
}
