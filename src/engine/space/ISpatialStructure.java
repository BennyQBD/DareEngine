package engine.space;

import java.util.Set;

public interface ISpatialStructure<T extends ISpatialObject> {
	public void add(T obj);
	public void remove(T obj);
	public void clear();
	public Set<T> getAll(Set<T> result);
	public Set<T> queryRange(Set<T> result, AABB range);
}
