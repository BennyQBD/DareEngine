package engine.components;

import engine.core.*;
import engine.rendering.*;

public class PhysicsComponent extends EntityComponent
{
	public static final String NAME = "PhysicsComponent";

	private float m_velX;
	private float m_velY;
	private static AudioClip s_audioClip = null;

	public float GetVelX() { return m_velX; }
	public float GetVelY() { return m_velY; }

	public PhysicsComponent(float velX, float velY)
	{
		super(NAME);
		m_velX = velX;
		m_velY = velY;
		if(s_audioClip == null)
		{
			s_audioClip = new AudioClip("./res/beep.wav");
		}
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
		//TODO: Temp code!
		Bounce(other, distX, distY);
	}

	public void Bounce(PhysicsComponent other, float distX, float distY)
	{
		if(distY > distX)
		{
			distX = 0.0f;
			m_velY *= -1;
		}
		else if(distX > distY)
		{
			distY = 0.0f;
			m_velX *= -1;
		}
		else
		{
			m_velX *= -1;
			m_velY *= -1;
		}

		s_audioClip.start();

//		float length = Util.VectorLength(distX, distY);
//		distX /= length;
//		distY /= length;
//
//		float normalDot = m_velX * distX + m_velY * distY;
//		float dirX = Util.VectorReflect(m_velX, distX, normalDot);
//		float dirY = Util.VectorReflect(m_velY, distY, normalDot);
//
//		m_velX = dirX;
//		m_velY = dirY;
	}
}
