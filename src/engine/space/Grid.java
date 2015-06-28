package engine.space;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import engine.util.Util;

public class Grid<T extends ISpatialObject> implements ISpatialStructure<T> {
	private final int tileSize;
	private final int width;
	private final int height;
	private final List<T>[] tiles;

	@SuppressWarnings("unchecked")
	public Grid(int tileSize, int width, int height) {
		this.tileSize = tileSize;
		this.width = width;
		this.height = height;
		this.tiles = (List<T>[]) (new List[width * height]);
		for (int i = 0; i < tiles.length; i++) {
			tiles[i] = new ArrayList<T>();
		}
	}

	private int getGridPosMin(double pos) {
		return (int) Math.floor(pos / (double) tileSize);
	}

	private int getGridPosMax(double pos) {
		return (int) Math.ceil(pos / (double) tileSize);
	}

	private int getTileX(int x) {
		return Util.floorMod(x, width);
	}

	private int getTileY(int y) {
		return Util.floorMod(y, height);
	}

	private List<T> getTile(int x, int y) {
		return tiles[getTileX(x) + getTileY(y) * width];
	}

	private interface IVisitor<T> {
		public void onVisit(List<T> tile);
	}

	private void visit(final AABB aabb, final IVisitor<T> visitor) {
		int minX, minY, maxX, maxY;

		if (aabb.getWidth() > (double) (width * tileSize)) {
			minX = 0;
			maxX = width - 1;
		} else {
			minX = getGridPosMin(aabb.getMinX());
			maxX = getGridPosMax(aabb.getMaxX());
		}
		if (aabb.getHeight() > (double) (height * tileSize)) {
			minY = 0;
			maxY = height - 1;
		} else {
			minY = getGridPosMin(aabb.getMinY());
			maxY = getGridPosMax(aabb.getMaxY());
		}

		for (int j = minY; j <= maxY; j++) {
			for (int i = minX; i <= maxX; i++) {
				visitor.onVisit(getTile(i, j));
			}
		}
	}

	@Override
	public void add(final T obj) {
		visit(obj.getAABB(), new IVisitor<T>() {
			@Override
			public void onVisit(List<T> tile) {
				tile.add(obj);
			}
		});
	}

	@Override
	public void remove(final T obj) {
		visit(obj.getAABB(), new IVisitor<T>() {
			@Override
			public void onVisit(List<T> tile) {
				tile.remove(obj);
			}
		});
	}

	@Override
	public Set<T> getAll(Set<T> result) {
		return result;
	}

	private Set<T> queryTile(List<T> tile, Set<T> result, AABB aabb) {
		Iterator<T> it = tile.iterator();
		while (it.hasNext()) {
			T t = it.next();
			if (t.getAABB().intersects(aabb)) {
				result.add(t);
			}
		}
		return result;
	}

	@Override
	public Set<T> queryRange(final Set<T> result, final AABB aabb) {
		visit(aabb, new IVisitor<T>() {
			@Override
			public void onVisit(List<T> tile) {
				queryTile(tile, result, aabb);
			}
		});

		return result;
	}

	@Override
	public void clear() {
		for(int i = 0; i < tiles.length; i++) {
			tiles[i].clear();
		}
	}
}
