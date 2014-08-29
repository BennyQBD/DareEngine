package core;

import rendering.*;

public class TestComponent2 extends TestComponent
{
	public TestComponent2(Bitmap sprite)
	{
		super(sprite);
	}

	@Override
	public void Update(Input input, float delta)
	{
		float speed = delta/10.0f;
		GetEntity().SetY(GetEntity().GetY() + speed);
		GetEntity().SetX(GetEntity().GetX() + speed);
	}
}
