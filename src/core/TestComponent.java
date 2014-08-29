package core;

import rendering.*;

public class TestComponent extends EntityComponent
{
	@Override
	public void Update(Input input, float delta)
	{

	}

	@Override
	public void Render(RenderContext target)
	{
		target.FillRect(GetEntity().GetAABB().GetMinX(), 
				GetEntity().GetAABB().GetMinY(),
		        GetEntity().GetAABB().GetMaxX(), 
				GetEntity().GetAABB().GetMaxY(),
				(byte)0x00, 
				(byte)0x79, (byte)0xbf, (byte)0x10);

	}
}
