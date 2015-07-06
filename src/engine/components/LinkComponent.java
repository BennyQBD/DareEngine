/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.components;

import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.util.IDAssigner;

/**
 * Links two objects such that when one is removed, the other is also removed.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class LinkComponent extends EntityComponent {
	public static final int ID = IDAssigner.getId();
	private Entity linked;

	/**
	 * Creates a new LinkComponent.
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 * @param toLink
	 *            The entity which, if removed, also removes this component.
	 */
	public LinkComponent(Entity entity, Entity toLink) {
		super(entity, ID);
		this.linked = toLink;
	}
	
	@Override
	public void update(double delta) {
		if(linked.getRemoved()) {
			getEntity().remove();
		}
	}
}
