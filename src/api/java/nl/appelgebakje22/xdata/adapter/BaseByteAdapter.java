package nl.appelgebakje22.xdata.adapter;

public class BaseByteAdapter extends BaseNumberAdapter<Byte> implements ByteAdapter {

	private byte value;

	public void setNumber(final byte number) {
		this.value = number;
	}

	@Override
	public Byte getNumber() {
		return this.value;
	}
}
