/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.util;

/**
 * Used to delay actions by a certain amount of time.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class Delay {
	private final double duration;
	private double time;

	/**
	 * Creates a new Delay.
	 * 
	 * @param duration
	 *            How long the delay takes.
	 */
	public Delay(double duration) {
		this.duration = duration;
		this.time = 0.0;
	}

	/**
	 * Updates the delay and returns whether or not the delay is over yet.
	 * 
	 * @param delta
	 *            The amount of time passed since this method was last called.
	 * @return Whether the delay is finished or not.
	 */
	public boolean over(double delta) {
		this.time += delta;
		return time >= duration;
	}

	/**
	 * Resets the delay to the beginning.
	 */
	public void reset() {
		this.time = 0.0;
	}
}
