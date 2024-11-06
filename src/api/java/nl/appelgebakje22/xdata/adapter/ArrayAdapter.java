package nl.appelgebakje22.xdata.adapter;

import org.jetbrains.annotations.UnknownNullability;

public interface ArrayAdapter extends BaseAdapter {

	void add(boolean value);

	void add(byte value);

	void add(short value);

	void add(int value);

	void add(long value);

	void add(float value);

	void add(double value);

	void add(char value);

	void add(String value);

	void add(BaseAdapter value);

	ArrayAdapter addArray();

	TableAdapter addTable();

	void set(int index, boolean value);

	void set(int index, byte value);

	void set(int index, short value);

	void set(int index, int value);

	void set(int index, long value);

	void set(int index, float value);

	void set(int index, double value);

	void set(int index, char value);

	void set(int index, String value);

	void set(int index, BaseAdapter value);

	ArrayAdapter setArray(int index);

	TableAdapter setTable(int index);

	boolean has(int index);

	boolean getBoolean(int index, boolean fallback);

	default boolean getBoolean(final int index) {
		return this.getBoolean(index, false);
	}

	byte getByte(int index, byte fallback);

	default byte getByte(final int index) {
		return this.getByte(index, (byte) 0);
	}

	short getShort(int index, short fallback);

	default short getShort(final int index) {
		return this.getShort(index, (short) 0);
	}

	int getInt(int index, int fallback);

	default int getInt(final int index) {
		return this.getInt(index, 0);
	}

	long getLong(int index, long fallback);

	default long getLong(final int index) {
		return this.getLong(index, 0);
	}

	float getFloat(int index, float fallback);

	default float getFloat(final int index) {
		return this.getFloat(index, (float) 0);
	}

	double getDouble(int index, double fallback);

	default double getDouble(final int index) {
		return this.getDouble(index, 0);
	}

	char getChar(int index, char fallback);

	default char getChar(final int index) {
		return this.getChar(index, (char) 0);
	}

	@UnknownNullability
	String getString(int index, String fallback);

	@UnknownNullability
	default String getString(final int index) {
		return this.getString(index, null);
	}

	@UnknownNullability
	ArrayAdapter getArray(int index);

	@UnknownNullability
	TableAdapter getTable(int index);

	@UnknownNullability
	BaseAdapter get(int index);

	void remove(int index);

	int size();
}
