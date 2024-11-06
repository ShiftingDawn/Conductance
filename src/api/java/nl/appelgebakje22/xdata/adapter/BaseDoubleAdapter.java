package nl.appelgebakje22.xdata.adapter;

public class BaseDoubleAdapter extends BaseNumberAdapter<Double> implements DoubleAdapter {

	private double value;

	public void setNumber(final double number) {
		this.value = number;
	}

	@Override
	public Double getNumber() {
		return this.value;
	}
}
