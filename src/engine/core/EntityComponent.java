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
package engine.core;

import engine.rendering.*;

/**
 * Base class for all components that can be attached to entities.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public abstract class EntityComponent {
	private Entity entity;
	private String name;

	/**
	 * Creates and initializes with a specific name.
	 * 
	 * @param name
	 *            The name associated with this particular component.
	 */
	public EntityComponent(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of the component.
	 * 
	 * @return The name of this component.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the parent entity
	 * 
	 * @return The entity that has this as a component.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Sets the parent entity
	 * 
	 * @param entity
	 *            The entity that has this as a component.
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Called whenever an entity adds this component.
	 */
	public void onAdd() {
	}

	/**
	 * Updates any parameters or properties of this component based on time,
	 * user input, or other factors.
	 * 
	 * @param input
	 *            User input to be considered
	 * @param delta
	 *            The amount of time, in seconds, since the last update.
	 */
	public void update(Input input, float delta) {
	}

	/**
	 * Renders anything this component needs to draw to a specific render
	 * target.
	 * 
	 * @param target
	 *            The location being rendered to.
	 */
	public void render(RenderContext target) {
	}
}
