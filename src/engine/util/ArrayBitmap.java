package engine.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ArrayBitmap {
	public static interface IVisitor {
		public void visit(int x, int y, int pixel);
	}
	
	private final int width;
	private final int height;
	private final int[] pixels;
	
	public ArrayBitmap(int width, int height) {
		this.width = width;
		this.height = height;
		this.pixels = new int[getNumPixels()];
	}
	
	public ArrayBitmap(int width, int height, int[] pixels) {
		this.width = width;
		this.height = height;
		if(pixels.length < getNumPixels()) {
			throw new IllegalArgumentException("Pixel array is smaller than width and height specify");
		}
		this.pixels = pixels;
	}
	
	public ArrayBitmap(String fileName) throws IOException {
		BufferedImage image = ImageIO.read(new File(fileName));
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.pixels = new int[getNumPixels()];
		image.getRGB(0, 0, width, height, pixels, 0, width);
	}
	
	public void save(String fileName, String outputFormat) throws IOException {
		BufferedImage output = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		int[] data = ((DataBufferInt) output.getRaster()
				.getDataBuffer()).getData();
		for(int i = 0; i < data.length; i++) {
			data[i] = pixels[i];
		}
		ImageIO.write(output, outputFormat, new File(fileName));
	}
	
	public void visitAll(IVisitor visitor) {
		for(int j = 0; j < height; j++) {
			for(int i = 0; i < width; i++) {
				visitor.visit(i, j, get(i, j));
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int get(int x, int y) {
		return pixels[getIndex(x, y)];
	}
	
	public void set(int x, int y, int pixel) {
		pixels[getIndex(x, y)] = pixel;
	}
	
	private int getIndex(int x, int y) {
		return x + y * width;
	}

	private int getNumPixels() {
		return width * height;
	}
}
