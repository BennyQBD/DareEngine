package engine.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundData {
	private final IAudioDevice device;
	private int id;

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

	public void dispose() {
		id = device.releaseAudioData(id);
	}

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
