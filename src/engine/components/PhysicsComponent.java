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

public class PhysicsComponent extends EntityComponent {
	public static final String NAME = "PhysicsComponent";

	private float xVel;
	private float yVel;

	public PhysicsComponent(float xVel, float yVel) {
		super(NAME);
		this.xVel = xVel;
		this.yVel = yVel;
	}

	@Override
	public void update(Input input, float delta) {
		getEntity().setY(getEntity().getY() + yVel * delta);
		getEntity().setX(getEntity().getX() + xVel * delta);
	}

	public void onCollision(PhysicsComponent other, float distX,
			float distY) {
		// TODO: Temp code!
		bounce(other, distX, distY);
	}

	public void bounce(PhysicsComponent other, float distX,
			float distY) {
		if (distY > distX) {
			distX = 0.0f;
			yVel *= -1;
		} else if (distX > distY) {
			distY = 0.0f;
			xVel *= -1;
		} else {
			xVel *= -1;
			yVel *= -1;
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

	public float getXVel() {
		return xVel;
	}

	public float getYVel() {
		return yVel;
	}
}
