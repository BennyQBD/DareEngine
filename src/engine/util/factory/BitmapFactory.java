package engine.util.factory;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import engine.rendering.Bitmap;
import engine.rendering.IRenderDevice;

public class BitmapFactory {
	private final IRenderDevice device;
	private final String filePath;
	private Map<String, SoftReference<Bitmap>> loadedBitmaps;

	public BitmapFactory(IRenderDevice device, String filePath) {
		this.device = device;
		this.filePath = filePath;
		this.loadedBitmaps = new HashMap<>();
	}

	public Bitmap get(String fileName, int filter) throws IOException {
		fileName = filePath + fileName;
		SoftReference<Bitmap> ref = loadedBitmaps.get(fileName);
		Bitmap current = ref == null ? null : ref.get();
		if(current != null) {
			return current;
		} else {
			loadedBitmaps.remove(fileName);
			Bitmap result = new Bitmap(device, fileName, filter);
			loadedBitmaps.put(fileName, new SoftReference<Bitmap>(result));
			return result;
		}
	}
}

