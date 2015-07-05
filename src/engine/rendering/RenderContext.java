/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.rendering;

import engine.util.Util;

/**
 * An implementation of higher-level rendering functions.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class RenderContext implements IRenderContext {
	private final LightMap lightMap;
	private final RenderTarget target;

	/**
	 * Creates a new RenderContext.
	 * 
	 * @param device The device being used for rendering.
	 * @param target The target being rendered to.
	 */
	public RenderContext(IRenderDevice device, RenderTarget target) {
		this.target = target;
		this.lightMap = new LightMap(device, target.getWidth(), target.getHeight(), 1);
	}

	@Override
	public void dispose() {
		lightMap.dispose();
	}

	@Override
	public void clear(Color color) {
		target.clear(color);
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

		target.drawRect(sheet.getSheet().getDeviceID(),
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
	public void clearLighting(Color color) {
		lightMap.clear(color);
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
		target.drawRect(lightMap.getId(),
				IRenderDevice.BlendMode.APPLY_LIGHT, -1, -1, 1, 1, 0, 0, 1, 1,
				Color.WHITE, 1.0);
	}
}
