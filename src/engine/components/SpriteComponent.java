package engine.components;

import engine.core.*;
import engine.rendering.*;

public class SpriteComponent extends EntityComponent
{
	private Bitmap m_sprite;
	private int    m_transparencyType;
	private float  m_renderLayer;

	public SpriteComponent(Bitmap sprite, int transparencyType, float layer)
	{
		m_sprite = sprite;
		m_transparencyType = transparencyType;
		m_renderLayer = layer;
	}

	@Override
	public void OnAdd()
	{
		GetEntity().SetRenderLayer(m_renderLayer);
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
				m_transparencyType
			);
	}
}
