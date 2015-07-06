/** 
 * Copyright (c) 2015, Benny Bobaganoosh. All rights reserved.
 * License terms are in the included LICENSE.txt file.
 */
package engine.input;

import engine.util.Util;

/**
 * Axis composed of multiple other axes.
 * 
 * @author Benny Bobaganoosh (thebennybox@gmail.com)
 */
public class CompoundAxis implements IAxis {
	private IAxis[] axes;

	/**
	 * Creates a new axis from two other axes.
	 * 
	 * @param axis1 The first axis being combined.
	 * @param axis2 The second axis being combined.
	 */
	public CompoundAxis(IAxis axis1, IAxis axis2) {
		this(new IAxis[] { axis1, axis2 });
	}

	/**
	 * Creates a new axis from a list of axes.
	 * @param axes The list of axes to combine.
	 */
	public CompoundAxis(IAxis[] axes) {
		this.axes = axes;
	}

	@Override
	public double getAmount() {
		double result = 0.0;
		for(int i = 0; i < axes.length; i++) {
			result += axes[i].getAmount();
		}
		return Util.clamp(result, -1.0, 1.0);
	}

}
