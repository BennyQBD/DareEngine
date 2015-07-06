package engine.input;

public class ButtonAxis implements IAxis {
	private IButton positive;
	private IButton negative;
	
	public ButtonAxis(IButton negative, IButton positive) {
		this.negative = negative;
		this.positive = positive;
	}
	
	@Override
	public double getAmount() {
		double result = 0.0;
		if(positive.isDown()) {
			result += 1.0;
		}
		if(negative.isDown()) {
			result -= 1.0;
		}
		return result;
	}
}
