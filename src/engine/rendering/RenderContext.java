package engine.rendering;

import engine.util.Util;

public class RenderContext implements IRenderContext {
	private final int width;
	private final int height;
	private final IRenderDevice device;
	private final LightMap lightMap;

	public RenderContext(IRenderDevice device) {
		this.device = device;
		this.width = device.getRenderTargetWidth(0);
		this.height = device.getRenderTargetHeight(0);
		this.lightMap = new LightMap(device, width, height, 1);
	}

	@Override
	public void dispose() {
		lightMap.dispose();
	}

	// @Override
	// public int getWidth() {
	// return width;
	// }
	//
	// @Override
	// public int getHeight() {
	// return height;
	// }

	@Override
	public void clear(double a, double r, double g, double b) {
		device.clear(0, a, r, g, b);
	}

	@Override
	public void drawSprite(SpriteSheet sheet, int index, double startX,
			double startY, double endX, double endY, double transparency,
			boolean flipX, boolean flipY, Color color) {
		double texMinX = ((double) sheet.getStartX(index) / ((double) sheet
				.getSheet().getWidth()));
		double texMinY = ((double) sheet.getStartY(index) / ((double) sheet
				.getSheet().getHeight()));
		double texMaxX = texMinX + ((double) sheet.getSpriteWidth())
				/ ((double) sheet.getSheet().getWidth());
		double texMaxY = texMinY + ((double) sheet.getSpriteHeight())
				/ ((double) sheet.getSheet().getHeight());

		if (flipX) {
			double temp = texMinX;
			texMinX = texMaxX;
			texMaxX = temp;
		}

		if (!flipY) {
			double temp = texMinY;
			texMinY = texMaxY;
			texMaxY = temp;
		}

		device.drawRect(0, sheet.getSheet().getDeviceID(),
				IRenderDevice.BlendMode.SPRITE, startX, startY, endX, endY,
				texMinX, texMinY, texMaxX, texMaxY, color, transparency);
	}

	@Override
	public double drawString(String str, SpriteSheet font, double x, double y,
			double scale, Color color, double wrapX) {
		double aspect = font.getSpriteAspect();
		double maxLength = (wrapX - x) / (scale * aspect);
		if (wrapX <= x || wrapX <= -1 || str.length() < maxLength) {
			drawStringLine(str, font, x, y, scale, color);
			return y - scale;
		}
		str = Util.wrapString(str, maxLength);
		String[] strs = str.split("\n");
		for (int i = 0; i < strs.length; i++) {
			String[] wrappedStrings = strs[i].split("(?<=\\G.{"
					+ ((int) maxLength) + "})");
			for (int j = 0; j < wrappedStrings.length; j++, y -= scale) {
				drawStringLine(wrappedStrings[j], font, x, y, scale, color);
			}
		}
		return y;
	}

	private void drawStringLine(String str, SpriteSheet font, double x,
			double y, double scale, Color color) {
		double aspect = font.getSpriteAspect();
		double startX = x;
		double startY = y;
		double width = scale * aspect;
		double height = scale;
		for (int i = 0; i < str.length(); i++, startX += width) {
			char c = str.charAt(i);
			drawSprite(font, (int) c, startX, startY, startX + width, startY
					+ height, 1.0, false, false, color);
		}
	}

	@Override
	public void clearLighting(double a, double r, double g, double b) {
		lightMap.clear(a, r, g, b);
	}

	@Override
	public void drawLight(LightMap light, double startX, double startY,
			double endX, double endY, double texStartX, double texStartY,
			double texEndX, double texEndY, Color color) {
		lightMap.addLight(light, startX, startY, endX, endY, texStartX,
				texStartY, texEndX, texEndY, color);
	}

	@Override
	public void applyLighting() {
		device.drawRect(0, lightMap.getId(),
				IRenderDevice.BlendMode.APPLY_LIGHT, -1, -1, 1, 1, 0, 0, 1, 1,
				Color.WHITE, 1.0);
	}

	public void getPixels(int[] dest) {
		device.getTexture(0, dest, 0, 0, width, height);
	}
}
