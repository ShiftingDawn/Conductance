package nl.appelgebakje22.xdata.adapter;

public class BaseBooleanAdapter implements BooleanAdapter {

	private boolean value;

	@Override
	public void setBoolean(final boolean value) {
		this.value = value;
	}

	@Override
	public boolean getBoolean() {
		return this.value;
	}

	@Override
	public boolean getBoolean(final boolean fallback) {
		return this.value || fallback;
	}
}
