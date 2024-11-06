package nl.appelgebakje22.xdata.adapter;

import org.jetbrains.annotations.Nullable;

public interface AdapterFactory {

	default NullTypeAdapter ofNull() {
		return new NullTypeAdapter();
	}

	BooleanAdapter ofBoolean(boolean value);

	default BooleanAdapter ofBoolean() {
		return this.ofBoolean(false);
	}

	ByteAdapter ofByte(byte value);

	default ByteAdapter ofByte() {
		return this.ofByte((byte) 0);
	}

	ShortAdapter ofShort(short value);

	default ShortAdapter ofShort() {
		return this.ofShort((short) 0);
	}

	IntAdapter ofInt(int value);

	default IntAdapter ofInt() {
		return this.ofInt(0);
	}

	LongAdapter ofLong(long value);

	default LongAdapter ofLong() {
		return this.ofLong(0);
	}

	FloatAdapter ofFloat(float value);

	default FloatAdapter ofFloat() {
		return this.ofFloat((float) 0);
	}

	DoubleAdapter ofDouble(double value);

	default DoubleAdapter ofDouble() {
		return this.ofDouble(0);
	}

	CharAdapter ofChar(char value);

	default CharAdapter ofChar() {
		return this.ofChar((char) 0);
	}

	StringAdapter ofString(@Nullable String value);

	default StringAdapter ofString() {
		return this.ofString(null);
	}

	ArrayAdapter array();

	TableAdapter table();
}
