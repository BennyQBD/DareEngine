/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.components;

import java.util.HashMap;
import java.util.Map;

import engine.audio.Sound;
import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.util.IDAssigner;

/**
 * Holds sounds that can be played by an entity.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class AudioComponent extends EntityComponent {
	public static final int ID = IDAssigner.getId();

	private Map<String, Sound> sounds;

	/**
	 * Creates a new AudioComponent
	 * 
	 * @param entity
	 *            The entity this component is attached to.
	 * @param soundNames
	 *            Strings identifying the sounds being passed in.
	 * @param soundsIn
	 *            The sounds being added to the component.
	 */
	public AudioComponent(Entity entity, String[] soundNames, Sound[] soundsIn) {
		super(entity, ID);
		sounds = new HashMap<>();
		for (int i = 0; i < soundNames.length; i++) {
			sounds.put(soundNames[i], soundsIn[i]);
		}
	}

	/**
	 * Gets the volume that the sound was originally created with.
	 * 
	 * @param soundName
	 *            The sound of interest.
	 * @return default volume if sound exists, -1.0 otherwise.
	 */
	public double getDefaultVolume(String soundName) {
		Sound sound = sounds.get(soundName);
		if (sound != null) {
			return sound.getDefaultVolume();
		}
		return -1.0;
	}

	/**
	 * Sets the volume of a sound to a particular value.
	 * 
	 * @param soundName
	 *            The sound of interest.
	 * @param volume
	 *            How loud the audio should be played at. 1.0 specifies normal
	 *            volume, and lower or higher values specify quieter or louder
	 *            volumes, respectively.
	 */
	public void setVolume(String soundName, double volume) {
		Sound sound = sounds.get(soundName);
		if (sound != null) {
			sound.setVolume(volume);
		}
	}

	/**
	 * Plays a sound. If it is already playing, it is stopped and restarted.
	 * When the audio is finished playing, it will stop if the object's
	 * shouldLoop is false, or it will restart if the object's shouldLoop is
	 * true.
	 * 
	 * @param soundName
	 *            The sound to be played.
	 */
	public void play(String soundName) {
		Sound sound = sounds.get(soundName);
		if (sound != null) {
			sound.play();
		}
	}

	/**
	 * Stops playing this sound, but leaves its position alone. When the object
	 * is played again, it should start where it left off.
	 * 
	 * @param soundName
	 *            The sound to be paused.
	 */
	public void pause(String soundName) {
		Sound sound = sounds.get(soundName);
		if (sound != null) {
			sound.pause();
		}
	}

	/**
	 * Stops playing this sound and resets its position to the start of the
	 * audio. When the object is played again, it should start at the beginning.
	 * 
	 * @param soundName
	 *            The sound to be stopped.
	 */
	public void stop(String soundName) {
		Sound sound = sounds.get(soundName);
		if (sound != null) {
			sound.stop();
		}
	}
}
