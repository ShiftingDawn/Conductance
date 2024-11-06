package nl.appelgebakje22.xdata.adapter;

public interface NetworkOutput {

	void write(boolean data);

	void write(byte data);

	void write(short data);

	void write(int data);

	void write(long data);

	void write(float data);

	void write(double data);

	void write(char data);

	void write(String data);

	void write(byte[] bytes, int start, int length);

	default void write(final byte[] bytes) {
		this.write(bytes, 0, bytes.length);
	}
}
