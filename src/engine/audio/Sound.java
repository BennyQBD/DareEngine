/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.audio;

/**
 * Represents a playable sound.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Sound {
	private IAudioDevice device;
	private int soundId;
	private double defaultVolume;
	private double volume;
	private double pitch;
	private boolean shouldLoop;

	/**
	 * Creates a new Sound. It is preferred to use this object instead of
	 * manipulating an {@link IAudioDevice}'s audio object methods directly.
	 * 
	 * @param device
	 *            The device that plays this sound
	 * @param data
	 *            The SoundData for the sound being played.
	 * @param volume
	 *            How loud the sound should be. 1.0 specifies normal volume, and
	 *            lower or higher values specify quieter or louder volumes,
	 *            respectively.
	 * @param pitch
	 *            What pitch the audio should be played at. 1.0 specifies normal
	 *            pitch, and lower or higher values specify higher or lower
	 *            pitches, respectively.
	 * @param shouldLoop
	 *            Whether the audio should automatically restart when finished
	 *            playing.
	 */
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

	/**
	 * Releases this object. This should be called when the object will no
	 * longer be used, and no methods or fields should be used after this method
	 * is called.
	 */
	public void dispose() {
		soundId = device.releaseAudioObject(soundId);
	}

	/**
	 * Plays this sound. If it is already playing, it is stopped and restarted.
	 * When the audio is finished playing, it will stop if the object's
	 * shouldLoop is false, or it will restart if the object's shouldLoop is
	 * true.
	 */
	public void play() {
		device.play(soundId);
	}

	/**
	 * Stops playing this sound, but leaves its position alone. When the object
	 * is played again, it should start where it left off.
	 */
	public void pause() {
		device.pause(soundId);
	}

	/**
	 * Stops playing this sound and resets its position to the start of the
	 * audio. When the object is played again, it should start at the beginning.
	 */
	public void stop() {
		device.stop(soundId);
	}

	/**
	 * Gets the volume this object was initially created with.
	 * 
	 * @return The volume this object was initially created with.
	 */
	public double getDefaultVolume() {
		return defaultVolume;
	}

	/**
	 * Sets the volume of this sound to a new value.
	 * 
	 * @param amt
	 *            The new volume to be set to. 1.0 specifies normal volume, and
	 *            lower or higher values specify quieter or louder volumes,
	 *            respectively.
	 */
	public void setVolume(double amt) {
		volume = amt;
		device.updateAudioObject(soundId, volume, pitch, shouldLoop);
	}
}
