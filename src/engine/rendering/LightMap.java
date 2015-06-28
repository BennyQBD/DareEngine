package engine.rendering;

import java.io.IOException;

import engine.util.Util;

public class LightMap {
	private final IRenderDevice device;
	private final int width;
	private final int height;
	private final double scale;
	private int id;
	private int fbo;

	public LightMap(IRenderDevice device, int radius, Color color) {
		this.device = device;
		this.width = radius * 2;
		this.height = radius * 2;
		this.scale = 1;
		initTextures(generateLighting(radius, width, height, color));
	}

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

	public void clear(double a, double r, double g, double b) {
		device.clear(getFbo(), r, g, b, a);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

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

	private static ArrayBitmap generateLighting(int radius, int width, int height,
			Color color) {
		ArrayBitmap result = new ArrayBitmap(width, height);
		int centerX = width / 2;
		int centerY = height / 2;
		int radiusSq = radius * radius;
		for (int j = 0, distY = -centerY; j < height; j++, distY++) {
			for (int i = 0, distX = -centerX; i < width; i++, distX++) {
				result.set(i, j, calcLight(radius, radiusSq, distX,
						distY, Dither.getDither(i, j), color));
			}
		}
		return result;
	}

	public int getId() {
		return id;
	}

	public void save(String fileName, String outputFileFormat) throws IOException {
		final ArrayBitmap image = device.getTexture(id, 0, 0, width, height);
		
		image.visitAll(new ArrayBitmap.IVisitor() {
			@Override
			public void visit(int x, int y, int pixel) {
				image.set(x, y, pixel | 0xFF000000);
			}
		});
		
		image.save(fileName, outputFileFormat);
	}

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
