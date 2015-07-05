/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Represents audio data for a particular audio device.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class SoundData {
	private final IAudioDevice device;
	private int id;

	/**
	 * Creates a new SoundData object. It is preferred to use this object
	 * instead of manipulating an {@link IAudioDevice}'s audio data methods
	 * directly.
	 * 
	 * @param device
	 *            The device the sound data will be created for.
	 * @param fileName
	 *            The name and path to the sound file to be loaded.
	 * @throws IOException
	 *             If the audio file cannot be loaded and sent to the
	 *             {@code device}
	 */
	public SoundData(IAudioDevice device, String fileName) throws IOException {
		this.device = device;
		this.id = 0;
		try {
			this.id = loadAudio(device, fileName);
		} catch (UnsupportedAudioFileException e) {
			throw new IOException(e);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}

	/**
	 * Releases this object. This should be called when the object will no
	 * longer be used, and no methods or fields should be used after this method
	 * is called.
	 */
	public void dispose() {
		id = device.releaseAudioData(id);
	}

	/**
	 * Gets the audio device's integer id for this audio data.
	 * 
	 * @return The audio device's integer id for this audio data.
	 */
	public int getId() {
		return id;
	}

	private static int getFormat(AudioFormat streamFormat) {
		int format = -1;
		if (streamFormat.getChannels() == 1) {
			if (streamFormat.getSampleSizeInBits() == 8) {
				format = IAudioDevice.FORMAT_MONO_8;
			} else if (streamFormat.getSampleSizeInBits() == 16) {
				format = IAudioDevice.FORMAT_MONO_16;
			}
		} else if (streamFormat.getChannels() == 2) {
			if (streamFormat.getSampleSizeInBits() == 8) {
				format = IAudioDevice.FORMAT_STEREO_8;
			} else if (streamFormat.getSampleSizeInBits() == 16) {
				format = IAudioDevice.FORMAT_STEREO_16;
			}
		}
		return format;
	}

	private static byte[] readStream(AudioInputStream stream)
			throws IOException {
		byte[] data = new byte[stream.available()];
		int bytesRead = 0;
		int totalBytesRead = 0;
		while ((bytesRead = stream.read(data, totalBytesRead, data.length
				- totalBytesRead)) != -1
				&& totalBytesRead < data.length) {
			totalBytesRead += bytesRead;
		}
		return data;
	}

	private static int loadAudio(IAudioDevice device, String fileName)
			throws UnsupportedAudioFileException, IOException {
		int result = -1;
		try (AudioInputStream stream = AudioSystem
				.getAudioInputStream(new File(fileName))) {
			AudioFormat streamFormat = stream.getFormat();

			result = device.createAudioData(readStream(stream),
					getFormat(streamFormat),
					(int) streamFormat.getSampleRate(),
					streamFormat.isBigEndian());
		}
		return result;
	}
}
