package components;

import rendering.*;
import core.*;

public class TestComponent2 extends EntityComponent
{
	@Override
	public void Update(Input input, float delta)
	{
		float speed = delta/10.0f;
		GetEntity().SetY(GetEntity().GetY() + speed);
		GetEntity().SetX(GetEntity().GetX() + speed);
	}
}
