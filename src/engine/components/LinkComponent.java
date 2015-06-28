package engine.components;

import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.util.IDAssigner;

public class LinkComponent extends EntityComponent {
	public static final int ID = IDAssigner.getId();
	private Entity linked;

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
