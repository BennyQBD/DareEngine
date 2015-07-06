/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util.factory;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import engine.rendering.Color;
import engine.rendering.IRenderDevice;
import engine.rendering.LightMap;

/**
 * A factory for creating LightMaps.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class LightMapFactory {
	private class LightMapKey {
		private int radius;
		private Color color;

		public LightMapKey(int radius, Color color) {
			this.radius = radius;
			this.color = color;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((color == null) ? 0 : color.hashCode());
			result = prime * result + radius;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			LightMapKey other = (LightMapKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (color == null) {
				if (other.color != null)
					return false;
			} else if (!color.equals(other.color))
				return false;
			if (radius != other.radius)
				return false;
			return true;
		}

		private LightMapFactory getOuterType() {
			return LightMapFactory.this;
		}
	}

	private final IRenderDevice device;
	private Map<LightMapKey, SoftReference<LightMap>> loadedLightMaps;

	/**
	 * Creates a new LightMap Factory
	 * 
	 * @param device
	 *            The render device to generate light maps for.
	 */
	public LightMapFactory(IRenderDevice device) {
		this.device = device;
		this.loadedLightMaps = new HashMap<>();
	}

	/**
	 * Gets a new object from the factory. If the desired object already exists,
	 * then that object is returned. Otherwise, a new object is created.
	 * 
	 * @param radius
	 *            The radius of the light in the LightMap.
	 * @param color
	 *            What color the light should be.
	 * @return A LightMap matching the specification.
	 */
	public LightMap get(int radius, Color color) {
		LightMapKey key = new LightMapKey(radius, color);
		SoftReference<LightMap> ref = loadedLightMaps.get(key);
		LightMap current = ref == null ? null : ref.get();
		if (current != null) {
			return current;
		} else {
			loadedLightMaps.remove(radius);
			LightMap result = new LightMap(device, radius, color);
			loadedLightMaps.put(key, new SoftReference<LightMap>(result));
			return result;
		}
	}
}
