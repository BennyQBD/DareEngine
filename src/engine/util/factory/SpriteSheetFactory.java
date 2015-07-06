/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util.factory;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import engine.rendering.SpriteSheet;

/**
 * A factory for creating SpriteSheets.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class SpriteSheetFactory {
	private final TextureFactory bitmaps;
	private final Map<String, SoftReference<SpriteSheet>> loadedSpriteSheets;

	/**
	 * Creates a new Sprite Sheet Factory.
	 * 
	 * @param bitmaps
	 *            The source of textures for the sprite sheets.
	 */
	public SpriteSheetFactory(TextureFactory bitmaps) {
		this.bitmaps = bitmaps;
		this.loadedSpriteSheets = new HashMap<>();
	}

	/**
	 * Gets a new object from the factory. If the desired object already exists,
	 * then that object is returned. Otherwise, a new object is created.
	 * 
	 * @param fileName
	 *            The name of an image file containing the sprites.
	 * @param spritesPerX
	 *            Number of sprites on the X axis.
	 * @param spritesPerY
	 *            Number of sprites on the Y axis.
	 * @param spriteBorderSize
	 *            The number of pixels bordering each sprite on all sides.
	 * @param filter
	 *            The type of filtering to be used. Should be one of the
	 *            IRenderDevice.FILTER options. Note that this is only updated
	 *            if the desired texture hasn't been loaded yet.
	 * @return A SpriteSheet matching the specification.
	 * @throws IOException If the file cannot be loaded.
	 */
	public SpriteSheet get(String fileName, int spritesPerX, int spritesPerY,
			int spriteBorderSize, int filter) throws IOException {
		SoftReference<SpriteSheet> ref = loadedSpriteSheets.get(fileName);
		SpriteSheet current = ref == null ? null : ref.get();
		if (current != null) {
			return current;
		} else {
			loadedSpriteSheets.remove(fileName);
			SpriteSheet result = new SpriteSheet(bitmaps.get(fileName, filter),
					spritesPerX, spritesPerY, spriteBorderSize);
			loadedSpriteSheets.put(fileName, new SoftReference<SpriteSheet>(
					result));
			return result;
		}
	}
}
