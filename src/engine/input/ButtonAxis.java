/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.input;

/**
 * Axis composed of two buttons.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class ButtonAxis implements IAxis {
	private IButton positive;
	private IButton negative;
	
	/**
	 * Creates an axis from two buttons
	 * 
	 * @param negative When this button is down, the axis is negative.
	 * @param positive When this button is down, the axis is positive.
	 */
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
