package engine.util.factory;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import engine.rendering.ArrayBitmap;
import engine.rendering.Texture;
import engine.rendering.IRenderDevice;

public class TextureFactory {
	private final IRenderDevice device;
	private final String filePath;
	private Map<String, SoftReference<Texture>> loadedBitmaps;

	public TextureFactory(IRenderDevice device, String filePath) {
		this.device = device;
		this.filePath = filePath;
		this.loadedBitmaps = new HashMap<>();
	}

	public Texture get(String fileName, int filter) throws IOException {
		fileName = filePath + fileName;
		SoftReference<Texture> ref = loadedBitmaps.get(fileName);
		Texture current = ref == null ? null : ref.get();
		if(current != null) {
			return current;
		} else {
			loadedBitmaps.remove(fileName);
			Texture result = new Texture(device, new ArrayBitmap(fileName), filter);
			loadedBitmaps.put(fileName, new SoftReference<Texture>(result));
			return result;
		}
	}
}

