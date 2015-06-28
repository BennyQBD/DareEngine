package engine.audio.openal;
import static org.lwjgl.openal.AL10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;

import engine.audio.IAudioDevice;

public class OpenALAudioDevice implements IAudioDevice {
	private class AudioObject {
		public AudioObject(int dataId, double volume, double pitch, boolean loop) {
			this.dataId = dataId;
			this.volume = volume;
			this.pitch = pitch;
			this.loop = loop;
		}

		private int dataId;
		private double volume;
		private double pitch;
		private boolean loop;
	}

	private class AudioChannel {
		private int source;

		public AudioChannel() {
			this.source = alGenSources();
		}

		public void dispose() {
			if(source != 0) {
				alDeleteSources(source);
			}
			source = 0;
		}

		public boolean isFree() {
			int state = alGetSourcei(source, AL_SOURCE_STATE);
			return state != AL_PLAYING && state != AL_PAUSED;
		}
	}

	private Map<Integer, AudioObject> objects;
	private Map<Integer, AudioChannel> channels;
	private int currentObjectId;

	public OpenALAudioDevice() throws LWJGLException {
		AL.create();
		channels = new HashMap<>();
		objects = new HashMap<>();
		currentObjectId = 1;
	}

	@Override
	public void dispose() {
		Iterator<Entry<Integer, AudioChannel>> it = channels.entrySet()
				.iterator();
		while (it.hasNext()) {
			it.next().getValue().dispose();
		}
		AL.destroy();
	}

	private static ByteBuffer toByteBuffer(byte[] data, boolean isStereo,
			boolean isBigEndian) {
		ByteBuffer dest = ByteBuffer.allocateDirect(data.length);
		dest.order(ByteOrder.nativeOrder());
		ByteBuffer src = ByteBuffer.wrap(data);
		src.order(isBigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
		if (isStereo) {
			ShortBuffer destAsShort = dest.asShortBuffer();
			ShortBuffer srcAsShort = src.asShortBuffer();
			while (srcAsShort.hasRemaining()) {
				destAsShort.put(srcAsShort.get());
			}
		} else {
			while (src.hasRemaining()) {
				dest.put(src.get());
			}
		}
		dest.rewind();
		return dest;
	}

	@Override
	public int createAudioData(byte[] data, int format, int sampleRate,
			boolean isBigEndian) {
		int buffer = alGenBuffers();
		boolean isStereo = format == IAudioDevice.FORMAT_STEREO_16
				|| format == IAudioDevice.FORMAT_MONO_16;
		alBufferData(buffer, format, toByteBuffer(data, isStereo, isBigEndian), sampleRate);
		return buffer;
	}

	@Override
	public int releaseAudioData(int dataId) {
		if (dataId != 0) {
			alDeleteBuffers(dataId);
		}
		return 0;
	}

	@Override
	public int createAudioObject(int dataId, double volume, double pitch,
			boolean shouldLoop) {
		AudioObject object = new AudioObject(dataId, volume, pitch, shouldLoop);
		int id = currentObjectId++;
		objects.put(id, object);
		return id;
	}
	
	@Override
	public void updateAudioObject(int objectId, double volume, double pitch, boolean shouldLoop) {
		if(objectId == 0) {
			return;
		}
		AudioObject object = objects.get(objectId);
		object.volume = volume;
		object.pitch = pitch;
		object.loop = shouldLoop;
	}

	@Override
	public int releaseAudioObject(int objectId) {
		if (objectId != 0) {
			objects.remove(objectId);
		}
		return 0;
	}

	private AudioChannel getAudioChannel(int objectId) {
		if (channels.containsKey(objectId)) {
			return channels.get(objectId);
		}

		Iterator<Entry<Integer, AudioChannel>> it = channels.entrySet()
				.iterator();

		while (it.hasNext()) {
			Entry<Integer, AudioChannel> entry = it.next();
			if (entry.getValue().isFree()) {
				AudioChannel result = entry.getValue();
				it.remove();
				channels.put(objectId, result);
				return result;
			}
		}

		AudioChannel result = new AudioChannel();
		channels.put(objectId, result);
		return result;
	}

	@Override
	public void play(int objectId) {
		AudioChannel channel = getAudioChannel(objectId);
		AudioObject object = objects.get(objectId);
		int source = channel.source;
		alSourcei(source, AL_BUFFER, object.dataId);
		alSourcef(source, AL_PITCH, (float) object.pitch);
		alSourcef(source, AL_GAIN, (float) object.volume);
		alSourcei(source, AL_LOOPING, object.loop ? AL_TRUE : AL_FALSE);
		alSourcePlay(source);
	}

	@Override
	public void stop(int objectId) {
		AudioChannel channel = channels.get(objectId);
		if (channel != null) {
			alSourceStop(channel.source);
		}
	}

	@Override
	public void pause(int objectId) {
		AudioChannel channel = channels.get(objectId);
		if (channel != null) {
			alSourcePause(channel.source);
		}
	}
}
