package engine.rendering;


public class Texture {
	private final IRenderDevice device;
	private final int width;
	private final int height;
	private int id;

	public Texture(IRenderDevice device, ArrayBitmap image, int filter) {
		this.device = device;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.id = device.createTexture(width, height, image,
				filter);
	}

	public void dispose() {
		id = device.releaseTexture(id);
	}

	@Override
	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}

	public int getDeviceID() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ArrayBitmap getPixels() {
		return getPixels(0, 0, width, height);
	}

	public ArrayBitmap getPixels(int x, int y, int width, int height) {
		return device.getTexture(id, x, y, width, height);
	}
}
