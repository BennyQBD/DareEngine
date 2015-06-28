package engine.util;

public class Delay {
	private final double duration;
	private double time;

	public Delay(double duration) {
		this.duration = duration;
		this.time = 0.0;
	}

	public boolean over(double delta) {
		this.time += delta;
		return time >= duration;
	}

	public void reset() {
		this.time = 0.0;
	}
}
