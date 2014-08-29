package core;

import rendering.*;

public abstract class EntityComponent
{
	public abstract void Update(Input input, float delta);
	public abstract void Render(RenderContext target);
}
