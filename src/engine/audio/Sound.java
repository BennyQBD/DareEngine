package engine.audio;
public class Sound {
	private IAudioDevice device;
	private int soundId;
	private double defaultVolume;
	private double volume;
	private double pitch;
	private boolean shouldLoop;

	public Sound(IAudioDevice device, SoundData data, double volume,
			double pitch, boolean shouldLoop) {
		this.device = device;
		this.soundId = device.createAudioObject(data.getId(), volume, pitch,
				shouldLoop);
		this.defaultVolume = volume;
		this.volume = volume;
		this.pitch = pitch;
		this.shouldLoop = shouldLoop;
	}
	
	@Override
	protected void finalize() throws Throwable {
		dispose();
		super.finalize();
	}

	public void dispose() {
		soundId = device.releaseAudioObject(soundId);
	}
	
	public void play() {
		device.play(soundId);
	}
	
	public void pause() {
		device.pause(soundId);
	}
	
	public void stop() {
		device.stop(soundId);
	}
	
	public double getDefaultVolume() {
		return defaultVolume;
	}
	
	public void setVolume(double amt) {
		volume = amt;
		device.updateAudioObject(soundId, volume, pitch, shouldLoop);
	}
}
