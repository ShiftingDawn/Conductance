package nl.appelgebakje22.xdata.adapter;

public class BaseShortAdapter extends BaseNumberAdapter<Short> implements ShortAdapter {

	private short value;

	public void setNumber(final short number) {
		this.value = number;
	}

	@Override
	public Short getNumber() {
		return this.value;
	}
}
