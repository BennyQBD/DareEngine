/*
 * Copyright (c) 2014, Benny Bobaganoosh
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package engine.components;

import engine.core.EntityComponent;
import engine.core.Input;

/**
 * The base class for any component that interacts with the physics engine.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class PhysicsComponent extends EntityComponent {
	/** The name of this component when attached to an entity */
	public static final String NAME = "PhysicsComponent";

	private float velX;
	private float velY;

	/**
	 * Creates and initializes a new physics component.
	 * 
	 * @param velX
	 *            The velocity of the object on X, in units per second.
	 * @param velY
	 *            The velocity of the object on Y, in units per second.
	 */
	public PhysicsComponent(float velX, float velY) {
		super(NAME);
		this.velX = velX;
		this.velY = velY;
	}

	/**
	 * Moves the entity into a new position based on the information in the
	 * physics component.
	 * <p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void update(Input input, float delta) {
		getEntity().setY(getEntity().getY() + velY * delta);
		getEntity().setX(getEntity().getX() + velX * delta);
	}

	/**
	 * Respond to a collision with another object.
	 * 
	 * @param other
	 *            The physics component attached to the other object
	 * @param distX
	 *            Penetration distance on X
	 * @param distY
	 *            Penetration distance on Y
	 */
	public void onCollision(PhysicsComponent other, float distX,
			float distY) {
		// TODO: Temp code!
		bounce(other, distX, distY);
	}

	/**
	 * Bounce off of another object that is colliding with this.
	 * 
	 * @param other
	 *            The physics component attached to the other object
	 * @param distX
	 *            Penetration distance on X
	 * @param distY
	 *            Penetration distance on Y
	 */
	public void bounce(PhysicsComponent other, float distX,
			float distY) {

		// TODO: This calculation is not completely correct.
		// The bounce direction shouldn't be determined just based on distY >
		// distX or whatnot. Instead, it should be based on which side of the
		// other object is hit first.

		// For instance, if this object is moving and the other object is still,
		// then logically "slide" this object backwards based on it's velocity
		// until it's at the point of impact. From there, the side to bounce off
		// can be determined from whether distY or distX reached 0 first.

		// Once this is solved, making it work for general cases where both
		// objects might be moving is relatively simple.
		
		// Some useful equations:
		// let t = time since impact
		// let impactY = Y location at impact point
		//
		// distX - velX * t = 0
		// distY - velY * t = impactY

		if (distY > distX) {
			distX = 0.0f;
			velY *= -1;
		} else if (distX > distY) {
			distY = 0.0f;
			velX *= -1;
		} else {
			velX *= -1;
			velY *= -1;
		}
		// float length = Util.VectorLength(distX, distY);
		// distX /= length;
		// distY /= length;
		//
		// float normalDot = m_velX * distX + m_velY * distY;
		// float dirX = Util.VectorReflect(m_velX, distX, normalDot);
		// float dirY = Util.VectorReflect(m_velY, distY, normalDot);
		//
		// m_velX = dirX;
		// m_velY = dirY;
	}

	/**
	 * Get the velocity on the X axis
	 * 
	 * @return velocity along the X axis
	 */
	public float getVelX() {
		return velX;
	}

	/**
	 * Get the velocity on the Y axis
	 * 
	 * @return velocity along the Y axis
	 */
	public float getVelY() {
		return velY;
	}
}
