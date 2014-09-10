package engine.core;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class AudioClip
{
//	private static Sequencer sequencer;
//	private static final float AUDIO_VOLUME = -5.0f;
//	private static final float DECAY_FACTOR = 0.12f;

	private final Clip m_clip;

	public AudioClip(String fileName)
	{
		Clip clip = null;
		try
		{
			AudioInputStream stream = 
				AudioSystem.getAudioInputStream(
						new File(fileName));
			clip = 
				(Clip)AudioSystem.getLine(
						new DataLine.Info(Clip.class,
						   	stream.getFormat()));
			
			clip.open(stream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		finally
		{
			m_clip = clip;
		}
	}

	public void start()
	{
		if(m_clip.isActive())
		{
			m_clip.stop();
		}
		
		m_clip.setFramePosition(0);
		m_clip.start();
	}
}
