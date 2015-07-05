package engine;

import java.io.IOException;
import java.text.ParseException;

import engine.audio.IAudioDevice;
import engine.components.ColliderComponent;
import engine.components.CollisionComponent;
import engine.components.LightComponent;
import engine.components.SpriteComponent;
import engine.core.CoreEngine;
import engine.core.Scene;
import engine.core.entity.Entity;
import engine.input.Axis;
import engine.input.IInput;
import engine.rendering.Color;
import engine.rendering.IDisplay;
import engine.rendering.IRenderContext;
import engine.rendering.IRenderDevice;
import engine.rendering.LightMap;
import engine.rendering.SpriteSheet;
import engine.rendering.opengl.OpenGLDisplay;
import engine.space.AABB;
import engine.space.QuadTree;
import engine.util.factory.SpriteSheetFactory;
import engine.util.factory.TextureFactory;

public class Main {
	private static class TestScene extends Scene {
		SpriteSheet font;
		Entity e2;

		Axis movementX;
		Axis movementY;

		public TestScene(IInput input, IRenderDevice device,
				IAudioDevice audioDevice) throws IOException {
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
			new LightComponent(e2, light, 1.0, 1.0, 0.0, 0.0);

			movementX = new Axis(input, new int[] { IInput.KEY_A,
					IInput.KEY_LEFT }, new int[] { IInput.KEY_D,
					IInput.KEY_RIGHT }, null, null, 0.0, 0.0, 0, new int[] { 7 },
					new int[] { 5 }, new int[] { 0 });
			movementY = new Axis(input, new int[] { IInput.KEY_W,
					IInput.KEY_UP }, new int[] { IInput.KEY_S,
					IInput.KEY_DOWN }, null, null, 0.0, 0.0, 0, new int[] { 4 },
					new int[] { 6 }, new int[] { 1 });
		}

		@Override
		public boolean update(double delta) {
			super.updateRange(delta, new AABB(-2, -2, 2, 2));
			double speed = delta;
			e2.move((float)(movementX.getAmount() * speed), 0);
			e2.move(0, (float)(-movementY.getAmount() * speed));
			return false;
		}

		@Override
		public void render(IRenderContext target) {
			target.clear(Color.BLACK);
			target.clearLighting(new Color(0.1));
			super.renderRange(target, 0, 0);
			target.applyLighting();

			double y = 0.75;
			y = target.drawString("Hello, World!", font, -1, y, 0.25,
					Color.WHITE, 1.0);
		}
	}

	public static void main(String[] args) throws IOException, ParseException {
		IDisplay display = new OpenGLDisplay(640, 480, "My Display");
		CoreEngine engine = new CoreEngine(display, new TestScene(
				display.getInput(), display.getRenderDevice(),
				display.getAudioDevice()), 60.0);
		engine.start();
		display.dispose();
	}
}
