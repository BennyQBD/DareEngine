/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.rendering;

import java.io.IOException;

import engine.util.Util;

/**
 * A texture storing lighting information.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class LightMap {
	private final IRenderDevice device;
	private final int width;
	private final int height;
	private final double scale;
	private int id;
	private int fbo;

	/**
	 * Creates a new LightMap with a generated light
	 * 
	 * @param device
	 *            The device used for rendering.
	 * @param radius
	 *            The radius of the generated light.
	 * @param color
	 *            The color of the generated light.
	 */
	public LightMap(IRenderDevice device, int radius, Color color) {
		this.device = device;
		this.width = radius * 2;
		this.height = radius * 2;
		this.scale = 1;
		initTextures(generateLighting(radius, width, height, color));
	}

	/**
	 * Creates a blank LightMap with a certain width and height.
	 * 
	 * @param device
	 *            The device used for rendering.
	 * @param width
	 *            The width of the LightMap.
	 * @param height
	 *            The height of the LightMap.
	 * @param scale
	 *            How much this LightMap is scaled when drawn.
	 */
	public LightMap(IRenderDevice device, int width, int height, double scale) {
		this.device = device;
		this.width = width;
		this.height = height;
		this.scale = scale;
		ArrayBitmap image = new ArrayBitmap(width, height);
		image.clear(0);
		initTextures(image);
	}

	private void initTextures(ArrayBitmap data) {
		this.id = device.createTexture(width, height, data,
				IRenderDevice.FILTER_LINEAR);
		this.fbo = 0;
	}

	/**
	 * Releases this object. This should be called when the object will no
	 * longer be used, and no methods or fields should be used after this method
	 * is called.
	 */
	public void dispose() {
		fbo = device.releaseRenderTarget(fbo);
		id = device.releaseTexture(id);
	}

	@Override
	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}

	private int getFbo() {
		if (fbo == 0) {
			fbo = device.createRenderTarget(width, height, id);
		}
		return fbo;
	}

	/**
	 * Clears the lighting to a single color.
	 * 
	 * @param color
	 *            The color to clear to.
	 */
	public void clear(Color color) {
		device.clear(getFbo(), color);
	}

	/**
	 * Gets the width of the LightMap.
	 * 
	 * @return The width of the LightMap.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Gets the height of the LightMap.
	 * 
	 * @return The height of the LightMap.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the scale of the LightMap.
	 * 
	 * @return The scale of the LightMap.
	 */
	public double getScale() {
		return scale;
	}

	private static int toData(double val, double dither, Color color) {
		double amt = (Util.saturate(val + dither / 255.0));
		return color.scaleToARGB(amt);
	}

	private static int calcLight(int radius, int radiusSq, int distX,
			int distY, double dither, Color color) {
		int distCenterSq = distY * distY + distX * distX;
		if (distCenterSq > radiusSq) {
			return 0;
		}
		return toData(((double) radius / (double) (distCenterSq)), dither,
				color);
	}

	private static ArrayBitmap generateLighting(int radius, int width,
			int height, Color color) {
		ArrayBitmap result = new ArrayBitmap(width, height);
		int centerX = width / 2;
		int centerY = height / 2;
		int radiusSq = radius * radius;
		for (int j = 0, distY = -centerY; j < height; j++, distY++) {
			for (int i = 0, distX = -centerX; i < width; i++, distX++) {
				result.set(
						i,
						j,
						calcLight(radius, radiusSq, distX, distY,
								Dither.getDither(i, j), color));
			}
		}
		return result;
	}

	/**
	 * Gets the integer id of the LightMap on it's associated
	 * {@link IRenderDevice}.
	 * 
	 * @return The integer id of the LightMap on it's associated
	 *         {@link IRenderDevice}.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Saves this LightMap to a file.
	 * 
	 * @param fileName
	 *            The file name and path of the desired file.
	 * @param outputFileFormat
	 *            A string representing the image format for output. For
	 *            example, "png" and "jpg" are valid options.
	 * @throws IOException
	 *             If the image cannot be saved to a file.
	 */
	public void save(String fileName, String outputFileFormat)
			throws IOException {
		device.getTexture(id, 0, 0, width, height).save(fileName,
				outputFileFormat);
	}

	/**
	 * Adds another light to this LightMap.
	 * 
	 * @param light
	 *            The light to draw
	 * @param startX
	 *            The start location on X, normalized into the range (-1, 1)
	 * @param startY
	 *            The start location on Y, normalized into the range (-1, 1)
	 * @param endX
	 *            The end location on X, normalized into the range (-1, 1)
	 * @param endY
	 *            The end location on Y, normalized into the range (-1, 1)
	 * @param texStartX
	 *            The location in the {@link LightMap} on X where pixels should
	 *            begin being copied, normalized into the range (0, 1)
	 * @param texStartY
	 *            The location in the {@link LightMap} on Y where pixels should
	 *            begin being copied, normalized into the range (0, 1)
	 * @param texEndX
	 *            The location in the {@link LightMap} on X where pixels should
	 *            finish being copied, normalized into the range (0, 1)
	 * @param texEndY
	 *            The location in the {@link LightMap} on Y where pixels should
	 *            finish being copied, normalized into the range (0, 1)
	 * @param color
	 *            The color to mask the light with.
	 */
	public void addLight(LightMap light, double startX, double startY,
			double endX, double endY, double texStartX, double texStartY,
			double texEndX, double texEndY, Color color) {
		double posScale = 1.0 / scale;
		double texScale = 1.0 / light.getScale();
		double texMinX = texStartX * texScale;
		double texMinY = texStartY * texScale;
		double texMaxX = texEndX * texScale;
		double texMaxY = texEndY * texScale;

		double xStart = (startX + 1.0) * posScale - 1.0;
		double xEnd = (endX + 1.0) * posScale - 1.0;
		double yStart = (startY + 1.0) * posScale - 1.0;
		double yEnd = (endY + 1.0) * posScale - 1.0;

		device.drawRect(getFbo(), light.id, IRenderDevice.BlendMode.ADD_LIGHT,
				xStart, yStart, xEnd, yEnd, texMinX, texMinY, texMaxX, texMaxY,
				color, 1.0);
	}
}
