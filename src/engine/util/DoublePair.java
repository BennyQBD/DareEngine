package engine.util;

public class DoublePair {
	public DoublePair(double val1, double val2) {
		this.setVal1(val1);
		this.setVal2(val2);
	}

	private double val1;
	private double val2;

	public double getVal1() {
		return val1;
	}

	public double getVal2() {
		return val2;
	}

	public void setVal1(double val1) {
		this.val1 = val1;
	}

	public void setVal2(double val2) {
		this.val2 = val2;
	}
}
