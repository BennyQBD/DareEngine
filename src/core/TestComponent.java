package core;

import rendering.*;

public class TestComponent extends EntityComponent
{
	private Bitmap m_sprite;
//	private Sampler m_sampler;

	public TestComponent(Bitmap sprite)
	{
		m_sprite = sprite;
//		m_sampler = new NearestSampler();
	}

	@Override
	public void Update(Input input, float delta)
	{

	}

	@Override
	public void Render(RenderContext target)
	{
		target.DrawImage(m_sprite,
				GetEntity().GetAABB().GetMinX(), 
				GetEntity().GetAABB().GetMinY(),
		        GetEntity().GetAABB().GetMaxX(), 
				GetEntity().GetAABB().GetMaxY(),
				RenderContext.TRANSPARENCY_NONE
			);

	}
}
