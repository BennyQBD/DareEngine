package core;

import rendering.*;

public class TestComponent2 extends TestComponent
{
	@Override
	public void Update(Input input, float delta)
	{
		GetEntity().SetX(GetEntity().GetX() + delta);
	}
}
