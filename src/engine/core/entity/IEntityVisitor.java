package engine.core.entity;

public interface IEntityVisitor {
	public void visit(Entity entity, EntityComponent component);
}
