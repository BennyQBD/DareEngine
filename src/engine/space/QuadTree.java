package engine.space;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class QuadTree<T extends ISpatialObject> implements
		ISpatialStructure<T> {
	private QuadTree<T> nodes[];
	private int capacity;
	private List<T> objects;
	private AABB aabb;

	public QuadTree(AABB aabb, int capacity) {
		this.aabb = aabb;
		this.capacity = capacity;
		objects = new ArrayList<>();
		nodes = null;
	}
	
	private QuadTree(QuadTree<T> other) {
		this.nodes = other.nodes;
		this.objects = other.objects;
		this.capacity = other.capacity;
		this.aabb = other.aabb;
	}

	@Override
	public void add(T obj) {
		if (addInternal(obj)) {
			return;
		}
		
		double dirX = obj.getAABB().getCenterX() - aabb.getCenterX();
		double dirY = obj.getAABB().getCenterY() - aabb.getCenterY();
		expand(dirX, dirY);
		add(obj);
	}
	
	private void expand(double dirX, double dirY) {
		QuadTree<T> thisAsNode = new QuadTree<>(this);

		double minX = aabb.getMinX();
		double minY = aabb.getMinY();
		double maxX = aabb.getMaxX();
		double maxY = aabb.getMaxY();

		double expanseX = maxX - minX;
		double expanseY = maxY - minY;

		nodes = null;
		objects = new ArrayList<>();
		
		if (dirX <= 0 && dirY <= 0) {	
			aabb = new AABB(minX - expanseX, minY - expanseY, maxX, maxY);
			subdivide();
			nodes[1] = thisAsNode;
		} else if (dirX <= 0 && dirY > 0) {
			aabb = new AABB(minX - expanseX, minY, maxX, maxY + expanseY);
			subdivide();
			nodes[3] = thisAsNode;
		} else if (dirX > 0 && dirY > 0) {
			aabb = new AABB(minX, minY, maxX + expanseX, maxY + expanseY);
			subdivide();
			nodes[2] = thisAsNode;
		} else if (dirX > 0 && dirY <= 0) {
			aabb = new AABB(minX, minY - expanseY, maxX + expanseX, maxY);
			subdivide();
			nodes[0] = thisAsNode;
		} else {
			throw new AssertionError(
					"Error: QuadTree direction is invalid (?): " + dirX
							+ " (dirX) " + dirY + " (dirY)");
		}
	}

	private boolean addInternal(T obj) {
		if (!aabb.contains(obj.getAABB())) {
			return false;
		}
		if (nodes == null) {
			if (objects.size() < capacity) {
				objects.add(obj);
				return true;
			}
			subdivide();
		}
		if (!addToChildNode(obj)) {
			objects.add(obj);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private void subdivide() {
		double minX = aabb.getMinX();
		double minY = aabb.getMinY();
		double maxX = aabb.getMaxX();
		double maxY = aabb.getMaxY();

		double halfXLength = (maxX - minX) / 2.0f;
		double halfYLength = (maxY - minY) / 2.0f;

		nodes = (QuadTree<T>[]) (new QuadTree[4]);

		minY += halfYLength;
		maxX -= halfXLength;
		nodes[0] = new QuadTree<T>(new AABB(minX, minY, maxX, maxY), capacity);

		minX += halfXLength;
		maxX += halfXLength;
		nodes[1] = new QuadTree<T>(new AABB(minX, minY, maxX, maxY), capacity);

		minY -= halfYLength;
		maxY -= halfYLength;
		nodes[3] = new QuadTree<T>(new AABB(minX, minY, maxX, maxY), capacity);

		minX -= halfXLength;
		maxX -= halfXLength;
		nodes[2] = new QuadTree<T>(new AABB(minX, minY, maxX, maxY), capacity);

		reinsertObjects();
	}

	private void reinsertObjects() {
		Iterator<T> it = objects.iterator();
		while (it.hasNext()) {
			if (addToChildNode(it.next())) {
				it.remove();
			}
		}
	}

	private boolean addToChildNode(T obj) {
		for(int i = 0; i < nodes.length; i++) {
			if(nodes[i].addInternal(obj)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void remove(T obj) {
		if(!removeInternal(obj)) {
			objects.remove(obj);
		}
	}
	
	private boolean removeInternal(T obj) {
		if(!aabb.contains(obj.getAABB())) {
			return false;
		}
		
		if(objects.remove(obj)) {
			return true;
		}
		
		if(nodes == null) {
			return false;
		}
		
		for(int i = 0; i < nodes.length; i++) {
			if(nodes[i].removeInternal(obj)) {
				prune();
				return true;
			}
		}
		
		return false;
	}

	private void prune() {
		if(!isNodesEmpty()) {
			return;
		}
		
		nodes = null;
	}
	
	private boolean isNodesEmpty() {
		for(int i = 0; i < nodes.length; i++) {
			if(!nodes[i].isEmpty()) {
				return false;
			}
		}
		
		return true;
	}

	private boolean isEmpty() {
		if(!objects.isEmpty()) {
			return false;
		}
		
		if(nodes == null) {
			return true;
		}
		
		return isNodesEmpty();
	}

	@Override
	public void clear() {
		objects.clear();
		
		if(nodes != null) {
			for(int i = 0; i < nodes.length; i++) {
				nodes[i].clear();
			}
		}
	}

	@Override
	public Set<T> getAll(Set<T> result) {
		return addAll(result);
	}
	
	private Set<T> addAll(Set<T> result) {
		result.addAll(objects);
		
		if(nodes != null) {
			for(int i = 0; i < nodes.length; i++) {
				nodes[i].addAll(result);
			}
		}
		
		return result;
	}
	
	@Override
	public Set<T> queryRange(Set<T> result, AABB range) {
		if (!aabb.intersects(range)) {
			return result;
		}
		
		if(range.contains(aabb)) {
			return addAll(result);
		}
		
		if(nodes != null) {
			for (int i = 0; i < nodes.length; i++) {
				result = nodes[i].queryRange(result, range);
			}
		}
		
		Iterator<T> it = objects.iterator();
		while (it.hasNext()) {
			T current = it.next();
			if (current.getAABB().intersects(range)) {
				result.add(current);
			}
		}
		
		return result;
	}
}
