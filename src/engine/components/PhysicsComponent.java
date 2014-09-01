package engine.components;

import engine.core.*;
import engine.rendering.*;

public class PhysicsComponent extends EntityComponent
{
	public static final String NAME = "PhysicsComponent";

	private float m_velX;
	private float m_velY;

	public PhysicsComponent(float velX, float velY)
	{
		super(NAME);
		m_velX = velX;
		m_velY = velY;
	}

	@Override
	public void Update(Input input, float delta)
	{
		GetEntity().SetY(GetEntity().GetY() + m_velY * delta);
		GetEntity().SetX(GetEntity().GetX() + m_velX * delta);
	}

	public void OnCollision(PhysicsComponent other, 
			float distX, float distY)
	{
		//m_velX *= -1;
	}
}
