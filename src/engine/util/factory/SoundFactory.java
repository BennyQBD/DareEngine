/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util.factory;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import engine.audio.IAudioDevice;
import engine.audio.Sound;
import engine.audio.SoundData;

/**
 * A factory for creating Sounds.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class SoundFactory {
	private final IAudioDevice device;
	private final String filePath;
	private final Map<String, SoftReference<SoundData>> loaded;

	/**
	 * Creates a new Sound Factory
	 * 
	 * @param device
	 *            The device to generate sounds for.
	 * @param filePath
	 *            The base path for sound files.
	 */
	public SoundFactory(IAudioDevice device, String filePath) {
		this.device = device;
		this.filePath = filePath;
		this.loaded = new HashMap<>();
	}

	/**
	 * Gets a new object from the factory. If the desired sound data is already
	 * loaded, then the sound data is reused and not loaded again. Otherwise,
	 * the sound data is loaded from the specified file.
	 * 
	 * @param fileName
	 *            The name of an audio file to be loaded.
	 * @param volume
	 *            How loud the audio should be played at. 1.0 specifies normal
	 *            volume, and lower or higher values specify quieter or louder
	 *            volumes, respectively.
	 * @param pitch
	 *            What pitch the audio should be played at. 1.0 specifies normal
	 *            pitch, and lower or higher values specify higher or lower
	 *            pitches, respectively.
	 * @param shouldLoop
	 *            Whether the audio should automatically restart when finished
	 *            playing.
	 * @return A Sound matching the specification.
	 * @throws IOException If the file cannot be loaded.
	 */
	public Sound get(String fileName, double volume, double pitch,
			boolean shouldLoop) throws IOException {
		fileName = filePath + fileName;
		SoftReference<SoundData> ref = loaded.get(fileName);
		SoundData data = ref == null ? null : ref.get();
		if (data == null) {
			loaded.remove(fileName);
			data = new SoundData(device, fileName);
			loaded.put(fileName, new SoftReference<SoundData>(data));
		}

		return new Sound(device, data, volume, pitch, shouldLoop);
	}
}
