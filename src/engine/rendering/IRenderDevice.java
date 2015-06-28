package engine.rendering;

import static org.lwjgl.opengl.GL11.*;

public interface IRenderDevice {
	public static enum BlendMode {
		SPRITE, ADD_LIGHT, APPLY_LIGHT
	}

	public static final int FILTER_NEAREST = GL_NEAREST;
	public static final int FILTER_LINEAR = GL_LINEAR;
	
	public void dispose();

	public int createTexture(int width, int height, ArrayBitmap image, int filter);
	public int releaseTexture(int id);
	public ArrayBitmap getTexture(int id, int x, int y, int width, int height);

	public int createRenderTarget(int width, int height, int texId);
	public int getRenderTargetWidth(int fbo);
	public int getRenderTargetHeight(int fbo);
	public int releaseRenderTarget(int fbo);

	public void clear(int fbo, double a, double r, double g, double b);
	public void drawRect(int fbo, int texId, BlendMode mode, double startX,
			double startY, double endX, double endY, double texStartX, double texStartY,
			double texEndX, double texEndY, Color c, double transparency);
}
