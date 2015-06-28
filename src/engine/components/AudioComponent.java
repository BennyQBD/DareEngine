package engine.components;

import java.util.HashMap;
import java.util.Map;

import engine.audio.Sound;
import engine.core.entity.Entity;
import engine.core.entity.EntityComponent;
import engine.util.IDAssigner;

public class AudioComponent extends EntityComponent {
	public static final int ID = IDAssigner.getId();
	
	private Map<String, Sound> sounds;
	
	public AudioComponent(Entity entity, String[] soundNames, Sound[] soundsIn) {
		super(entity, ID);
		sounds = new HashMap<>();
		for(int i = 0; i < soundNames.length; i++) {
			sounds.put(soundNames[i], soundsIn[i]);
		}
	}
	
	public double getDefaultVolume(String soundName) {
		Sound sound = sounds.get(soundName);
		if(sound != null) {
			return sound.getDefaultVolume();
		}
		return -1.0;
	}
	
	public void setVolume(String soundName, double volume) {
		Sound sound = sounds.get(soundName);
		if(sound != null) {
			sound.setVolume(volume);
		}
	}
	
	public void play(String soundName) {
		Sound sound = sounds.get(soundName);
		if(sound != null) {
			sound.play();
		}
	}
	
	public void pause(String soundName) {
		Sound sound = sounds.get(soundName);
		if(sound != null) {
			sound.pause();
		}
	}
	
	public void stop(String soundName) {
		Sound sound = sounds.get(soundName);
		if(sound != null) {
			sound.stop();
		}
	}
}
