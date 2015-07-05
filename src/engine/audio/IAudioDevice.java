/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.audio;

import static org.lwjgl.openal.AL10.*;

/**
 * Interface for some piece of hardware, physical or virtual, that is capable of
 * playing audio.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public interface IAudioDevice {
	/** Format for 8 bit mono audio */
	public static final int FORMAT_MONO_8 = AL_FORMAT_MONO8;
	/** Format for 16 bit mono audio */
	public static final int FORMAT_MONO_16 = AL_FORMAT_MONO16;
	/** Format for 8 bit stereo audio */
	public static final int FORMAT_STEREO_8 = AL_FORMAT_STEREO8;
	/** Format for 16 bit stereo audio */
	public static final int FORMAT_STEREO_16 = AL_FORMAT_STEREO16;

	/**
	 * Releases any resources being used. Should not be used after this is
	 * called.
	 */
	public void dispose();

	/**
	 * Creates a piece of audio data that can be used by this audio device. It
	 * is preferred to use this through the {@link SoundData} class when
	 * possible.
	 * 
	 * @param data
	 *            The raw array of bytes specifying the piece of audio.
	 * @param format
	 *            The format of {@code data}. Should be one of the
	 *            IAudioDevice.FORMAT options.
	 * @param sampleRate
	 *            The number of samples {@code data} specifies per second of
	 *            audio.
	 * @param isBigEndian
	 *            True if {@code data} is in big endian byte ordering; false
	 *            otherwise.
	 * @return An integer identifying this audio data on this device.
	 */
	public int createAudioData(byte[] data, int format, int sampleRate,
			boolean isBigEndian);

	/**
	 * Releases and invalidates a piece of audio. The value of {@code dataId}
	 * will be invalid after this call and may be reused to identify a new piece
	 * of audio. Note that this does nothing when the null id, 0, is given for
	 * {@code dataId}.
	 * <p/>
	 * It is preferred to use this through the {@link SoundData} class when
	 * possible.
	 * 
	 * @param dataId
	 *            The integer identifying the audio data to be released.
	 * @return The null dataId of 0.
	 */
	public int releaseAudioData(int dataId);

	/**
	 * Creates an audio object that completely specifies a playable piece of
	 * sound. It is preferred to use this through the {@link Sound} class when
	 * possible.
	 * 
	 * @param dataId
	 *            The integer that identifies the audio data being played.
	 * @param volume
	 *            How loud the audio should be played at. 1.0 specifies normal
	 *            volume, and lower or higher values specify quieter or louder
	 *            volumes, respectively.
	 *            <p/>
	 *            If the device is incapable of playing audio at the specified
	 *            volume, then the audio should be played at the closest
	 *            possible volume to the volume specified.
	 * @param pitch
	 *            What pitch the audio should be played at. 1.0 specifies normal
	 *            pitch, and lower or higher values specify higher or lower
	 *            pitches, respectively.
	 *            <p/>
	 *            If the device is incapable of playing audio at the specified
	 *            pitch, then the audio should be played at the closest possible
	 *            pitch to the pitch specified.
	 * @param shouldLoop
	 *            Whether the audio should automatically restart when finished
	 *            playing.
	 * @return An integer identifying this audio object on the device.
	 */
	public int createAudioObject(int dataId, double volume, double pitch,
			boolean shouldLoop);

	/**
	 * Updates an existing audio object with new parameters. It is preferred to
	 * use this through the {@link Sound} class when possible.
	 * 
	 * @param objectId
	 *            The integer that identifies the audio object being updated.
	 * @param volume
	 *            How loud the audio should be played at. 1.0 specifies normal
	 *            volume, and lower or higher values specify quieter or louder
	 *            volumes, respectively.
	 *            <p/>
	 *            If the device is incapable of playing audio at the specified
	 *            volume, then the audio should be played at the closest
	 *            possible volume to the volume specified.
	 * @param pitch
	 *            What pitch the audio should be played at. 1.0 specifies normal
	 *            pitch, and lower or higher values specify higher or lower
	 *            pitches, respectively.
	 *            <p/>
	 *            If the device is incapable of playing audio at the specified
	 *            pitch, then the audio should be played at the closest possible
	 *            pitch to the pitch specified.
	 * @param shouldLoop
	 *            Whether the audio should automatically restart when finished
	 *            playing.
	 */
	public void updateAudioObject(int objectId, double volume, double pitch,
			boolean shouldLoop);

	/**
	 * Releases and invalidates an audio object. The value of {@code objectId}
	 * will be invalid after this call and may be reused to identify a new audio
	 * object. Note that this does nothing when the null id, 0, is given for
	 * {@code objectId}.
	 * <p/>
	 * It is preferred to use this through the {@link Sound} class when
	 * possible.
	 * 
	 * @param objectId
	 *            The integer identifying the audio object to be released.
	 * @return The null objectId of 0.
	 */
	public int releaseAudioObject(int objectId);

	/**
	 * Plays an audio object through this audio device. If it is already
	 * playing, it is stopped and restarted. When the audio is finished
	 * playing, it will stop if the object's shouldLoop is false, or it will
	 * restart if the object's shouldLoop is true. It is preferred to use this
	 * through the {@link Sound} class when possible.
	 * 
	 * @param objectId
	 *            The integer identifying the audio object to be played.
	 */
	public void play(int objectId);

	/**
	 * Stops playing an audio object through this device and resets its position
	 * to the start of the audio. When the object is played again, it should
	 * start at the beginning. It is preferred to use this through the
	 * {@link Sound} class when possible.
	 * 
	 * @param objectId
	 *            The integer identifying the audio object to be stopped.
	 */
	public void stop(int objectId);

	/**
	 * Stops playing an audio object through this device, but leaves it's
	 * position alone. When the object is played again, it should start where it
	 * left off. It is preferred to use this through the {@link Sound} class
	 * when possible.
	 * 
	 * @param objectId
	 *            The integer identifying the audio object to be paused.
	 */
	public void pause(int objectId);
}
