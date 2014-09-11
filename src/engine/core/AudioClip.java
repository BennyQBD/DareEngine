/*
 * Copyright (c) 2014, Benny Bobaganoosh
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package engine.core;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/**
 * Represents a piece of audio that can be played in game.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class AudioClip {
	private final Clip clip;

	/**
	 * Loads an audio clip from a file.
	 * 
	 * @param fileName
	 *            The name of the file to load.
	 */
	public AudioClip(String fileName) {
		Clip clip = null;
		try {
			AudioInputStream stream = AudioSystem
					.getAudioInputStream(new File(fileName));
			clip = (Clip) AudioSystem.getLine(new DataLine.Info(
					Clip.class, stream.getFormat()));

			clip.open(stream);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			this.clip = clip;
		}
	}

	/**
	 * Starts playing the audio clip from the beginning. If it is already
	 * playing, the clip is stopped and restarted.
	 */
	public void start() {
		if (clip.isActive()) {
			clip.stop();
		}

		clip.setFramePosition(0);
		clip.start();
	}
}
