package nl.appelgebakje22.xdata.adapter;

import org.jetbrains.annotations.UnknownNullability;

public interface TableAdapter extends BaseAdapter {

	void set(String key, boolean value);

	void set(String key, byte value);

	void set(String key, short value);

	void set(String key, int value);

	void set(String key, long value);

	void set(String key, float value);

	void set(String key, double value);

	void set(String key, char value);

	void set(String key, String value);

	void set(String key, BaseAdapter value);

	ArrayAdapter setArray(String key);

	TableAdapter setTable(String key);

	boolean has(String key);

	boolean getBoolean(String key, boolean fallback);

	default boolean getBoolean(final String key) {
		return this.getBoolean(key, false);
	}

	byte getByte(String key, byte fallback);

	default byte getByte(final String key) {
		return this.getByte(key, (byte) 0);
	}

	short getShort(String key, short fallback);

	default short getShort(final String key) {
		return this.getShort(key, (short) 0);
	}

	int getInt(String key, int fallback);

	default int getInt(final String key) {
		return this.getInt(key, 0);
	}

	long getLong(String key, long fallback);

	default long getLong(final String key) {
		return this.getLong(key, 0);
	}

	float getFloat(String key, float fallback);

	default float getFloat(final String key) {
		return this.getFloat(key, (float) 0);
	}

	double getDouble(String key, double fallback);

	default double getDouble(final String key) {
		return this.getDouble(key, 0);
	}

	char getChar(String key, char fallback);

	default char getChar(final String key) {
		return this.getChar(key, (char) 0);
	}

	@UnknownNullability
	String getString(String key, String fallback);

	@UnknownNullability
	default String getString(final String key) {
		return this.getString(key, null);
	}

	@UnknownNullability
	ArrayAdapter getArray(String key);

	@UnknownNullability
	TableAdapter getTable(String key);

	@UnknownNullability
	BaseAdapter get(String key);

	void remove(String key);

	int size();

	String[] getKeys();
}
