package engine;

import java.io.IOException;

import org.lwjgl.LWJGLException;

import engine.components.ColliderComponent;
import engine.components.CollisionComponent;
import engine.components.LightComponent;
import engine.components.SpriteComponent;
import engine.core.CoreEngine;
import engine.core.Scene;
import engine.core.entity.Entity;
import engine.rendering.Color;
import engine.rendering.IDisplay;
import engine.rendering.IRenderContext;
import engine.rendering.IRenderDevice;
import engine.rendering.LightMap;
import engine.rendering.SpriteSheet;
import engine.rendering.opengl.OpenGLDisplay;
import engine.space.AABB;
import engine.space.QuadTree;
import engine.util.factory.TextureFactory;
import engine.util.factory.SpriteSheetFactory;

public class Main {
	private static class TestScene extends Scene {
		SpriteSheet font;
		Entity e2;

		public TestScene(IRenderDevice device) throws IOException {
			super(new QuadTree<Entity>(new AABB(-1, -1, 1, 1), 8));
			SpriteSheetFactory sprites = new SpriteSheetFactory(
					new TextureFactory(device, "./res/"));

			font = sprites.get("monospace.png", 16, 16, 1,
					IRenderDevice.FILTER_LINEAR);

			Entity e = new Entity(getStructure(), 0, 0, 0);
			new ColliderComponent(e);
			new CollisionComponent(e);
			new SpriteComponent(e, 1.0, 1.0, sprites.get("bricks.jpg", 1, 1, 0,
					IRenderDevice.FILTER_LINEAR), 0, Color.WHITE);
			LightMap light = new LightMap(device, 32, Color.WHITE);
			new LightComponent(e, light, 2.0, 2.0, 0.0, 0.0);

			e2 = new Entity(getStructure(), -1.25, 0, 0);
			new ColliderComponent(e2);
			new CollisionComponent(e2);
			new SpriteComponent(e2, 0.5, 0.5, sprites.get("bricks.jpg", 1, 1,
					0, IRenderDevice.FILTER_LINEAR), 0, Color.WHITE);
		}

		@Override
		public boolean update(double delta) {
			super.updateRange(delta, new AABB(-2, -2, 2, 2));
			e2.move((float) (delta * 0.25), 0);
			return false;
		}

		@Override
		public void render(IRenderContext target) {
			target.clear(0.0, 0.0, 0.0, 0.0);
			target.clearLighting(0.1, 0.1, 0.1, 0.1);
			super.renderRange(target, 0, 0);
			target.applyLighting();

			double y = 0.75;
			y = target.drawString("Hello, World!", font, -1, y, 0.25,
					Color.WHITE, 1.0);
		}
	}

	public static void main(String[] args) throws LWJGLException, IOException {
		IDisplay display = new OpenGLDisplay(640, 480, "My Display");
		CoreEngine engine = new CoreEngine(display, new TestScene(
				display.getRenderDevice()), 60.0);
		engine.start();
		display.dispose();
	}
}
