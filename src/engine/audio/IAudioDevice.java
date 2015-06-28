package engine.audio;
import static org.lwjgl.openal.AL10.*;

public interface IAudioDevice {
	public static final int FORMAT_MONO_8 = AL_FORMAT_MONO8;
	public static final int FORMAT_MONO_16 = AL_FORMAT_MONO16;
	public static final int FORMAT_STEREO_8 = AL_FORMAT_STEREO8;
	public static final int FORMAT_STEREO_16 = AL_FORMAT_STEREO16;
	public void dispose();
	public int createAudioData(byte[] data, int format, int sampleRate, boolean isBigEndian);
	public int releaseAudioData(int dataId);
	
	public int createAudioObject(int dataId, double volume, double pitch, boolean shouldLoop);
	public void updateAudioObject(int objectId, double volume, double pitch, boolean shouldLoop);
	public int releaseAudioObject(int objectId);
	
	public void play(int objectId);
	public void stop(int objectId);
	public void pause(int objectId);
}
