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
import engine.input.ButtonAxis;
import engine.input.CompoundAxis;
import engine.input.CompoundButton;
import engine.input.IAxis;
import engine.input.IButton;
import engine.input.IInput;
import engine.input.JoystickAxis;
import engine.input.JoystickButton;
import engine.input.KeyButton;
import engine.rendering.Color;
import engine.rendering.IDisplay;
import engine.rendering.IRenderContext;
import engine.rendering.IRenderDevice;
import engine.rendering.LightMap;
import engine.rendering.SpriteSheet;
import engine.rendering.opengl.OpenGLDisplay;
import engine.space.AABB;
import engine.space.QuadTree;
import engine.util.Debug;
import engine.util.factory.SpriteSheetFactory;
import engine.util.factory.TextureFactory;

public class Main {
	private static class TestScene extends Scene {
		SpriteSheet font;
		Entity e2;

		IAxis movementX;
		IAxis movementY;

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

			e2 = new Entity(getStructure(), -1.0, 0, 0);
			new ColliderComponent(e2);
			new CollisionComponent(e2);
			new SpriteComponent(e2, 0.5, 0.5, sprites.get("bricks.jpg", 1, 1,
					0, IRenderDevice.FILTER_LINEAR), 0, Color.WHITE);
			new LightComponent(e2, light, 1.0, 1.0, 0.0, 0.0);

			IButton leftKeyButtons = new KeyButton(input, new int[] {
					IInput.KEY_A, IInput.KEY_LEFT });
			IButton rightKeyButtons = new KeyButton(input, new int[] {
					IInput.KEY_D, IInput.KEY_RIGHT });
			IButton upKeyButtons = new KeyButton(input, new int[] {
					IInput.KEY_W, IInput.KEY_UP });
			IButton downKeyButtons = new KeyButton(input, new int[] {
					IInput.KEY_S, IInput.KEY_DOWN });

			IButton leftJoyButtons = new JoystickButton(input, 0, 7);
			IButton rightJoyButtons = new JoystickButton(input, 0, 5);
			IButton upJoyButtons = new JoystickButton(input, 0, 4);
			IButton downJoyButtons = new JoystickButton(input, 0, 6);

			IButton leftButtons = new CompoundButton(leftKeyButtons,
					leftJoyButtons);
			IButton rightButtons = new CompoundButton(rightKeyButtons,
					rightJoyButtons);
			IButton upButtons = new CompoundButton(upKeyButtons, upJoyButtons);
			IButton downButtons = new CompoundButton(downKeyButtons,
					downJoyButtons);
			
			IAxis buttonX = new ButtonAxis(leftButtons, rightButtons);
			IAxis buttonY = new ButtonAxis(upButtons, downButtons);
			
			IAxis joystickX = new JoystickAxis(input, 0, 0);
			IAxis joystickY = new JoystickAxis(input, 0, 1);

			movementX = new CompoundAxis(buttonX, joystickX);
			movementY = new CompoundAxis(buttonY, joystickY);
		}

		@Override
		public boolean update(double delta) {
			super.updateRange(delta, new AABB(-2, -2, 2, 2));
			double speed = delta;
			e2.move((float) (movementX.getAmount() * speed), 0);
			e2.move(0, (float) (-movementY.getAmount() * speed));
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
		Debug.init(true, true);
		IDisplay display = new OpenGLDisplay(640, 480, "My Display");
		CoreEngine engine = new CoreEngine(display, new TestScene(
				display.getInput(), display.getRenderDevice(),
				display.getAudioDevice()), 60.0);
		engine.start();
		display.dispose();
	}
}
