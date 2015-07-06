package engine.input;

import engine.util.Util;

public class CompoundAxis implements IAxis {
	private IAxis[] axes;

	public CompoundAxis(IAxis axis1, IAxis axis2) {
		this(new IAxis[] { axis1, axis2 });
	}

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
