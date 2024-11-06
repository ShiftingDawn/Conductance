package nl.appelgebakje22.xdata.adapter;

public class BaseIntAdapter extends BaseNumberAdapter<Integer> implements IntAdapter {

	private int value;

	public void setNumber(final int number) {
		this.value = number;
	}

	@Override
	public Integer getNumber() {
		return this.value;
	}
}
