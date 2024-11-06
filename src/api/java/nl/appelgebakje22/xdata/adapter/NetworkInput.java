package nl.appelgebakje22.xdata.adapter;

public interface NetworkInput {

	boolean readBoolean();

	byte readByte();

	short readShort();

	int readInt();

	long readLong();

	float readFloat();

	double readDouble();

	char readChar();

	String readString();

	void read(byte[] bytes, int start, int length);

	default void read(final byte[] bytes) {
		this.read(bytes, 0, bytes.length);
	}
}
