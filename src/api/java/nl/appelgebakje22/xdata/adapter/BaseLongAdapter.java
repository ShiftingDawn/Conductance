package nl.appelgebakje22.xdata.adapter;

public class BaseLongAdapter extends BaseNumberAdapter<Long> implements LongAdapter {

	private long value;

	public void setNumber(final long number) {
		this.value = number;
	}

	@Override
	public Long getNumber() {
		return this.value;
	}
}
