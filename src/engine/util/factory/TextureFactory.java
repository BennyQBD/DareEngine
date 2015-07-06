/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util.factory;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import engine.rendering.ArrayBitmap;
import engine.rendering.Texture;
import engine.rendering.IRenderDevice;

/**
 * A factory for creating Textures.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class TextureFactory {
	private final IRenderDevice device;
	private final String filePath;
	private Map<String, SoftReference<Texture>> loadedBitmaps;

	/**
	 * Creates a new Texture Factory.
	 * 
	 * @param device
	 *            The device to load textures for.
	 * @param filePath
	 *            The base path for image files.
	 */
	public TextureFactory(IRenderDevice device, String filePath) {
		this.device = device;
		this.filePath = filePath;
		this.loadedBitmaps = new HashMap<>();
	}

	/**
	 * Gets a new object from the factory. If the desired object already exists,
	 * then that object is returned. Otherwise, a new object is created.
	 * 
	 * @param fileName
	 *            The name of an image file to be loaded.
	 * @param filter
	 *            The type of filtering to be used. Should be one of the
	 *            IRenderDevice.FILTER options. Note that this is only updated
	 *            if the desired texture hasn't been loaded yet.
	 * @return A Texture matching the specification.
	 * @throws IOException If the file cannot be loaded.
	 */
	public Texture get(String fileName, int filter) throws IOException {
		fileName = filePath + fileName;
		SoftReference<Texture> ref = loadedBitmaps.get(fileName);
		Texture current = ref == null ? null : ref.get();
		if (current != null) {
			return current;
		} else {
			loadedBitmaps.remove(fileName);
			Texture result = new Texture(device, new ArrayBitmap(fileName),
					filter);
			loadedBitmaps.put(fileName, new SoftReference<Texture>(result));
			return result;
		}
	}
}
