package engine.util.menu;

import engine.rendering.Color;
import engine.rendering.IRenderContext;
import engine.rendering.SpriteSheet;
import engine.util.Util;

public class Menu {
	private String[] options;
	private IMenuHandler handler;
	private int selectionIndex;

	private MenuStack menuStack;

	public Menu(String[] options, IMenuHandler handler) {
		this.options = options;
		this.handler = handler;
		this.selectionIndex = 0;
		this.menuStack = null;
	}

	public void setMenuStack(MenuStack stack) {
		this.menuStack = stack;
	}

	public void activate() {
		handler.handleMenu(selectionIndex, menuStack);
	}

	public void move(int amt) {
		selectionIndex = Util.floorMod(selectionIndex + amt, options.length);
	}

	public void render(IRenderContext target, SpriteSheet font, double offsetX,
			double offsetY, double scale, Color selectionColor, Color fontColor) {
		double y = offsetY;
		for (int i = 0; i < options.length; i++) {
			Color color = i == selectionIndex ? selectionColor : fontColor;
			y = target.drawString(options[i], font, offsetX, y, scale, color, 1.0);
		}
	}
}
