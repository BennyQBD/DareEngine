import java.awt.event.KeyEvent;

public class SpriteObject extends Entity
{
	private static final float BASE_SIZE = 0.05f;

	private float        m_width;
	private float        m_height;
	private final Bitmap m_image;
	
	public SpriteObject(float x, float y, 
			float width, float height, Bitmap image)
	{
		super(x, y, Util.VectorLength(width * BASE_SIZE,
					height * BASE_SIZE));
		m_width = width * BASE_SIZE;
		m_height = height * BASE_SIZE;
		m_image = image;
	}

	@Override
	public void Update(Input input, float delta)
	{
	}

	@Override
	public void Render(RenderContext target)
	{
		target.DrawImage(m_image, GetX(), GetY(),
			m_width, m_height);
	}
}
