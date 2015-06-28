package engine.util.factory;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import engine.rendering.SpriteSheet;

public class SpriteSheetFactory {
	private final TextureFactory bitmaps;
	private final Map<String, SoftReference<SpriteSheet>> loadedSpriteSheets;

	public SpriteSheetFactory(TextureFactory bitmaps) {
		this.bitmaps = bitmaps;
		this.loadedSpriteSheets = new HashMap<>();
	}

	public SpriteSheet get(String fileName, int spritesPerX, int spritesPerY, int spriteBorderSize, int filter)
			throws IOException {
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
