package engine.rendering;

public interface IRenderContext {
	public void clear(double a, double r, double g, double b);
	public void drawSprite(SpriteSheet sheet, int index, double startX,
			double startY, double endX, double endY, double transparency,
			boolean flipX, boolean flipY, Color color);
	public double drawString(String msg, SpriteSheet font, double x, double y,
			double scale, Color color, double wrapX);

	public void clearLighting(double a, double r, double g, double b);
	public void drawLight(LightMap light, double startX, double startY,
			double endX, double endY, double texStartX, double texStartY,
			double texEndX, double texEndY, Color color);
	public void applyLighting();

	void dispose();
}
