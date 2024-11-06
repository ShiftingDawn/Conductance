package nl.appelgebakje22.xdata.adapter;

public class BaseCharAdapter implements CharAdapter {

	private char value;

	@Override
	public void setChar(final char value) {
		this.value = value;
	}

	@Override
	public char getChar() {
		return this.value;
	}

	@Override
	public char getChar(final char fallback) {
		return this.value != 0 ? this.value : fallback;
	}
}
