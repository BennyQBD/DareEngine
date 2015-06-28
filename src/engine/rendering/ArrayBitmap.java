package engine.rendering;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.util.Util;

public class ArrayBitmap {
	public static interface IVisitor {
		public void visit(int x, int y, int pixel);
	}

	private final int offsetX;
	private final int offsetY;
	private final int rowOffset;
	private final int width;
	private final int height;
	private final int[] pixels;

	public ArrayBitmap(int width, int height) {
		this(width, height, new int[width * height]);
	}

	public ArrayBitmap(int width, int height, int[] pixels) {
		this(width, height, pixels, 0, 0, width);
	}

	public ArrayBitmap(int width, int height, int[] pixels, int offsetX,
			int offsetY, int rowOffset) {
		this.width = width;
		this.height = height;
		if (pixels.length < getNumPixels()) {
			throw new IllegalArgumentException(
					"Pixel array is smaller than width and height specify");
		}
		this.pixels = pixels;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.rowOffset = rowOffset;
		if (getIndex(width - 1, height - 1) > pixels.length) {
			throw new IllegalArgumentException(
					"Pixel array is not big enough to fit "
							+ "width, height, offsetX, offsetY, and rowOffset combination");
		}
	}

	public ArrayBitmap(String fileName) throws IOException {
		BufferedImage image = ImageIO.read(new File(fileName));
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = new int[getNumPixels()];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		this.offsetX = 0;
		this.offsetY = 0;
		this.rowOffset = width;
	}

	public void save(String fileName, String outputFormat) throws IOException {
		BufferedImage output = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		int[] data = ((DataBufferInt) output.getRaster().getDataBuffer())
				.getData();
		for (int i = 0; i < data.length; i++) {
			data[i] = pixels[i];
		}
		ImageIO.write(output, outputFormat, new File(fileName));
	}

	public void visitAll(IVisitor visitor) {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				visitor.visit(i, j, get(i, j));
			}
		}
	}

	public void clear(int color) {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				set(i, j, color);
			}
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isMoreOpaqueThanTransparent(int x, int y) {
		boundsCheck(x, y);
		return get(x, y) < 0;
	}

	public int get(int x, int y) {
		boundsCheck(x, y);
		return pixels[getIndex(x, y)];
	}

	public void set(int x, int y, int pixel) {
		boundsCheck(x, y);
		pixels[getIndex(x, y)] = pixel;
	}

	private int getIndex(int x, int y) {
		return (x + offsetX) + (y + offsetY) * rowOffset;
	}

	private int getNumPixels() {
		return width * height;
	}

	private void boundsCheck(int x, int y) {
		Util.boundsCheck(x, 0, width - 1);
		Util.boundsCheck(y, 0, height - 1);
	}
}
